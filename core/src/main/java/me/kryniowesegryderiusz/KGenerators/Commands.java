package me.kryniowesegryderiusz.KGenerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.kryniowesegryderiusz.KGenerators.Classes.Generator;
import me.kryniowesegryderiusz.KGenerators.Classes.PlayerLimits;
import me.kryniowesegryderiusz.KGenerators.EnumsManager.Message;
import me.kryniowesegryderiusz.KGenerators.Utils.ConfigManager;
import me.kryniowesegryderiusz.KGenerators.Utils.LangUtils;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("kgenerators.commands") || sender instanceof ConsoleCommandSender){

			if (args.length == 0){
				LangUtils.sendHelpMessage(sender);
				return false;
			}
		
			switch(args[0]){
				case "reload":
					if (sender.hasPermission("kgenerators.reload") || sender instanceof ConsoleCommandSender){
						
						try {
							KGenerators.messagesFile = ConfigManager.getConfig("lang/"+KGenerators.lang+".yml", null, false);
						} catch (FileNotFoundException e1) {
							System.out.println("[KGenerators] !!! ERROR !!! Cant find lang/" + KGenerators.lang + ".yml file");
						}
				    	try {
							KGenerators.config.loadConfig();
							KGenerators.messagesFile.loadConfig();
						} catch (IOException
								| InvalidConfigurationException e) {
							e.printStackTrace();
						}
						ConfigLoader.loadConfig();
						try {
							LangUtils.loadMessages();
						} catch (IOException e) {
							e.printStackTrace();
						}
						LangUtils.sendMessage(sender, Message.CommandsReloadDone);
					}
					else
					{
						LangUtils.addReplecable("<permission>", "kgenerators.reload");
						LangUtils.sendMessage(sender, Message.CommandsReloadNoPermission);
					}
					break;
				case "getall":
					if (sender instanceof Player){
						if (sender.hasPermission("kgenerators.getall")){
							Player player = (Player) sender;
					        for (HashMap.Entry<String, Generator> generatorhmap : KGenerators.generators.entrySet()) {
					        	Generator generator = generatorhmap.getValue();
					        	player.getInventory().addItem(generator.getGeneratorItem());
					        }
					        LangUtils.sendMessage(sender, Message.CommandsGetallRecieved);
						}
						else
						{
							LangUtils.addReplecable("<permission>", "kgenerators.getall");
							LangUtils.sendMessage(sender, Message.CommandsGetallNoPermission);
						}
					}
					else
					{
						System.out.println("[KGenerators] Use that command as player!");
					}
					break;
				case "list":
						if (sender.hasPermission("kgenerators.list") || sender instanceof ConsoleCommandSender){
							LangUtils.sendMessage(sender, Message.CommandsListHeader);
					        for (Entry<String, Generator> e : KGenerators.generators.entrySet()) {
					        	LangUtils.addReplecable("<generator>", e.getValue().getGeneratorItem().getItemMeta().getDisplayName());
					        	LangUtils.addReplecable("<generatorID>", e.getKey());
					        	LangUtils.sendMessage(sender, Message.CommandsListList);
					        }
						}
						else
						{
							LangUtils.addReplecable("<permission>", "kgenerators.list");
							LangUtils.sendMessage(sender, Message.CommandsListNoPermission);
						}
					break;
				case "check":
					if (sender.hasPermission("kgenerators.check") || sender instanceof ConsoleCommandSender){
						if (args.length == 1)
						{
							if (sender instanceof Player){
								Player player = (Player) sender;
								check(sender, player.getName());
							}
							else
							{
								System.out.println("[KGenerators] Use that command as player!");
							}
						}
						else
						{
							if (sender.hasPermission("kgenerators.check.others"))
							{
								check(sender, args[1]);
							}
							else
							{
								LangUtils.addReplecable("<permission>", "kgenerators.check.others");
								LangUtils.sendMessage(sender, Message.CommandsCheckNoPermissionOthers);
							}
						}
					}
					else
					{
						LangUtils.addReplecable("<permission>", "kgenerators.check");
						LangUtils.sendMessage(sender, Message.CommandsCheckNoPermission);
					}
					break;
				case "give":
					if (sender.hasPermission("kgenerators.give") || sender instanceof ConsoleCommandSender){
						if (args.length >= 3){
							Player player = Bukkit.getPlayer(args[1]);
							if (player == null){
								LangUtils.sendMessage(sender, Message.CommandsAnyPlayerNotOnline);
							}
							else
							{
								String generatorID = args[2];
								if (!KGenerators.generators.containsKey(generatorID)){
									LangUtils.sendMessage(sender, Message.CommandsGiveGeneratorDoesntExist);
									break;
								}
								
								ItemStack item = KGenerators.generators.get(generatorID).getGeneratorItem();
								
								player.getInventory().addItem(item);
								
								LangUtils.addReplecable("<generator>", item.getItemMeta().getDisplayName());
								LangUtils.addReplecable("<player>", player.getDisplayName());
								LangUtils.sendMessage(sender, Message.CommandsGiveGeneratorGiven);
								
								LangUtils.addReplecable("<generator>", item.getItemMeta().getDisplayName());
								LangUtils.sendMessage(sender, Message.CommandsGiveGeneratorRecieved);
							}
						}
						else
						{
							LangUtils.sendMessage(sender, Message.CommandsGiveUsage);
						}
					}
					else
					{
						LangUtils.addReplecable("<permission>", "kgenerators.give");
						LangUtils.sendMessage(sender, Message.CommandsGiveNoPermission);
					}
					break;
				default:
					LangUtils.sendMessage(sender, Message.CommandsAnyWrong);
					break;
			}
		}
		else
		{
			LangUtils.addReplecable("<permission>", "kgenerators.commands");
			LangUtils.sendMessage(sender, Message.CommandsAnyNoPermission);
		}

		return false;
	}
	
	void check(CommandSender sender, String nick)
	{
		Player player = Bukkit.getPlayer(nick);
		if (player != null)
		{
			LangUtils.addReplecable("<player>", player.getDisplayName());
			LangUtils.sendMessage(sender, Message.CommandsCheckHeader);
			
			for (Entry<String, Generator> e : KGenerators.generators.entrySet())
			{
				String nr = String.valueOf(PerPlayerGenerators.getPlayerGeneratorsCount(player, e.getKey()));
				
				LangUtils.addReplecable("<number>", nr);
				LangUtils.addReplecable("<generator>", e.getValue().getGeneratorItem().getItemMeta().getDisplayName());
				LangUtils.sendMessage(sender, Message.CommandsCheckList);
			}
			
			if (KGenerators.overAllPerPlayerGeneratorsEnabled)
			{
				LangUtils.addReplecable("<player>", player.getDisplayName());
				LangUtils.sendMessage(sender, Message.CommandsLimitsHeader);
				PlayerLimits pLimits = new PlayerLimits(player);
				for (Entry<String, Generator> e : KGenerators.generators.entrySet())
				{
					int limit = pLimits.getLimit(e.getKey());
					String limitS;
					if (limit == -1){limitS = LangUtils.getMessage(Message.CommandsLimitsNone, false);}	else {limitS = String.valueOf(limit);}
					
					LangUtils.addReplecable("<generator>", e.getValue().getGeneratorItem().getItemMeta().getDisplayName());
					LangUtils.addReplecable("<limit>", limitS);
					LangUtils.sendMessage(sender, Message.CommandsLimitsList);
				}
				int limit = pLimits.getGlobalLimit();	String limitS;
				if (limit == -1){limitS = LangUtils.getMessage(Message.CommandsLimitsNone, false);}	else {limitS = String.valueOf(limit);}
				LangUtils.addReplecable("<limit>", limitS);
				LangUtils.sendMessage(sender, Message.CommandsLimitsOverall);
				
			}
		}
		else
		{
			LangUtils.sendMessage(sender, Message.CommandsAnyPlayerDoesntExist);
		}
	}
}
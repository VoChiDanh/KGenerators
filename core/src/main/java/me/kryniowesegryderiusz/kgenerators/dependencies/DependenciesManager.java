package me.kryniowesegryderiusz.kgenerators.dependencies;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.dependencies.enums.Dependency;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.BentoBoxHook;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.IridiumSkyblockHook;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.JetsMinionsHook;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.MinionsHook;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.SlimefunHook;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.SuperiorSkyblock2Hook;
import me.kryniowesegryderiusz.kgenerators.dependencies.hooks.VaultHook;
import me.kryniowesegryderiusz.kgenerators.generators.generator.objects.Generator;
import me.kryniowesegryderiusz.kgenerators.logger.Logger;

public class DependenciesManager {
	
	@Getter private ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
	
	public DependenciesManager() {
		
		dependencies.clear();
		
		/*
		 * On enable checks
		 */
		
    	if (Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null) {
    		Logger.info("Dependencies: Detected plugin SuperiorSkyblock2. Hooking into it.");
    		dependencies.add(Dependency.SUPERIOR_SKYBLOCK_2);
    		SuperiorSkyblock2Hook.setup();
    	}
    	
    	if (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null) {
    		Logger.info("Dependencies: Detected plugin ItemsAdder. Hooking into it.");
    		dependencies.add(Dependency.ITEMS_ADDER);
    	}
    	
    	if (VaultHook.setupEconomy())
    	{
    		Logger.info("Dependencies: Detected Vault economy. Hooked into it.");
    		dependencies.add(Dependency.VAULT_ECONOMY);
    	}
    	else
    		Logger.warn("Dependencies: Vault economy was not found! Some features could not work!");
    	
    	if (VaultHook.setupPermissions())
    	{
    		Logger.info("Dependencies: Detected Vault permissions. Hooked into it.");
    		dependencies.add(Dependency.VAULT_PERMISSIONS);
    	}
    	else
    		Logger.warn("Dependencies: Vault permissions was not found! Some features could not work!");
    	
    	/*
    	 * Delayed checks
    	 */
    	
    	Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
	    	if (Bukkit.getPluginManager().isPluginEnabled("BentoBox")) {
	    		Logger.info("Dependencies: Detected plugin BentoBox. Hooking into it.");
	    		BentoBoxHook.setup();
	    		dependencies.add(Dependency.BENTO_BOX);
	    	}
	    	
	    	if (Bukkit.getPluginManager().isPluginEnabled("IridiumSkyblock")) {
	    		Logger.info("Dependencies: Detected plugin IridiumSkyblock. Hooking into it.");
	    		IridiumSkyblockHook.setup();
	    		dependencies.add(Dependency.IRIDIUM_SKYBLOCK);
	    	}
	    	
	    	if (Bukkit.getPluginManager().isPluginEnabled("JetsMinions")) {
	    		Logger.info("Dependencies: Detected plugin JetsMinions. Hooking into it.");
	    		Main.getInstance().getServer().getPluginManager().registerEvents(new JetsMinionsHook(), Main.getInstance());
	    		dependencies.add(Dependency.JETS_MINIONS);
	    	}
	    	
	    	if (Bukkit.getPluginManager().isPluginEnabled("Minions-Revamped")) {
	    		Logger.info("Dependencies: Detected plugin Minions-Revamped. Hooking into it.");
	    		Main.getInstance().getServer().getPluginManager().registerEvents(new MinionsHook(), Main.getInstance());
	    		dependencies.add(Dependency.MINIONS);
	    	}
	    	
	    	if (Bukkit.getPluginManager().isPluginEnabled("Slimefun")) {
	    		Logger.info("Dependencies: Detected plugin Slimefun. Hooking into it.");
	    		Main.getInstance().getServer().getPluginManager().registerEvents(new SlimefunHook(), Main.getInstance());
	    		dependencies.add(Dependency.SLIMEFUN);
	    	}
	    	
	    	if (Main.getMultiVersion().getWorldGuardUtils() != null && Main.getMultiVersion().getWorldGuardUtils().isWorldGuardHooked()) {
	   			Logger.info("Dependencies: Detected plugin WorldGuard. Hooked into it.");
	   			dependencies.add(Dependency.WORLD_GUARD);
	    	}
	    	else if (Main.getMultiVersion().getWorldGuardUtils() != null)
	    	{
	    		if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
	    			Logger.error("Dependencies: Detected plugin WorldGuard, but couldnt hook into it! Search console log above for errors!");
	    		}
	    	}
	    	
	        if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
	        	Logger.info("Dependencies: Detected plugin HolographicDisplays. Hooked into it.");
	        	dependencies.add(Dependency.HOLOGRAPHIC_DISPLAYS);
	        } else if (Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")) {
	        	Logger.info("Dependencies: Detected plugin DecentHolograms. Hooked into it.");
	        	dependencies.add(Dependency.DECENT_HOLOGRAMS);
	        } else {
	        	for (Map.Entry<String, Generator> e : Main.getGenerators().getEntrySet()) {
					if ((e.getValue()).isHologram())
						Logger.warn("Generators file: Generator " + e.getKey() + " has enabled holograms, but hologram provider was not found! Holograms wouldnt work!"); 
				} 
	        }
	    	
        });
	}
	
	public boolean isEnabled(Dependency dep) {
		return this.dependencies.contains(dep);
	}
}

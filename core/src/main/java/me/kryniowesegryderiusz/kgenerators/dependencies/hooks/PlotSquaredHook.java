package me.kryniowesegryderiusz.kgenerators.dependencies.hooks;

import org.bukkit.entity.Player;

import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;

import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.dependencies.enums.Dependency;

public class PlotSquaredHook {
	
	public static boolean isPlayerAllowedToMine(Player player, org.bukkit.Location location) {
		return canHere(player, location);
	}
	
	public static boolean isPlayerAllowedToPickUp(Player player, org.bukkit.Location location) {
		return canHere(player, location);
	}
	
	public static boolean isPlayerAllowedToInteract(Player player, org.bukkit.Location location) {
		return canHere(player, location);
	}
	
	public static boolean isPlayerAllowedToPlace(Player player, org.bukkit.Location location) {
		return canHere(player, location);
	}
	
	private static boolean canHere(Player p, org.bukkit.Location l) {
		
		if (!Main.getDependencies().isEnabled(Dependency.PLOT_SQUARED))
			return true;
		
		if (p.hasPermission("kgenerators.bypass.plotsquared"))
			return true;   
        
        Location pl = Location.at(l.getWorld().getName(), l.getBlockX(), l.getBlockX(), l.getBlockX());
        
        if (!pl.isPlotArea())
        	return false;
        
        Plot plot = pl.getPlot();
        
        if (plot == null)
        	return false;
        
        if (plot.isOwner(p.getUniqueId()) || plot.getMembers().contains(p.getUniqueId()))
        	return true;
		
		return false;
		
	}

}

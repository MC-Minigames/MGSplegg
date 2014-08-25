package com.comze_instancelabs.mgsplegg;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.util.Util;

public class ICommandHandler extends CommandHandler {

	@Override
	public boolean setSpawn(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			int i = pli.arenaSetup.autoSetSpawn(plugin, args[1], p.getLocation());
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spawn " + Integer.toString(i)));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public boolean setAreaPoint(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 1) {
			int i = autoSetAreaPoint(plugin, args[1], p.getLocation());
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "area point " + Integer.toString(i)));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena>");
		}
		return true;
	}

	public int autoSetAreaPoint(JavaPlugin plugin, String arenaname, Location l) {
		int count = getAllAreaPoints(plugin, arenaname).size();
		if (count < 2) {
			Util.saveComponentForArena(plugin, arenaname, "area.point" + Integer.toString(count), l);
		}
		return count;
	}

	public static ArrayList<Location> getAllAreaPoints(JavaPlugin plugin, String arena) {
		FileConfiguration config = MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig();
		ArrayList<Location> ret = new ArrayList<Location>();
		if (!config.isSet("arenas." + arena + ".area")) {
			return ret;
		}
		for (String point : config.getConfigurationSection("arenas." + arena + ".area.").getKeys(false)) {
			ret.add(Util.getComponentForArena(plugin, arena, "area." + point));
		}
		return ret;
	}

}

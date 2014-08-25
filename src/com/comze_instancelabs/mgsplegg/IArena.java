package com.comze_instancelabs.mgsplegg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class IArena extends Arena {

	private BukkitTask timer;
	private BukkitTask starttimer;
	Main m = null;
	public int c = 30;
	BukkitTask tt;
	int currentingamecount;

	public IArena(Main m, String arena) {
		super(m, arena);
		this.m = m;
		MinigamesAPI.getAPI().pinstances.get(m).getArenaListener().loseY = 10;
	}

	public void setRadius(int i) {
		this.c = i;
	}

	public void generateArena() {

		Location start = Util.getComponentForArena(m, this.getName(), "area.point0");
		Location end = Util.getComponentForArena(m, this.getName(), "area.point1");

		int x = Math.min(start.getBlockX(), end.getBlockX());
		int y = Math.min(start.getBlockY(), end.getBlockY());
		int z = Math.min(start.getBlockZ(), end.getBlockZ());

		int x2 = Math.max(start.getBlockX(), end.getBlockX());
		int y2 = Math.max(start.getBlockY(), end.getBlockY());
		int z2 = Math.max(start.getBlockZ(), end.getBlockZ());

		for (int i = 0; i < (x2 - x); i++) {
			for (int j = 0; j <= (y2 - y); j++) {
				for (int k = 0; k < (z2 - z); k++) {
					Block b = start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y + j, z + k));
					b.setType(Material.SNOW_BLOCK);
				}
			}
		}

	}

	@Override
	public void start(boolean tp) {
		super.start(tp);
		final IArena a = this;
		Bukkit.getScheduler().runTaskLater(m, new Runnable() {
			public void run() {
				for (String p_ : a.getAllPlayers()) {
					if (Validator.isPlayerOnline(p_)) {
						Player p = Bukkit.getPlayer(p_);
						p.setWalkSpeed(0.2F);
						p.setFoodLevel(20);
						p.removePotionEffect(PotionEffectType.JUMP);
					}
				}
			}
		}, 20L);
	}

	@Override
	public void stop() {
		super.stop();
		reset();
	}

	@Override
	public void reset() {
		// TODO test
		generateArena();
	}

}

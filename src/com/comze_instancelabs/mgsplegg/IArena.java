package com.comze_instancelabs.mgsplegg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.PowerupUtil;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class IArena extends Arena {

	private BukkitTask timer;
	private BukkitTask starttimer;
	Main m = null;
	BukkitTask tt;
	int currentingamecount;
	BukkitTask powerup_task;

	public IArena(Main m, String arena) {
		super(m, arena, ArenaType.REGENERATION);
		this.m = m;
		MinigamesAPI.getAPI().pinstances.get(m).getArenaListener().loseY = 20;
	}

	/*
	 * public void generateArena() {
	 * 
	 * Location start = Util.getComponentForArena(m, this.getName(), "area.point0"); Location end = Util.getComponentForArena(m, this.getName(),
	 * "area.point1");
	 * 
	 * int x = Math.min(start.getBlockX(), end.getBlockX()); int y = Math.min(start.getBlockY(), end.getBlockY()); int z = Math.min(start.getBlockZ(),
	 * end.getBlockZ());
	 * 
	 * int x2 = Math.max(start.getBlockX(), end.getBlockX()); int y2 = Math.max(start.getBlockY(), end.getBlockY()); int z2 =
	 * Math.max(start.getBlockZ(), end.getBlockZ());
	 * 
	 * for (int i = 0; i < (x2 - x); i++) { for (int j = 0; j <= (y2 - y); j++) { for (int k = 0; k < (z2 - z); k++) { Block b =
	 * start.getWorld().getBlockAt(new Location(start.getWorld(), x + i, y + j, z + k)); b.setType(Material.SNOW_BLOCK); } } }
	 * 
	 * }
	 */

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

		powerup_task = Bukkit.getScheduler().runTaskTimer(m, new Runnable() {
			public void run() {
				if (Math.random() * 100 <= m.getConfig().getInt("config.powerup_spawn_percentage")) {
					try {
						Player p = Bukkit.getPlayer(a.getAllPlayers().get((int) Math.random() * (a.getAllPlayers().size() - 1)));
						if (p != null) {
							PowerupUtil.spawnPowerup(p.getLocation().clone().add(0D, 5D, 0D), getItemStack());
						}
					} catch (Exception e) {
						System.out.println("Use the latest MinigamesLib version to get powerups.");
					}
				}
			}
		}, 60, 60);
	}

	public ItemStack getItemStack() {
		double i = Math.random() * 100;
		ItemStack ret = new ItemStack(Material.SNOW_BALL, 32);
		if (i <= 40) { // ~40%
			// get a speed and jump boost
			ret = new ItemStack(Material.POTION);
		} else if (i > 40 && i <= 90) { // ~40%
			// get some snowballs
			ret = new ItemStack(Material.SNOW_BALL, 32);
		} else if (i > 90) { // ~10%
			// get a very good diamond shovel
			ItemStack spade = new ItemStack(Material.DIAMOND_SPADE);
			ItemMeta im = spade.getItemMeta();
			im.addEnchant(Enchantment.DIG_SPEED, 10, true);
			im.addEnchant(Enchantment.DURABILITY, 10, true);
			spade.setItemMeta(im);
			ret = spade;
		}
		return ret;
	}

	@Override
	public void reset() {
		// TODO test
		Util.loadArenaFromFileSYNC(m, this);
	}

	@Override
	public void stop() {
		if (powerup_task != null) {
			powerup_task.cancel();
		}
	}

}

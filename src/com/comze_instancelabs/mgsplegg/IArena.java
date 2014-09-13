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
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.ArenaType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
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

	int failcount = 0;

	@Override
	public void started() {
		final IArena a = this;
		powerup_task = Bukkit.getScheduler().runTaskTimer(m, new Runnable() {
			public void run() {
				if (Math.random() * 100 <= m.getConfig().getInt("config.powerup_spawn_percentage")) {
					try {
						Player p = Bukkit.getPlayer(a.getAllPlayers().get((int) Math.random() * (a.getAllPlayers().size() - 1)));
						if (p != null) {
							Util.spawnPowerup(m, a, p.getLocation().clone().add(0D, 5D, 0D), getItemStack());
						}
					} catch (Exception e) {
						if (a != null) {
							if (a.getArenaState() != ArenaState.INGAME) {
								if (powerup_task != null) {
									System.out.println("Cancelled powerup task.");
									powerup_task.cancel();
								}
							}
						}
						Bukkit.getLogger().info("Use the latest MinigamesLib version to get powerups.");
						failcount++;
						if (failcount > 2) {
							if (powerup_task != null) {
								System.out.println("Cancelled powerup task.");
								powerup_task.cancel();
							}
						}
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
		super.stop();
	}

}

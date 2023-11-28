package me.elb1to.ffa.game.listener;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class FfaListener implements Listener {

	private final FfaPlugin plugin;

	/*@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		event.setCancelled(!event.getPlayer().isOp());
	}*/

	@EventHandler
	public void onPlayerInteractSoup(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!player.isDead() && player.getItemInHand().getType() == Material.MUSHROOM_SOUP && player.getHealth() < 19.0) {
			final double newHealth = Math.min(player.getHealth() + 7.0, 20.0);
			player.setHealth(newHealth);
			player.getItemInHand().setType(Material.BOWL);
			player.updateInventory();
		}
	}

	/*@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile.getState() == UserProfile.State.SPECTATING) {
			ItemStack item = event.getItem();
			if (item != null) {
				*//*switch (item.getType()) {
					case COMPASS:
						new AlivePlayersMenu(plugin).openMenu(player);
						break;
					case REDSTONE:
						plugin.getMinigameManager().getCurrentMinigame().removeSpectator(player);
						break;
				}*//*
			}
			return;
		}

		FfaMap ffaMap = profile.getMap();
		if (ffaMap.denyDamage(player)) {
			event.setCancelled(true);
		}
	}*/

	/*@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (plugin.getMinigameManager().getCurrentMinigame() != null) {
			if (plugin.getUserManager().getByUuid(player.getUniqueId()).isInMinigame() && plugin.getMinigameManager().getCurrentMinigame() instanceof TerroristTagMinigame) {
				event.setCancelled(true);
			}
		}
	}*/

	@EventHandler
	public void onArrowHit(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			event.setCancelled(true);
			return;
		}

		Player victim = (Player) event.getEntity();
		if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player shooter = (Player) arrow.getShooter();
				/*if (victim.getName().equals(shooter.getName())) {
					event.setCancelled(true);
					return;
				}*/

				UserProfile profile = plugin.getUserProfileManager().getByUuid(victim.getUniqueId());
				FfaMap ffaMap = profile.getMap();
				if (ffaMap != null && ffaMap.getType() == FfaMap.Type.LOBBY && ffaMap.getCuboid().isIn(victim)) {
					event.setCancelled(true);
					return;
				}

				double health = Math.ceil(victim.getHealth() - event.getFinalDamage()) / 2.0D;
				if (health > 0.0D) {
					shooter.sendMessage(CC.color("&c" + victim.getName() + "&e is now at &c" + health + "&4❤&e."));
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		/*User user = plugin.getUserManager().getByUuid(player.getUniqueId());
		if (user.isSpectating()) {
			event.setCancelled(true);
			return;
		}*/

		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		FfaMap ffaMap = profile.getMap();
		if (ffaMap.denyDamage(player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof FishHook || event.getDamager() instanceof Arrow) && event.getEntity() instanceof Player) {
			Projectile projectile = (Projectile) event.getDamager();
			Player shooter = (Player) projectile.getShooter();
			if (shooter == null) {
				return;
			}

			/*User user = plugin.getUserManager().getByUuid(shooter.getUniqueId());
			if (user.isSpectating()) {
				event.setCancelled(true);
				return;
			}*/

			Player target = (Player) event.getEntity();
			if (target == null) {
				return;
			}

			handleMinigameDamage(event, target, shooter);
		}

		if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
			return;
		}

		Player victim = (Player) event.getEntity();
		Player attacker = (Player) event.getDamager();
		if (victim == null || attacker == null) {
			return;
		}

		handleMinigameDamage(event, victim, attacker);
	}

	private void handleMinigameDamage(EntityDamageByEntityEvent event, Player victim, Player attacker) {
		UserProfile victimData = plugin.getUserProfileManager().getByUuid(victim.getUniqueId());
		UserProfile damagerData = plugin.getUserProfileManager().getByUuid(attacker.getUniqueId());
		if (victimData == null || damagerData == null) {
			event.setCancelled(true);
			return;
		}
		if (victimData.getState() != damagerData.getState()) {
			event.setCancelled(true);
			return;
		}

		FfaMap region = damagerData.getMap();
		if (region.denyDamage(victim) || region.denyDamage(attacker)) {
			event.setCancelled(true);
		} else {
			attacker.sendMessage("Damaging " + victim.getName() + "!");
			victim.sendMessage("Damaged by " + attacker.getName() + "!");
		}
	}

	/*private void applyCooldown(Player attacker, Player victim, Cancellable event) {
		FfaMap region = plugin.getMapManager().get("spawn");
		if (region == null) {
			return;
		}

		if (region.denyDamage(victim) || region.denyDamage(attacker)) {
			event.setCancelled(true);
		} else {
			attacker.sendMessage("Damaging " + victim.getName() + "!");
			victim.sendMessage("Damaged by " + attacker.getName() + "!");
		}
	}*/

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);

		Player victim = event.getEntity().getPlayer();
		UserProfile victimUser = plugin.getUserProfileManager().getByUuid(victim.getUniqueId());
		event.getDrops().clear();

		if (victim.getKiller() != null && victim != victim.getKiller()) {
			Player killer = event.getEntity().getKiller();

			for (Entity entity : victim.getNearbyEntities(5, 5, 5)) {
				if (entity instanceof Player) {
					Player player = (Player) entity;
					if (player != killer && player != victim) {
						player.sendMessage(CC.color(plugin.getConfig().getString("messages.played_killed")
								.replace("<victim>", victim.getName())
								.replace("<killer>", killer.getName())
						));
					}
				}
			}

			/*victim.sendMessage(CC.color("&c&l+1 DEATH &4&l｜ &eYou were killed by &c" + killer.getName() + "&f."));
			killer.sendMessage(CC.color("&a&l+1 KILL &2&l｜ &eYou have killed &a" + victim.getName() + "&f."));*/

			UserProfile killerUser = plugin.getUserProfileManager().getByUuid(killer.getUniqueId());
			killerUser.setKills(killerUser.getKills() + 1);
			/*killerUser.setCurrentKillstreak(killerUser.getCurrentKillstreak() + 1);
			killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
			if (killerUser.getCurrentKillstreak() > killerUser.getHighestKillstreak()) {
				killerUser.setHighestKillstreak(killerUser.getCurrentKillstreak());
			}*/
		} else {
			victim.sendMessage(CC.color("&c&l+1 DEATH &4&l｜&eYou have died."));
		}

		victimUser.setDeaths(victimUser.getDeaths() + 1);
		// PlayerUtil.clearPlayer(victim);
	}
}

package me.elb1to.ffa.game.listener;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.user.ui.ffa.MapSelectionMenu;
import me.elb1to.ffa.util.PlayerUtil;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class FfaListener implements Listener {

	private final FfaPlugin plugin;

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile == null || profile.getState() != UserProfile.State.PLAYING || !plugin.getUserProfileManager().getBuilders().contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile == null || profile.getState() != UserProfile.State.PLAYING || !plugin.getUserProfileManager().getBuilders().contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getUserProfileManager().getBuilders().contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getUserProfileManager().getBuilders().contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile == null || profile.getState() != UserProfile.State.PLAYING || !plugin.getUserProfileManager().getBuilders().contains(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

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

	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile.getState() == UserProfile.State.SPAWN) {
			ItemStack item = event.getItem();
			if (item != null) {
				switch (item.getType()) {
					case COMPASS:
						new MapSelectionMenu(plugin).openMenu(player);
						break;
					case DIODE:
						player.sendMessage("Coming Soon™");
						break;
					case EMERALD:
						player.sendMessage("Coming Soon™");
						break;
					case BOOK:
						player.sendMessage("Coming Soon™");
						break;
					case PAINTING:
						player.sendMessage("Coming Soon™");
						break;
				}
			}
		}
	}

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
				UserProfile profile = plugin.getUserProfileManager().getByUuid(victim.getUniqueId());
				FfaMap ffaMap = profile.getMap();
				if (ffaMap != null && ffaMap.getType() == FfaMap.Type.LOBBY && ffaMap.getCuboid().isIn(victim)) {
					event.setCancelled(true);
					return;
				}

				double health = Math.ceil(victim.getHealth() - event.getFinalDamage()) / 2.0D;
				if (health > 0.0D && !victim.getName().equals(shooter.getName())) {
					shooter.sendMessage(CC.color("&c" + victim.getName() + "&e is now at &c" + health + "&4❤&e."));
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Player player = (Player) event.getEntity();
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile.getState() != UserProfile.State.PLAYING) {
			event.setCancelled(true);
			return;
		}

		FfaMap ffaMap = profile.getMap();
		if (ffaMap == null) {
			return;
		}
		if (ffaMap.denyDamageInSafeZone(player)) {
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

			Player target = (Player) event.getEntity();
			if (target == null) {
				return;
			}

			UserProfile profile = plugin.getUserProfileManager().getByUuid(target.getUniqueId());
			if (profile.getState() != UserProfile.State.PLAYING) {
				event.setCancelled(true);
				return;
			}

			handleDamage(event, target, shooter);
		}

		if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
			return;
		}

		Player victim = (Player) event.getEntity();
		Player attacker = (Player) event.getDamager();
		if (victim == null || attacker == null) {
			return;
		}

		handleDamage(event, victim, attacker);
	}

	private void handleDamage(EntityDamageByEntityEvent event, Player victim, Player attacker) {
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

		FfaMap region = victimData.getMap();
		if (region == null) {
			return;
		}
		if (region.denyDamageInSafeZone(victim) || region.denyDamageInSafeZone(attacker)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage(null);
		event.getDrops().clear();

		Player victim = event.getEntity().getPlayer();
		if (victim.getKiller() != null && victim != victim.getKiller()) {
			Player killer = event.getEntity().getKiller();
			UserProfile killerUser = plugin.getUserProfileManager().getByUuid(killer.getUniqueId());
			plugin.getFfaManager().updateKillstreak(killer, killerUser.getFfa());
			killerUser.setKills(killerUser.getKills() + 1);

			plugin.getFfaManager().broadcast(CC.color(plugin.getConfig().getString("messages.played_killed")
					.replace("<victim>", victim.getName())
					.replace("<killer>", killer.getName())
			));
		} else {
			victim.sendMessage(CC.color("&c" + victim.getName() + " has died."));
		}

		victim.spigot().respawn();
		PlayerUtil.clearPlayer(victim, false, null);
		plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
			UserProfile victimUser = plugin.getUserProfileManager().getByUuid(victim.getUniqueId());
			victim.teleport(victimUser.getMap().getSpawn().toBukkitLocation());
			victimUser.setDeaths(victimUser.getDeaths() + 1);

			FfaInstance ffa = victimUser.getFfa();
			ffa.getKit().equip(victim);
			ffa.getKills().put(victimUser.getUniqueId(), 0);
		}, 1L);
	}
}

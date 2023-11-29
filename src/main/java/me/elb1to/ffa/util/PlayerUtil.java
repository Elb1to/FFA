package me.elb1to.ffa.util;

import lombok.experimental.UtilityClass;
import me.elb1to.ffa.util.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@UtilityClass
public class PlayerUtil {

	private final ItemStack joinItem = new ItemBuilder(Material.COMPASS).name("&dJoin FFA &7(Right Click)").build();
	private final ItemStack settingsItem = new ItemBuilder(Material.DIODE).name("&dSettings &7(Right Click)").build();
	private final ItemStack leaderboardItem = new ItemBuilder(Material.EMERALD).name("&dLeaderboards &7(Right Click)").build();
	private final ItemStack editorItem = new ItemBuilder(Material.BOOK).name("&dEdit Kits &7(Right Click)").build();
	private final ItemStack spectateItem = new ItemBuilder(Material.PAINTING).name("&dSpectate FFA &7(Right Click)").build();

	public void clearPlayer(Player player, boolean toSpawn, Location location) {
		player.closeInventory();
		player.setFireTicks(0);
		player.setHealth(20.0D);
		player.setFoodLevel(20);
		player.setSaturation(12.8F);
		player.setFallDistance(0.0F);
		player.setLevel(0);
		player.setExp(0.0F);
		player.setWalkSpeed(0.2F);
		player.setFlySpeed(0.2F);
		player.setAllowFlight(false);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.spigot().setCollidesWithEntities(true);
		player.setGameMode(GameMode.SURVIVAL);
		player.setMaximumNoDamageTicks(20);
		player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
		((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);

		if (toSpawn && location != null) {
			player.teleport(location);
			player.getInventory().setItem(0, joinItem);
			player.getInventory().setItem(1, settingsItem);
			player.getInventory().setItem(4, leaderboardItem);
			player.getInventory().setItem(7, editorItem);
			player.getInventory().setItem(8, spectateItem);
		}

		player.updateInventory();
	}

	public void updateVisibility(Player player, Player target, boolean hide) {
		if (hide) {
			player.hidePlayer(target);
		} else {
			player.showPlayer(target);
		}
	}
}

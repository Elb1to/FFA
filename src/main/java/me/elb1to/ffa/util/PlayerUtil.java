package me.elb1to.ffa.util;

import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@UtilityClass
public class PlayerUtil {

	public void clearPlayer(Player player) {
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

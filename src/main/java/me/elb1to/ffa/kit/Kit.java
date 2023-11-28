package me.elb1to.ffa.kit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Kit {

	private final String name;

	private Material icon;
	private ItemStack[] armor;
	private ItemStack[] contents;

	public void equip(Player player) {
		player.getInventory().setArmorContents(armor);
		player.getInventory().setContents(contents);

		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			player.removePotionEffect(potionEffect.getType());
		}
	}
}

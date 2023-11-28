package me.elb1to.ffa.user.ui.ffa.button;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.ui.ffa.sub.KitSelectionMenu;
import me.elb1to.ffa.util.item.ItemBuilder;
import me.elb1to.ffa.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class MapSelectionButton extends Button {

	private final FfaMap ffaMap;
	private final FfaPlugin plugin;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.MAP)
				.name("&d" + ffaMap.getName())
				.lore(Arrays.asList(
						" ",
						"&eClick to select this map and continue to the kit selection menu."
				))
				.hideFlags()
				.build();
	}

	@Override
	public void onClick(Player player, int slot, ClickType clickType, int hotbarButton) {
		playSuccess(player);
		player.closeInventory();
		new KitSelectionMenu(plugin, ffaMap).openMenu(player);
	}
}

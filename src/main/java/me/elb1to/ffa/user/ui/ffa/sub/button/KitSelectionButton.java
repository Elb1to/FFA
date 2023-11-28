package me.elb1to.ffa.user.ui.ffa.sub.button;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.util.item.ItemBuilder;
import me.elb1to.ffa.util.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class KitSelectionButton extends Button {

	private final Kit kit;
	private final FfaMap map;
	private final FfaPlugin plugin;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(kit.getIcon())
				.name("&d" + kit.getName())
				.lore(Arrays.asList(
						"&e❙ Chosen Map: &d" + map.getName(),
						" ",
						"&eClick to select this kit and start playing!"
				))
				.build();
	}

	@Override
	public void onClick(Player player, int slot, ClickType clickType, int hotbarButton) {
		playSuccess(player);
		plugin.getFfaManager().addPlayer(
				player,
				plugin.getFfaManager().getByKitName(kit.getName()),
				map.getName()
		);
	}
}

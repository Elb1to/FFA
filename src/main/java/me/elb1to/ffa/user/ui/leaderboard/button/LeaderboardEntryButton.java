package me.elb1to.ffa.user.ui.leaderboard.button;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.leaderboard.data.LeaderboardType;
import me.elb1to.ffa.util.item.ItemBuilder;
import me.elb1to.ffa.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class LeaderboardEntryButton extends Button {

	private final Kit kit;
	private final List<String> lore;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(kit.getIcon())
				.name("&e&l" + kit.getName() + " ┃ Top 10")
				.lore(lore)
				.hideFlags()
				.build();
	}
}

package me.elb1to.ffa.user.ui.leaderboard.button;

import me.elb1to.ffa.util.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
public class LeaderboardEntryButton extends Button {

	@Override
	public ItemStack getButtonItem(Player player) {
		return null;
	}

	@Override
	public void onClick(Player player, int slot, ClickType clickType, int hotbarButton) {

	}
}

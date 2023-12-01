package me.elb1to.ffa.user.ui.leaderboard;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.leaderboard.data.LeaderboardEntry;
import me.elb1to.ffa.leaderboard.data.LeaderboardType;
import me.elb1to.ffa.user.ui.leaderboard.button.LeaderboardEntryButton;
import me.elb1to.ffa.util.item.ItemBuilder;
import me.elb1to.ffa.util.menu.Button;
import me.elb1to.ffa.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class LeaderboardMenu extends Menu {

	private final FfaPlugin plugin;

	@Override
	public String getTitle(Player player) {
		return "&e&lFFA Leaderboards";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		int slot = 10;
		for (Map.Entry<String, Kit> kitEntry : plugin.getKitManager().getKits().entrySet()) {
			List<LeaderboardEntry> players = plugin.getLeaderboardManager().getTopPlayers(LeaderboardType.TOTAL_KILLS, kitEntry.getKey());
			List<String> lore = new ArrayList<>();
			lore.add(" ");

			for (int i = 0; i < players.size(); i++) {
				LeaderboardEntry entry = players.get(i);
				lore.add("&e#" + (i + 1) + ": &d" + entry.getUsername() + " &e- " + entry.getKitValue());
			}

			buttons.put(slot, new LeaderboardEntryButton(kitEntry.getValue(), lore));
			slot++;
		}
		fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(7).build());

		return buttons;
	}

	@Override
	public int getSize() {
		return 9 * 3;
	}
}

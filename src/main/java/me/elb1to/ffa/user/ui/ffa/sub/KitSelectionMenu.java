package me.elb1to.ffa.user.ui.ffa.sub;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.ui.ffa.sub.button.KitSelectionButton;
import me.elb1to.ffa.util.menu.Button;
import me.elb1to.ffa.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class KitSelectionMenu extends Menu {

	private final FfaPlugin plugin;
	private final FfaMap map;

	@Override
	public String getTitle(Player player) {
		return "Choose a Kit";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		for (Kit kit : plugin.getKitManager().getKits().values()) {
			buttons.put((buttons.size() % 9), new KitSelectionButton(kit, map, plugin));
		}

		return buttons;
	}
}

package me.elb1to.ffa.user.ui.ffa;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.ui.ffa.button.MapSelectionButton;
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
public class MapSelectionMenu extends Menu {

	private final FfaPlugin plugin;

	@Override
	public String getTitle(Player player) {
		return "Choose a Map";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		for (FfaMap map : plugin.getMapManager().getMaps()) {
			if (map.getType() == FfaMap.Type.ARENA) {
				buttons.put((buttons.size() % 9), new MapSelectionButton(map, plugin));
			}
		}

		return buttons;
	}
}

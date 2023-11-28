package me.elb1to.ffa.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.kit.Kit;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@Getter
@RequiredArgsConstructor
public class FfaInstance {

	private final Kit kit;
	private final FfaPlugin plugin;

	private final List<UUID> players = new ArrayList<>();
	private final Map<UUID, Integer> kills = new HashMap<>();
	private final Map<Item, Long> droppedItems = new HashMap<>();

}

package me.elb1to.ffa.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.kit.Kit;
import org.bukkit.entity.Item;

import java.util.HashMap;
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

	private final Map<UUID, Integer> players = new HashMap<>();
	private final Map<Item, Long> droppedItems = new HashMap<>();

}

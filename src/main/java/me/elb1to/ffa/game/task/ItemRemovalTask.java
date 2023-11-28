package me.elb1to.ffa.game.task;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import org.bukkit.entity.Item;

import java.util.Iterator;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@AllArgsConstructor
public class ItemRemovalTask implements Runnable {

	private final FfaPlugin plugin;

	@Override
	public void run() {
		plugin.getFfaManager().getInstances().forEach((kitName, instance) -> {
			Iterator<Item> iterator = instance.getDroppedItems().keySet().iterator();
			while (iterator.hasNext()) {
				Item item = iterator.next();
				long time = instance.getDroppedItems().get(item);
				if (time + 10000 < System.currentTimeMillis()) {
					item.remove();
					iterator.remove();
				}
			}
		});
	}
}

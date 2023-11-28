package me.elb1to.ffa.map;

import lombok.Data;
import me.elb1to.ffa.util.world.Cuboid;
import me.elb1to.ffa.util.world.CustomLocation;
import org.bukkit.entity.Player;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@Data
public class FfaMap {

	private final String name;
	private final Type type;

	private CustomLocation spawn;
	private CustomLocation min;
	private CustomLocation max;

	private Cuboid cuboid;

	public enum Type {
		LOBBY, ARENA
	}

	public boolean has(Player player) {
		return this.cuboid.contains(player.getLocation());
	}

	public boolean denyDamage(Player player) {
		return this.type == Type.ARENA && has(player);
	}
}

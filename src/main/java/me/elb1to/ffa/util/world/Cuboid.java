package me.elb1to.ffa.util.world;

import lombok.Getter;
import me.elb1to.ffa.map.FfaMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Getter
public class Cuboid {

	private final int xMin;
	private final int xMax;

	private final int yMin;
	private final int yMax;

	private final int zMin;
	private final int zMax;

	private final World world;

	public Cuboid(Location point1, Location point2) {
		this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
		this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
		this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
		this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
		this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
		this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());

		this.world = point1.getWorld();
	}

	private boolean contains(World world, int x, int y, int z) {
		return world.getName().equals(this.world.getName()) && x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax;
	}

	public boolean contains(Location location) {
		return this.contains(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public boolean isIn(Player player) {
		return this.contains(player.getLocation());
	}
}

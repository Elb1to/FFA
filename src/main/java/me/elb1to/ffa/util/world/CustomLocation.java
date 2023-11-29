package me.elb1to.ffa.util.world;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.StringJoiner;

@Getter
@Setter
public class CustomLocation {

	private String world;

	private double x;
	private double y;
	private double z;

	private float yaw;
	private float pitch;

	public CustomLocation(double x, double y, double z) {
		this(x, y, z, 0.0F, 0.0F);
	}

	public CustomLocation(double x, double y, double z, float yaw, float pitch) {
		this("world", x, y, z, yaw, pitch);
	}

	public CustomLocation(String world, double x, double y, double z, float yaw, float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public static CustomLocation fromBukkitLocation(Location location) {
		return new CustomLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public static CustomLocation stringToLocation(String string) {
		String[] split = string.split(", ");

		double x = Double.parseDouble(split[0]);
		double y = Double.parseDouble(split[1]);
		double z = Double.parseDouble(split[2]);

		CustomLocation customLocation = new CustomLocation(x, y, z);
		if (split.length == 4) {
			customLocation.setWorld(split[3]);
		} else if (split.length >= 5) {
			customLocation.setYaw(Float.parseFloat(split[3]));
			customLocation.setPitch(Float.parseFloat(split[4]));
			if (split.length >= 6) {
				customLocation.setWorld(split[5]);
			}
		}

		return customLocation;
	}

	public static String locationToString(CustomLocation loc) {
		StringJoiner joiner = new StringJoiner(", ");
		joiner.add(Double.toString(Math.round(loc.getX()))).add(Double.toString(Math.round(loc.getY()))).add(Double.toString(Math.round(loc.getZ())));
		if (loc.getYaw() != 0.0f || loc.getPitch() != 0.0f) {
			joiner.add(Float.toString(loc.getYaw()));
			joiner.add(Float.toString(loc.getPitch()));
		}
		if (!loc.getWorld().equals("world")) {
			joiner.add(loc.getWorld());
		}

		return joiner.toString();
	}

	public Location toBukkitLocation() {
		return new Location(this.toBukkitWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
	}

	public String toString() {
		return locationToString(this);
	}

	public World toBukkitWorld() {
		if (this.world == null) {
			return Bukkit.getWorlds().get(0);
		} else {
			return Bukkit.getWorld(this.world);
		}
	}
}

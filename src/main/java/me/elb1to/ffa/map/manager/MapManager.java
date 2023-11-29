package me.elb1to.ffa.map.manager;

import lombok.Getter;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.util.world.Cuboid;
import me.elb1to.ffa.util.world.CustomLocation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
public class MapManager {

	private final FfaPlugin plugin;
	private final ConfigurationSection cfg;

	@Getter
	private final List<FfaMap> maps = new ArrayList<>();

	public MapManager(FfaPlugin plugin, ConfigurationSection cfg) {
		this.plugin = plugin;
		this.cfg = cfg;

		load();
	}

	private void load() {
		if (cfg != null) {
			cfg.getKeys(false).forEach(key -> {
				ConfigurationSection iMap = cfg.getConfigurationSection(key);
				if (iMap != null) {
					FfaMap ffaMap = new FfaMap(iMap.getName(), FfaMap.Type.valueOf(iMap.getString("type").toUpperCase()));
					ffaMap.setSpawn(CustomLocation.stringToLocation(iMap.getString("spawn")));
					ffaMap.setMin(CustomLocation.stringToLocation(iMap.getString("min")));
					ffaMap.setMax(CustomLocation.stringToLocation(iMap.getString("max")));
					ffaMap.setWorld(Bukkit.getWorld(ffaMap.getName()));

					maps.add(ffaMap);
					plugin.getLogger().info("Loaded map " + iMap.getName());
				}
			});
		}
	}

	public void save() {
		cfg.set("maps", null);

		maps.forEach(ffaMap -> {
			cfg.set(ffaMap.getName() + ".type", ffaMap.getType().toString().toLowerCase());
			cfg.set(ffaMap.getName() + ".spawn", ffaMap.getSpawn().toString());
			cfg.set(ffaMap.getName() + ".min", ffaMap.getMin().toString());
			cfg.set(ffaMap.getName() + ".max", ffaMap.getMax().toString());
		});

		plugin.saveConfig();
	}

	public void create(String name, FfaMap.Type type) {
		maps.add(new FfaMap(name, type));
	}

	public void delete(String name) {
		maps.remove(getByName(name));
	}

	public boolean exists(String name) {
		return getByName(name) != null;
	}

	public FfaMap getByName(String name) {
		for (FfaMap ffaMap : maps) {
			if (ffaMap.getName().equalsIgnoreCase(name)) {
				return ffaMap;
			}
		}

		return null;
	}

	public FfaMap getByType(FfaMap.Type type) {
		for (FfaMap ffaMap : maps) {
			if (ffaMap.getType() == type) {
				return ffaMap;
			}
		}

		return null;
	}

	public FfaMap getRandom() {
		List<FfaMap> maps = new ArrayList<>();
		this.maps.forEach(ffaMap -> {
			if (ffaMap.getType() != FfaMap.Type.LOBBY) {
				maps.add(ffaMap);
			}
		});

		return maps.get((int) (Math.random() * maps.size()));
	}
}

package me.elb1to.ffa.kit.manager;

import lombok.Getter;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.util.item.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
public class KitManager {

	private final FfaPlugin plugin;
	private final ConfigurationSection cfg;

	@Getter
	private final Map<String, Kit> kits = new HashMap<>();

	public KitManager(FfaPlugin plugin, ConfigurationSection cfg) {
		this.plugin = plugin;
		this.cfg = cfg;

		load();
	}

	private void load() {
		if (cfg != null) {
			cfg.getKeys(false).forEach(kitName -> {
				Material icon = Material.valueOf(cfg.getString(kitName + ".icon"));
				ItemStack[] armor = InventoryUtil.deserializeInventory(cfg.getString(kitName + ".armor"));
				ItemStack[] inventory = InventoryUtil.deserializeInventory(cfg.getString(kitName + ".items"));

				Kit kit = new Kit(kitName);
				kit.setIcon(icon);
				kit.setArmor(armor);
				kit.setContents(inventory);

				kits.put(kitName, kit);
				plugin.getLogger().info("Loaded kit " + kitName);
			});
		}
	}

	public void save() {
		cfg.set("kits", null);

		kits.forEach((name, kit) -> {
			cfg.set(name + ".icon", kit.getIcon().toString());
			cfg.set(name + ".armor", InventoryUtil.serializeInventory(kit.getArmor()));
			cfg.set(name + ".items", InventoryUtil.serializeInventory(kit.getContents()));
		});

		plugin.saveConfig();
	}

	public void create(String name) {
		kits.put(name, new Kit(name));
	}

	public void delete(String name) {
		kits.remove(name);
	}

	public Kit get(String name) {
		return kits.get(name);
	}

	public boolean exists(String name) {
		return kits.containsKey(name);
	}
}

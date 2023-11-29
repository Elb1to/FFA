package me.elb1to.ffa.map.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.util.chat.CC;
import me.elb1to.ffa.util.world.CustomLocation;
import org.bukkit.entity.Player;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@CommandAlias("map")
@CommandPermission("ffa.admin")
public class MapCommand extends BaseCommand {

	@Dependency
	private FfaPlugin plugin;

	@Default
	public void getHelp(Player player) {
		player.sendMessage(" ");
		player.sendMessage(CC.color("&b&lMap Commands &8- &7Information"));
		player.sendMessage(CC.color("&3 ● &b/map list &8- &7List all existing maps"));
		player.sendMessage(CC.color("&3 ● &b/map debug <name> &8- &7Debug a map"));
		player.sendMessage(CC.color("&3 ● &b/map create <name> &8- &7Create a new map"));
		player.sendMessage(CC.color("&3 ● &b/map delete <name> &8- &7Delete an existing map"));
		player.sendMessage(CC.color("&3 ● &b/map teleport <name> &8- &7Teleport to the map"));
		player.sendMessage(CC.color("&3 ● &b/map nofall <name> &8- &7Disable fall damage in the map"));
		player.sendMessage(CC.color("&3 ● &b/map setlocation <name> <spawn|min|max> &8- &7Set the location of the map"));
		player.sendMessage(CC.color("&3 ● &b/map slot <name> <number> &8- &7Set the slot of the map in the map selection menu"));
		player.sendMessage(" ");
	}

	@Subcommand("debug")
	@CommandCompletion("@maps")
	public void debug(Player player, FfaMap ffaMap) {
		if (!plugin.getMapManager().exists(ffaMap.getName())) {
			player.sendMessage(CC.color("&cA map with that name does not exist."));
			return;
		}

		player.sendMessage(" ");
		player.sendMessage(CC.color("&b&lMap Debug &8- &7Information"));
		player.sendMessage(CC.color("&3 ● &bName: &f" + ffaMap.getName()));
		player.sendMessage(CC.color("&3 ● &bType: &f" + ffaMap.getType().name()));
		player.sendMessage(CC.color("&3 ● &bSpawn: &f" + (ffaMap.getSpawn() == null ? "None" : ffaMap.getSpawn().toString())));
		player.sendMessage(CC.color("&3 ● &bMin: &f" + (ffaMap.getMin() == null ? "None" : ffaMap.getMin().toString())));
		player.sendMessage(CC.color("&3 ● &bMax: &f" + (ffaMap.getMax() == null ? "None" : ffaMap.getMax().toString())));
		player.sendMessage(" ");
	}

	@Subcommand("list")
	public void list(Player player) {
		player.sendMessage(" ");
		player.sendMessage(CC.color("&b&lMap List &8- &7Information"));
		for (FfaMap map : plugin.getMapManager().getMaps()) {
			player.sendMessage(CC.color("&3 ● &b" + map.getName()));
		}
		player.sendMessage(" ");
	}

	@Subcommand("create")
	public void create(Player player, String name, String type) {
		if (plugin.getMapManager().exists(name)) {
			player.sendMessage(CC.color("&cA map with that name already exists."));
			return;
		}
		if (!(type.equalsIgnoreCase("lobby") || type.equalsIgnoreCase("arena"))) {
			player.sendMessage(CC.color("&cThat is not a valid map type. Valid types: lobby, arena"));
			return;
		}

		plugin.getMapManager().create(name, FfaMap.Type.valueOf(type.toUpperCase()));
		player.sendMessage(CC.color("&aYou have created a new map with the name &b" + name + "&a."));
	}

	@Subcommand("delete")
	@CommandCompletion("@maps")
	public void delete(Player player, FfaMap ffaMap) {
		if (!plugin.getMapManager().exists(ffaMap.getName())) {
			player.sendMessage(CC.color("&cA ffaMap with that name does not exist."));
			return;
		}

		plugin.getMapManager().delete(ffaMap.getName());
		player.sendMessage(CC.color("&aYou have deleted the &b" + ffaMap.getName() + " &amap."));
	}

	@Subcommand("setlocation")
	@CommandCompletion("@maps")
	public void setLocation(Player player, FfaMap ffaMap, String location) {
		if (!plugin.getMapManager().exists(ffaMap.getName())) {
			player.sendMessage(CC.color("&cA ffaMap with that name does not exist."));
			return;
		}

		switch (location.toLowerCase()) {
			case "spawn":
				ffaMap.setSpawn(CustomLocation.fromBukkitLocation(player.getLocation()));
				break;
			case "min":
				ffaMap.setMin(CustomLocation.fromBukkitLocation(player.getLocation()));
				break;
			case "max":
				ffaMap.setMax(CustomLocation.fromBukkitLocation(player.getLocation()));
				break;
			default:
				player.sendMessage(CC.color("&cThat is not a valid location. Valid locations: spawn, min, max"));
		}

		player.sendMessage(CC.color("&aYou have set the &b" + location + " &alocation of the &b" + ffaMap.getName() + " &amap."));
	}

	@Subcommand("teleport")
	@CommandCompletion("@maps")
	public void sendToLocation(Player player, FfaMap ffaMap) {
		if (!plugin.getMapManager().exists(ffaMap.getName())) {
			player.sendMessage(CC.color("&cA map with that name does not exist."));
			return;
		}

		player.teleport(ffaMap.getSpawn().toBukkitLocation());
		player.sendMessage(CC.color("&aYou have been teleported to the &b" + ffaMap.getName() + " &amap."));
	}

	@Subcommand("nofall")
	@CommandCompletion("@maps")
	public void noFall(Player player, FfaMap ffaMap) {
		if (!plugin.getMapManager().exists(ffaMap.getName())) {
			player.sendMessage(CC.color("&cA map with that name does not exist."));
			return;
		}

		//ffaMap.setNoFall(!ffaMap.isNoFall());
		//player.sendMessage(CC.color("&aYou have " + (ffaMap.isNoFall() ? "enabled" : "disabled") + " no fall damage in the &b" + ffaMap.getName() + " &amap."));
	}

	@Subcommand("slot")
	@CommandCompletion("@maps")
	public void setSlot(Player player, FfaMap ffaMap, int slot) {
		if (!plugin.getMapManager().exists(ffaMap.getName())) {
			player.sendMessage(CC.color("&cA map with that name does not exist."));
			return;
		}

		//ffaMap.setSlot(slot);
		//player.sendMessage(CC.color("&aYou have set the slot of the &b" + ffaMap.getName() + " &amap to &b" + slot + "&a."));
	}
}

package me.elb1to.ffa.kit.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@CommandAlias("kit")
@CommandPermission("ffa.admin")
public class KitCommand extends BaseCommand {

	@Dependency
	private FfaPlugin plugin;

	@Default
	public void getHelp(Player player) {
		player.sendMessage(" ");
		player.sendMessage(CC.color("&b&lKit Commands &8- &7Information"));
		player.sendMessage(CC.color("&3 ● &b/kit list &8- &7List all existing kits"));
		player.sendMessage(CC.color("&3 ● &b/kit create <name> &8- &7Create a new kit"));
		player.sendMessage(CC.color("&3 ● &b/kit delete <name> &8- &7Delete an existing kit"));
		player.sendMessage(CC.color("&3 ● &b/kit icon <name> &8- &7Change the icon of the kit"));
		player.sendMessage(CC.color("&3 ● &b/kit update <name> &8- &7Update the kit inventory"));
		player.sendMessage(CC.color("&3 ● &b/kit give <name> <player> &8- &7Give a kit to a player"));
		player.sendMessage(" ");
	}

	@Subcommand("list")
	public void list(Player player) {
		player.sendMessage(" ");
		player.sendMessage(CC.color("&b&lKit List &8- &7Information"));
		for (Kit kit : plugin.getKitManager().getKits().values()) {
			player.sendMessage(CC.color("&3 ● &b" + kit.getName()));
		}
		player.sendMessage(" ");
	}

	@Subcommand("create")
	public void create(Player player, String name) {
		if (plugin.getKitManager().exists(name)) {
			player.sendMessage(CC.color("&cA kit with that name already exists."));
			return;
		}

		plugin.getKitManager().create(name);
		player.sendMessage(CC.color("&aYou have created a new kit with the name &b" + name + "&a."));
	}

	@Subcommand("delete")
	@CommandCompletion("@kits")
	public void delete(Player player, Kit kit) {
		if (!plugin.getKitManager().exists(kit.getName())) {
			player.sendMessage(CC.color("&cA kit with that name does not exist."));
			return;
		}

		plugin.getKitManager().delete(kit.getName());
		player.sendMessage(CC.color("&aYou have deleted the &b" + kit.getName() + " &akit."));
	}

	@Subcommand("icon")
	@CommandCompletion("@kits")
	public void updateIcon(Player player, Kit kit) {
		if (!plugin.getKitManager().exists(kit.getName())) {
			player.sendMessage(CC.color("&cA kit with that name does not exist."));
			return;
		}

		kit.setIcon(player.getItemInHand());
		player.sendMessage(CC.color("&aYou have updated the icon of the &b" + kit.getName() + " &akit."));
	}

	@Subcommand("update")
	@CommandCompletion("@kits")
	public void updateInventory(Player player, Kit kit) {
		if (!plugin.getKitManager().exists(kit.getName())) {
			player.sendMessage(CC.color("&cA kit with that name does not exist."));
			return;
		}

		player.updateInventory();
		kit.setArmor(player.getInventory().getArmorContents());
		kit.setContents(player.getInventory().getContents());
		player.sendMessage(CC.color("&aYou have updated the inventory of the &b" + kit.getName() + " &akit."));

		plugin.getKitManager().save();
	}

	@Subcommand("give")
	@CommandCompletion("@kits")
	public void getInventory(Player player, Kit kit, OnlinePlayer target) {
		if (!plugin.getKitManager().exists(kit.getName())) {
			player.sendMessage(CC.color("&cA kit with that name does not exist."));
			return;
		}

		kit.equip(target.getPlayer());
		player.sendMessage(CC.color("&aYou have given the &b" + kit.getName() + " &akit to &b" + target.getPlayer().getName() + "&a."));
	}
}

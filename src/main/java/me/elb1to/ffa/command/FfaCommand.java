package me.elb1to.ffa.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.user.ui.ffa.MapSelectionMenu;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@CommandAlias("ffa")
public class FfaCommand extends BaseCommand {

	@Dependency
	private FfaPlugin plugin;

	@Default
	public void getHelp(Player player) {
		new MapSelectionMenu(plugin).openMenu(player);

		player.sendMessage(" ");
		player.sendMessage(CC.color("&b&lFFA Commands &8- &7Information"));
		player.sendMessage(CC.color("&3 ● &b/ffa join <kit> &8- &7Join a FFA"));
		player.sendMessage(CC.color("&3 ● &b/ffa leave &8- &7Leave a FFA"));
		player.sendMessage(" ");
	}

	@Subcommand("join")
	@CommandCompletion("@kits")
	public void join(Player player, Kit kit) {
		if (kit == null) {
			player.sendMessage(CC.color("&cThat kit does not exist."));
			return;
		}
		if (plugin.getFfaManager().getByPlayer(player) != null || plugin.getUserProfileManager().getByUuid(player.getUniqueId()).getState() == UserProfile.State.PLAYING) {
			player.sendMessage(CC.color("&cYou are already in a FFA."));
			return;
		}

		FfaInstance instance = plugin.getFfaManager().getByKitName(kit.getName());
		if (instance == null) {
			player.sendMessage(CC.color("&cThere is no FFA with that kit."));
			return;
		}

		plugin.getFfaManager().addPlayer(player, instance, plugin.getMapManager().getRandom().getName());
	}

	@Subcommand("leave")
	public void leave(Player player) {
		FfaInstance instance = plugin.getFfaManager().getByPlayer(player);
		if (instance == null) {
			player.sendMessage(CC.color("&cYou are not in a FFA."));
			return;
		}

		plugin.getFfaManager().removePlayer(player, instance);
	}
}

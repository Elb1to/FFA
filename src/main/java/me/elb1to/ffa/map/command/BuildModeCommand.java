package me.elb1to.ffa.map.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Elb1to
 * @since 11/28/2023
 */
public class BuildModeCommand extends BaseCommand {

	@Dependency
	private FfaPlugin plugin;

	@CommandAlias("buildmode|build")
	@CommandPermission("ffa.admin")
	public void onExecute(Player player) {
		if (plugin.getUserProfileManager().getBuilders().contains(player.getUniqueId())) {
			plugin.getUserProfileManager().getBuilders().remove(player.getUniqueId());
		} else {
			plugin.getUserProfileManager().getBuilders().add(player.getUniqueId());
		}

		player.sendMessage(CC.color("&7You are now in &b" + (plugin.getUserProfileManager().getBuilders().contains(player.getUniqueId()) ? "build" : "play") + " &7mode."));
	}
}

package me.elb1to.ffa.user.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import me.elb1to.ffa.FfaPlugin;
import org.bukkit.entity.Player;

/**
 * @author Elb1to
 * @since 11/28/2023
 */
public class StatsCommand extends BaseCommand {

	@Dependency
	private FfaPlugin plugin;

	@CommandAlias("stats|statistics")
	public void onExecute(Player player, String targetName) {
		Player target = plugin.getServer().getPlayer(targetName);
		if (target == null) {
			player.sendMessage("§cThat player is not online.");
			return;
		}
	}
}

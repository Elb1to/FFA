package me.elb1to.ffa.user.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.user.UserProfile;
import org.bukkit.entity.Player;

/**
 * @author Elb1to
 * @since 11/27/2023
 */
public class UserProfileDebugCommand extends BaseCommand {

	@Dependency
	private FfaPlugin plugin;

	@CommandAlias("userdebug") @CommandPermission("ffa.admin")
	public void onCommand(Player player, OnlinePlayer target) {
		UserProfile profile = plugin.getUserProfileManager().getByUuid(target.getPlayer().getUniqueId());
		player.sendMessage("§aLoading user profile for §e" + target.getPlayer().getName() + "§a...");
		player.sendMessage("");
		player.sendMessage("§aName: §f" + target.getPlayer().getName());
		player.sendMessage("§aUUID: §f" + target.getPlayer().getUniqueId());
		player.sendMessage("§aState: §f" + profile.getState());
		player.sendMessage("§aKills: §f" + profile.getKills());
		player.sendMessage("§aDeaths: §f" + profile.getDeaths());
		player.sendMessage(profile.getMap().getName() == null ? "§aMap: §fNone" : "§aMap: §f" + profile.getMap().getName());
	}
}

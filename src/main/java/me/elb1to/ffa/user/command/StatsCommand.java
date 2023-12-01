package me.elb1to.ffa.user.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Syntax;
import com.google.common.collect.Lists;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Elb1to
 * @since 11/28/2023
 */
public class StatsCommand extends BaseCommand {

	private final List<String> format = Lists.newArrayList(
			"&b&l<player>'s Stats",
			" &bKills: &f<kills>",
			" &bDeaths: &f<deaths>",
			" &bKDR: &f<kdr>"
	);

	@Dependency
	private FfaPlugin plugin;

	@CommandAlias("stats|statistics")
	@CommandCompletion("@players")
	@Syntax("<player>")
	public void getPlayerStats(Player player, String name) {
		try {
			//OfflinePlayer target = plugin.getServer().getOfflinePlayer(name);
			UserProfile user = plugin.getMongoSrv().getProfile(name).get();
			if (user == null) {
				player.sendMessage(CC.color("&cNo data found for '" + name + "'"));
				return;
			}

			for (String message : format) {
				player.sendMessage(CC.color(message
						.replace("<player>", name)
						.replace("<kills>", String.valueOf(user.getKills()))
						.replace("<deaths>", String.valueOf(user.getDeaths()))
				));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

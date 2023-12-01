package me.elb1to.ffa.layout;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@AllArgsConstructor
public class ScoreboardLayout implements AssembleAdapter {

	private final FfaPlugin plugin;

	@Override
	public String getTitle(Player player) {
		return CC.color(plugin.getConfig().getString("scoreboard.title"));
	}

	@Override
	public List<String> getLines(Player player) {
		List<String> lines = new ArrayList<>();
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile == null) {
			return lines;
		}

		switch (profile.getState()) {
			case SPAWN:
				for (String line : plugin.getConfig().getStringList("scoreboard.lobby")) {
					String replace1 = line
							.replace("<max_players>", String.valueOf(Bukkit.getServer().getMaxPlayers()))
							.replace("<online_players>", String.valueOf(Bukkit.getOnlinePlayers().size()))
							.replace("<ingame_players>", String.valueOf(plugin.getFfaManager().getAllPlayers().size()))
							.replace("<kills>", String.valueOf(profile.getTotalKills()))
							.replace("<deaths>", String.valueOf(profile.getTotalDeaths()))
							.replace("<ping>", String.valueOf(((CraftPlayer) player).getHandle().ping));
					lines.add(replace1);
				}
				break;
			case PLAYING:
				FfaInstance ffaInstance = profile.getFfa();
				int kills = profile.getKills(ffaInstance.getKit().getName());
				int deaths = profile.getDeaths(ffaInstance.getKit().getName());

				for (String line : plugin.getConfig().getStringList("scoreboard.playing")) {
					String replace = line
							.replace("<players>", String.valueOf(profile.getFfa().getPlayers().size()))
							.replace("<kills>", String.valueOf(kills))
							.replace("<deaths>", String.valueOf(deaths))
							.replace("<killstreak>", String.valueOf(profile.getFfa().getPlayers().get(player.getUniqueId())))
							.replace("<kit>", String.valueOf(profile.getFfa().getKit().getName()))
							.replace("<ping>", String.valueOf(((CraftPlayer) player).getHandle().ping));
					lines.add(replace);
				}
				break;
		}

		return CC.color(lines);
	}
}

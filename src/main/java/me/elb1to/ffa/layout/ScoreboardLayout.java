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
		switch (profile.getState()) {
			case SPAWN:
				lines = plugin.getConfig().getStringList("scoreboard.lobby").stream()
						.map(line -> line
								.replace("<max_players>", String.valueOf(Bukkit.getServer().getMaxPlayers()))
								.replace("<online_players>", String.valueOf(Bukkit.getOnlinePlayers().size()))
								.replace("<ingame_players>", String.valueOf(plugin.getFfaManager().getAllPlayers().size()))
								.replace("<kills>", String.valueOf(profile.getKills()))
								.replace("<deaths>", String.valueOf(profile.getDeaths()))
								.replace("<ping>", String.valueOf(((CraftPlayer) player).getHandle().ping)))
						.collect(Collectors.toList());
				break;
			case PLAYING:
				lines = plugin.getConfig().getStringList("scoreboard.playing").stream()
						.map(line -> line
								.replace("<players>", String.valueOf(profile.getFfa().getPlayers().size()))
								.replace("<kills>", String.valueOf(profile.getKills()))
								.replace("<deaths>", String.valueOf(profile.getDeaths()))
								.replace("<killstreak>", String.valueOf(profile.getFfa().getPlayers().get(player.getUniqueId())))
								.replace("<kit>", String.valueOf(profile.getFfa().getKit().getName()))
								.replace("<ping>", String.valueOf(((CraftPlayer) player).getHandle().ping)))
						.collect(Collectors.toList());
				break;
		}

		return CC.color(lines);
	}
}

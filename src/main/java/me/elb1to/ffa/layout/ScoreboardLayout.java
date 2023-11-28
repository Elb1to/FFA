package me.elb1to.ffa.layout;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.util.chat.CC;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		FfaInstance instance = plugin.getFfaManager().getByPlayer(player);
		if (instance == null) { // If the player is not in a FFA, return an empty list.
			return null;
		}

		UserProfile.State state = profile.getState();

		return plugin.getConfig().getStringList("scoreboard.lines").stream()
				.map(line -> CC.color(line)
						.replace("<players>", String.valueOf(instance.getPlayers().size()))
						.replace("<kills>", String.valueOf(profile.getKills()))
						.replace("<deaths>", String.valueOf(profile.getDeaths()))
						.replace("<killstreak>", String.valueOf(instance.getKills().get(player.getUniqueId())))
						.replace("<kit>", String.valueOf(instance.getKit().getName()))
						.replace("<ping>", String.valueOf(((CraftPlayer) player).getHandle().ping))
				)
				.collect(Collectors.toList());
	}
}

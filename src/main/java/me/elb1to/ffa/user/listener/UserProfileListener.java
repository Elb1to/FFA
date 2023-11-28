package me.elb1to.ffa.user.listener;

import lombok.AllArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@AllArgsConstructor
public class UserProfileListener implements Listener {

	private final FfaPlugin plugin;

	@EventHandler
	public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		Player player = Bukkit.getServer().getPlayerExact(event.getName());
		if (player == null) {
			return;
		}
		if (player.isOnline()) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cA player with the same name is already connected!\n§cPlease try again later.");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			UserProfile user = plugin.getUserProfileManager().getOrCreate(player.getUniqueId());
			if (user != null) {
				plugin.getUserProfileManager().load(user);
				if (user.getName() == null || user.getName().isEmpty() || !user.getName().equals(player.getName())) {
					user.setName(player.getName());
				}
			}
		});

		PlayerUtil.clearPlayer(player);
		player.teleport(plugin.getMapManager().getByType(FfaMap.Type.LOBBY).getSpawn().toBukkitLocation());
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		removeLoadedUser(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent event) {
		removeLoadedUser(event.getPlayer());
	}

	private void removeLoadedUser(Player player) {
		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		if (profile != null) {
			plugin.getFfaManager().removePlayer(player, profile.getFfa());
			plugin.getUserProfileManager().unload(player.getUniqueId());
		}
	}
}

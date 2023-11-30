package me.elb1to.ffa.game.manager;

import lombok.Getter;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
public class FfaManager {

	private final FfaPlugin plugin;

	@Getter
	private final Map<String, FfaInstance> instances = new HashMap<>();

	public FfaManager(FfaPlugin plugin) {
		this.plugin = plugin;
		this.plugin.getKitManager().getKits().forEach((kitName, kit) -> instances.put(kit.getName(), new FfaInstance(kit, plugin)));
	}

	public void broadcast(String message) {
		for (FfaInstance ffaInstance : instances.values()) {
			ffaInstance.getPlayers().forEach((uuid, integer) -> {
				Player player = Bukkit.getPlayer(uuid);
				if (player == null) {
					return;
				}

				player.sendMessage(message);
			});
		}
	}

	public void brodcastWithSound(String message, Sound sound) {
		for (FfaInstance ffaInstance : instances.values()) {
			ffaInstance.getPlayers().forEach((uuid, integer) -> {
				Player player = Bukkit.getPlayer(uuid);
				if (player == null) {
					return;
				}

				player.sendMessage(message);
				player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
			});
		}
	}

	public void addPlayer(Player player, FfaInstance ffa, String mapName) {
		player.teleport(plugin.getMapManager().getByName(mapName).getSpawn().toBukkitLocation());
		player.setAllowFlight(false);
		player.setFlying(false);

		ffa.getPlayers().put(player.getUniqueId(), 0);
		ffa.getKit().equip(player);

		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		profile.setState(UserProfile.State.PLAYING);
		profile.setMap(plugin.getMapManager().getByName(mapName));
		profile.setFfa(ffa);

		// 1
		for (FfaInstance ffaInstance : instances.values()) {
			ffaInstance.getPlayers().forEach((uuid, integer) -> {

				//Player ffaPlayer = Bukkit.getPlayer(uuid);

				// issue here
				// description: if a player is already in an ffa instance, when someone joins a different ffa instance, only the player who joined after won't see the other player
				// while the player who joined before, will see the player who joined after
				// This is due to the ffa instance not having that player in the values
				Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
					if (ffaInstance.getPlayers().containsKey(onlinePlayer.getUniqueId())) {
						if (ffaInstance.equals(ffa)) {
							player.showPlayer(onlinePlayer);
						} else {
							player.hidePlayer(onlinePlayer);
						}
					}
				});


				/*if (ffaPlayer != null) {
					if (!ffaInstance.equals(ffa)) {
						player.hidePlayer(ffaPlayer);
					} else {
						player.showPlayer(ffaPlayer);
					}
				}*/
			});
		}
	}

	public void removePlayer(Player player, FfaInstance ffa) {
		PlayerUtil.clearPlayer(player, true, plugin.getMapManager().getByType(FfaMap.Type.LOBBY).getSpawn().toBukkitLocation());
		ffa.getPlayers().remove(player.getUniqueId());

		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		profile.setState(UserProfile.State.SPAWN);
		profile.setMap(null);
		profile.setFfa(null);

		for (FfaInstance ffaInstance : instances.values()) {
			ffaInstance.getPlayers().forEach((uuid, integer) -> {
				Player ffaPlayer = Bukkit.getPlayer(uuid);
				if (ffaPlayer != null) {
					if (ffaInstance.equals(ffa)) {
						player.hidePlayer(ffaPlayer);
					} else {
						player.showPlayer(ffaPlayer);
					}
				}
			});
		}
	}

	public void updateKillstreak(Player player, FfaInstance ffa) {
		ffa.getPlayers().put(player.getUniqueId(), ffa.getPlayers().get(player.getUniqueId()) + 1);
	}

	public FfaInstance getByKitName(String kitName) {
		return instances.get(kitName);
	}

	public FfaInstance getByPlayer(Player player) {
		return instances.values().stream().filter(ffaInstance -> ffaInstance.getPlayers().containsKey(player.getUniqueId())).findFirst().orElse(null);
	}

	public List<Player> getAllPlayers() {
		List<Player> list = new ArrayList<>();
		for (FfaInstance ffaInstance : instances.values()) {
			Map<UUID, Integer> uuidIntegerMap = ffaInstance.getPlayers();
			for (UUID uuid : uuidIntegerMap.keySet()) {
				Player player = Bukkit.getPlayer(uuid);
				list.add(player);
			}
		}

		return list;
	}
}

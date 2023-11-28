package me.elb1to.ffa.game.manager;

import lombok.Getter;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

	public void addPlayer(Player player, FfaInstance ffa, String mapName) {
		player.teleport(plugin.getMapManager().getByName(mapName).getSpawn().toBukkitLocation());
		player.setAllowFlight(false);
		player.setFlying(false);

		ffa.getPlayers().add(player.getUniqueId());
		ffa.getKills().put(player.getUniqueId(), 0);
		ffa.getKit().equip(player);

		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		profile.setState(UserProfile.State.PLAYING);
		profile.setMap(plugin.getMapManager().getByName(mapName));

		for (FfaInstance ffaInstance : instances.values()) {
			if (ffaInstance.getPlayers().contains(player.getUniqueId())) {
				continue;
			}

			for (UUID uuid : ffaInstance.getPlayers()) {
				Player ffaPlayer = Bukkit.getPlayer(uuid);

				// Hide player from all other players in different FFA Instances, show player to player in same FFA Instance
			}
		}
	}

	public void removePlayer(Player player, FfaInstance ffa) {
		for (FfaInstance ffaInstance : instances.values()) {
			if (ffaInstance.getPlayers().contains(player.getUniqueId())) {
				continue;
			}

			for (UUID uuid : ffaInstance.getPlayers()) {
				Player ffaPlayer = Bukkit.getPlayer(uuid);

				// Hide player from all other players in different FFA Instances, show player to players in spawn map type
			}
		}

		ffa.getPlayers().remove(player.getUniqueId());
		ffa.getKills().remove(player.getUniqueId());

		UserProfile profile = plugin.getUserProfileManager().getByUuid(player.getUniqueId());
		profile.setState(UserProfile.State.SPAWN);
		profile.setMap(null);

		PlayerUtil.clearPlayer(player);
		player.teleport(plugin.getMapManager().getByType(FfaMap.Type.LOBBY).getSpawn().toBukkitLocation());
	}

	public FfaInstance getByKitName(String kitName) {
		return instances.get(kitName);
	}

	public FfaInstance getByPlayer(Player player) {
		return instances.values().stream().filter(ffaInstance -> ffaInstance.getPlayers().contains(player.getUniqueId())).findFirst().orElse(null);
	}

	public List<UUID> getAllPlayers() {
		return instances.entrySet().stream().flatMap(entry -> entry.getValue().getPlayers().stream()).collect(Collectors.toList());
	}
}

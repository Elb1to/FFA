package me.elb1to.ffa.leaderboard.manager;

import lombok.RequiredArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.leaderboard.data.LeaderboardEntry;
import me.elb1to.ffa.leaderboard.data.LeaderboardType;
import me.elb1to.ffa.user.UserProfile;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@RequiredArgsConstructor
public class LeaderboardManager {

	private final FfaPlugin plugin;
	private final Set<LeaderboardEntry> entries = ConcurrentHashMap.newKeySet();

	public void updateLeaderboards() {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::initialize);
	}

	private void initialize() {
		entries.clear();

		plugin.getKitManager().getKits().forEach((kitName, kit) -> {
			for (LeaderboardType type : LeaderboardType.values()) {
				String leaderboardType = type.getName();
				try {
					for (Document document : plugin.getUserProfileManager().getPlayersSortedByStat(kitName, leaderboardType)) {
						UUID uuid = UUID.fromString(document.getString("uniqueId"));
						String name = document.getString("name");
						int value = 0;

						UserProfile user = plugin.getMongoSrv().getProfile(name).get();
						if (user == null) {
							continue;
						}

						if (!document.containsKey("stats")) {
							continue;
						}

						final Document statsDocument = (Document) document.get("stats");
						if (statsDocument.containsKey(kitName)) {
							Document kitDocument = (Document) statsDocument.get(kit.getName());
							if (kitDocument != null && kitDocument.containsKey(leaderboardType)) {
								value = kitDocument.getInteger(leaderboardType);
							}
						}

						entries.add(new LeaderboardEntry(uuid, name, leaderboardType, kitName, value));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public List<LeaderboardEntry> getTopPlayers(LeaderboardType leaderboardType, String kitName) {
		List<LeaderboardEntry> entriesList = new ArrayList<>(entries);
		List<LeaderboardEntry> filteredEntries = entriesList
				.stream()
				.filter(entry -> entry.getLeaderboardType().equalsIgnoreCase(leaderboardType.getName()))
				.filter(entry -> entry.getKitType().equalsIgnoreCase(kitName))
				.sorted(Comparator.comparingInt(LeaderboardEntry::getKitValue).reversed())
				.collect(Collectors.toList());

		return filteredEntries.stream().limit(10).collect(Collectors.toList());
	}
}

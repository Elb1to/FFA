package me.elb1to.ffa.user.manager;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Sorts;
import lombok.RequiredArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.user.UserProfile;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@RequiredArgsConstructor
public class UserProfileManager {

	private final FfaPlugin plugin;
	private final Set<UUID> builders = new HashSet<>();
	private final Map<UUID, UserProfile> users = new ConcurrentHashMap<>();

	public void load(UserProfile user) {
		CompletableFuture.supplyAsync(() -> {
			Document document = plugin.getMongoSrv().getUserProfiles().find(Filters.eq("uniqueId", user.getUniqueId().toString())).first();
			if (document != null) {
				user.setName(document.getString("name"));

				Document statsDocument = (Document) document.get("stats");
				for (String key : statsDocument.keySet()) {
					Document kitDocument = (Document) statsDocument.get(key);
					if (kitDocument == null) {
						continue;
					}

					if (kitDocument.containsKey("kills")) {
						user.getKills().put(key, kitDocument.getInteger("kills"));
					}
					if (kitDocument.containsKey("deaths")) {
						user.getDeaths().put(key, kitDocument.getInteger("deaths"));
					}
				}

				Document settingsDocument = (Document) document.get("settings");
				user.getSettings().setModernTablist(settingsDocument.getBoolean("modernTablist"));
				user.getSettings().setShowScoreboard(settingsDocument.getBoolean("showScoreboard"));
				user.getSettings().setKillAnnouncements(settingsDocument.getBoolean("killAnnouncements"));
				user.getSettings().setKillstreakAnnouncements(settingsDocument.getBoolean("killstreakAnnouncements"));
			}

			return user;
		});
	}

	public void save(UserProfile user) {
		CompletableFuture.supplyAsync(() -> {
			Document document = new Document();
			document.put("uniqueId", user.getUniqueId().toString());
			document.put("name", user.getName());

			Document statsDocument = new Document();
			user.getKills().forEach((kitName, amount) -> {
				Document kitDocument;
				if (statsDocument.containsKey(kitName)) {
					kitDocument = (Document) statsDocument.get(kitName);
				} else {
					kitDocument = new Document();
				}

				kitDocument.put("kills", amount);
				statsDocument.put(kitName, kitDocument);
			});
			user.getDeaths().forEach((kitName, amount) -> {
				Document kitDocument;
				if (statsDocument.containsKey(kitName)) {
					kitDocument = (Document) statsDocument.get(kitName);
				} else {
					kitDocument = new Document();
				}

				kitDocument.put("deaths", amount);
				statsDocument.put(kitName, kitDocument);
			});
			document.put("stats", statsDocument);

			Document settingsDocument = new Document();
			settingsDocument.put("modernTablist", user.getSettings().isModernTablist());
			settingsDocument.put("showScoreboard", user.getSettings().isShowScoreboard());
			settingsDocument.put("killAnnouncements", user.getSettings().isKillAnnouncements());
			settingsDocument.put("killstreakAnnouncements", user.getSettings().isKillstreakAnnouncements());
			document.put("settings", settingsDocument);

			plugin.getMongoSrv().getUserProfiles().replaceOne(Filters.eq("uniqueId", user.getUniqueId().toString()), document, new ReplaceOptions().upsert(true));
			return user;
		});
	}

	public void unload(UUID uniqueId) {
		save(getByUuid(uniqueId));
		users.remove(uniqueId);
	}

	public MongoCursor<Document> getPlayersSorted(String type) {
		Document sort = new Document();
		sort.put(type, -1);

		return plugin.getMongoSrv().getUserProfiles().find().sort(sort).limit(10).iterator();
	}

	public List<Document> getPlayersSortedByStat(String kitName, String type) {
		try {
			MongoCollection<Document> collection = plugin.getMongoSrv().getUserProfiles();
			MongoIterable<Document> documents = collection.find().sort(Sorts.descending("stats." + kitName + "." + type)).limit(10);
			List<Document> sortedPlayers = new ArrayList<>();
			for (Document playerDocument : documents) {
				if (playerDocument != null) {
					sortedPlayers.add(playerDocument);
				}
			}

			return sortedPlayers;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	public int getStats(UUID uuid, String kitName, String type) {
		try {
			Document document = plugin.getMongoSrv().getUserProfiles().find(Filters.eq("uniqueId", uuid.toString())).first();
			if (document != null) {
				Document stats = document.get("stats", Document.class);
				if (stats != null) {
					Document kitDocument = stats.get(kitName, Document.class);
					if (kitDocument != null) {
						Integer kills = kitDocument.getInteger(type);
						return kills != null ? kills : 0;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public UserProfile getOrCreate(UUID uuid) {
		return users.computeIfAbsent(uuid, UserProfile::new);
	}

	public UserProfile getByUuid(UUID uuid) {
		return users.getOrDefault(uuid, new UserProfile(uuid));
	}

	public Collection<UserProfile> getAllProfiles() {
		return users.values();
	}

	public Set<UUID> getBuilders() {
		return builders;
	}
}

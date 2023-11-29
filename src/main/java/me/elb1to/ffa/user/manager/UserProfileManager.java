package me.elb1to.ffa.user.manager;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.RequiredArgsConstructor;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.user.UserProfile;
import org.bson.Document;

import java.util.Collection;
import java.util.HashSet;
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
				user.setKills(document.getInteger("kills"));
				user.setDeaths(document.getInteger("deaths"));

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
			document.put("kills", user.getKills());
			document.put("deaths", user.getDeaths());

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

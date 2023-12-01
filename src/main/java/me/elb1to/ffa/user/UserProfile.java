package me.elb1to.ffa.user;

import lombok.Data;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.map.FfaMap;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@Data
public class UserProfile {

	private final UUID uniqueId;

	private boolean loaded;

	private String name;

	private Map<String, Integer> kills = new ConcurrentHashMap<>();
	private Map<String, Integer> deaths = new ConcurrentHashMap<>();

	private UserSettings settings = new UserSettings();

	private FfaMap map;
	private FfaInstance ffa;
	private State state = State.SPAWN;

	public UserProfile(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	public UserProfile(UUID uniqueId, String name, Map<String, Integer> kills, Map<String, Integer> deaths) {
		this.uniqueId = uniqueId;
		this.name = name;
		this.kills = kills;
		this.deaths = deaths;
	}

	public int getKills(String kitName) {
		return this.kills.getOrDefault(kitName, 0);
	}

	public int getDeaths(String kitName) {
		return this.deaths.getOrDefault(kitName, 0);
	}

	public int getTotalKills() {
		return this.kills.values().stream().mapToInt(Integer::intValue).sum();
	}

	public int getTotalDeaths() {
		return this.deaths.values().stream().mapToInt(Integer::intValue).sum();
	}

	public void increaseKills(String kitName) {
		this.kills.put(kitName, this.kills.getOrDefault(kitName, 0) + 1);
	}

	public void increaseDeaths(String kitName) {
		this.deaths.put(kitName, this.deaths.getOrDefault(kitName, 0) + 1);
	}

	public enum State {
		SPAWN, PLAYING, SPECTATING
	}
}

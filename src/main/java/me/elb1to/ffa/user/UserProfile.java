package me.elb1to.ffa.user;

import lombok.Data;
import me.elb1to.ffa.game.FfaInstance;
import me.elb1to.ffa.map.FfaMap;

import java.util.UUID;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@Data
public class UserProfile {

	private final UUID uniqueId;

	private String name;

	private int kills;
	private int deaths;

	private UserSettings settings = new UserSettings();

	private FfaMap map;
	private FfaInstance ffa;
	private State state = State.SPAWN;

	public enum State {
		SPAWN, PLAYING, SPECTATING
	}
}

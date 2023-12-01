package me.elb1to.ffa.leaderboard.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
@Data
@AllArgsConstructor
public class LeaderboardEntry {

	private final UUID uuid;
	private final String username;
	private final String leaderboardType;

	private String kitType;
	private int kitValue;
}

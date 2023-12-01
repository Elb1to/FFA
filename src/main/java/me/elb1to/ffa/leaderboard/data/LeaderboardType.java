package me.elb1to.ffa.leaderboard.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Elb1to
 * @since 11/29/2023
 */
@Getter
@RequiredArgsConstructor
public enum LeaderboardType {

	TOTAL_DEATHS("deaths", "Total Deaths"),
	TOTAL_KILLS("kills", "Total Kills");

	private final String name;
	private final String niceName;
}

package me.elb1to.ffa.user;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@Getter
@Setter
public class UserSettings {

	private boolean modernTablist = true;
	private boolean showScoreboard = true;
	private boolean killAnnouncements = true;
	private boolean killstreakAnnouncements = true;

}

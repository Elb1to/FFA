package me.elb1to.ffa.util.chat;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elb1to
 * @since 3/12/2023
 * © 2023 HungerGames from FrozedClub
 */
@UtilityClass
public class CC {

	public final String CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------";

	public String color(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public List<String> color(List<String> lines) {
		List<String> strings = new ArrayList<>();
		for (String line : lines) {
			strings.add(ChatColor.translateAlternateColorCodes('&', color(line)));
		}

		return strings;
	}

	public List<String> color(String[] lines) {
		List<String> strings = new ArrayList<>();
		for (String line : lines) {
			if (line != null) {
				strings.add(ChatColor.translateAlternateColorCodes('&', color(line)));
			}
		}

		return strings;
	}
}

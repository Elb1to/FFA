package me.elb1to.ffa.command.manager;

import co.aikar.commands.PaperCommandManager;
import me.elb1to.ffa.FfaPlugin;
import me.elb1to.ffa.command.FfaCommand;
import me.elb1to.ffa.kit.Kit;
import me.elb1to.ffa.kit.command.KitCommand;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.map.command.BuildModeCommand;
import me.elb1to.ffa.map.command.MapCommand;
import me.elb1to.ffa.user.command.SettingsCommand;
import me.elb1to.ffa.user.command.StatsCommand;
import me.elb1to.ffa.user.command.UserProfileDebugCommand;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Elb1to
 * @since 11/24/2023
 */
public class CommandManager {

	private final FfaPlugin plugin;
	private final PaperCommandManager manager;

	public CommandManager(FfaPlugin plugin) {
		this.plugin = plugin;
		this.manager = new PaperCommandManager(plugin);

		load();
	}

	private void load() {
		manager.getCommandCompletions().registerCompletion("kits", context -> new ArrayList<>(plugin.getKitManager().getKits().values())
				.stream()
				.map(Kit::getName)
				.collect(Collectors.toList())
		);

		manager.getCommandCompletions().registerCompletion("maps", context -> plugin.getMapManager().getMaps()
				.stream()
				.map(FfaMap::getName)
				.collect(Collectors.toList())
		);

		manager.getCommandContexts().registerContext(Kit.class, context -> plugin.getKitManager().get(context.popFirstArg()));
		manager.getCommandContexts().registerContext(FfaMap.class, context -> plugin.getMapManager().getByName(context.popFirstArg()));

		manager.registerCommand(new FfaCommand());
		manager.registerCommand(new KitCommand());
		manager.registerCommand(new MapCommand());
		//manager.registerCommand(new StatsCommand()); // Unfinished command
		//manager.registerCommand(new SettingsCommand()); // Not working currently
		manager.registerCommand(new BuildModeCommand());
		manager.registerCommand(new UserProfileDebugCommand());
	}
}

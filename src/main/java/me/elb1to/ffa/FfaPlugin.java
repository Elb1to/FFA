package me.elb1to.ffa;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import me.elb1to.ffa.command.manager.CommandManager;
import me.elb1to.ffa.database.MongoSrv;
import me.elb1to.ffa.game.listener.FfaListener;
import me.elb1to.ffa.game.manager.FfaManager;
import me.elb1to.ffa.game.task.ItemRemovalTask;
import me.elb1to.ffa.kit.manager.KitManager;
import me.elb1to.ffa.layout.ScoreboardLayout;
import me.elb1to.ffa.leaderboard.manager.LeaderboardManager;
import me.elb1to.ffa.map.FfaMap;
import me.elb1to.ffa.map.manager.MapManager;
import me.elb1to.ffa.user.UserProfile;
import me.elb1to.ffa.user.listener.UserProfileListener;
import me.elb1to.ffa.user.manager.UserProfileManager;
import me.elb1to.ffa.util.menu.listener.ButtonListener;
import me.elb1to.ffa.util.world.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
@Getter
public class FfaPlugin extends JavaPlugin {

	private MongoSrv mongoSrv;

	private MapManager mapManager;
	private KitManager kitManager;
	private FfaManager ffaManager;

	private UserProfileManager userProfileManager;
	private LeaderboardManager leaderboardManager;

	private CommandManager commandManager;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		mongoSrv = new MongoSrv(this, getConfig().getConfigurationSection("mongo"));

		mapManager = new MapManager(this, getConfig().getConfigurationSection("maps"));
		kitManager = new KitManager(this, getConfig().getConfigurationSection("kits"));
		ffaManager = new FfaManager(this);

		userProfileManager = new UserProfileManager(this);
		leaderboardManager = new LeaderboardManager(this);
		commandManager = new CommandManager(this);

		getServer().getPluginManager().registerEvents(new FfaListener(this), this);
		getServer().getPluginManager().registerEvents(new ButtonListener(this), this);
		getServer().getPluginManager().registerEvents(new UserProfileListener(this), this);

		getServer().getScheduler().runTaskLater(this, () -> {
			for (FfaMap ffaMap : mapManager.getMaps()) {
				ffaMap.setWorld(Bukkit.getWorld(ffaMap.getName()));
				Cuboid cuboid = new Cuboid(ffaMap.getMin().toBukkitLocation(), ffaMap.getMax().toBukkitLocation());
				ffaMap.setCuboid(cuboid);
			}

			leaderboardManager.updateLeaderboards();
		}, 100L);

		getServer().getScheduler().runTaskTimerAsynchronously(this, new ItemRemovalTask(this), 1L, 1L);

		Assemble assemble = new Assemble(this, new ScoreboardLayout(this));
		assemble.setAssembleStyle(AssembleStyle.CUSTOM);
		assemble.setTicks(20);
	}

	@Override
	public void onDisable() {
		kitManager.save();
		mapManager.save();

		for (UserProfile userProfile : userProfileManager.getAllProfiles()) {
			userProfileManager.save(userProfile);
		}

		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (!(entity.getType() == EntityType.PAINTING || entity.getType() == EntityType.ITEM_FRAME)) {
					entity.remove();
				}
			}
		}

		mongoSrv.disconnect();
	}
}

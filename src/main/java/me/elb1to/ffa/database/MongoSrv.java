package me.elb1to.ffa.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.elb1to.ffa.FfaPlugin;
import org.bson.Document;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * @author Elb1to
 * @since 11/22/2023
 */
public class MongoSrv {

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> userProfiles;

	public MongoSrv(FfaPlugin plugin, ConfigurationSection cfg) {
		CompletableFuture.runAsync(() -> {
			try {
				plugin.getLogger().info("Connecting to MongoDB...");

				String host = cfg.getString("host");
				int port = cfg.getInt("port");
				if (cfg.getBoolean("auth.enabled")) {
					mongoClient = new MongoClient(new ServerAddress(host, port), Collections.singletonList(MongoCredential.createCredential(
							cfg.getString("auth.user"),
							cfg.getString("auth.auth-db"),
							cfg.getString("auth.pass").toCharArray())
					));
				} else {
					mongoClient = new MongoClient(new ServerAddress(host, port));
				}
				mongoDatabase = mongoClient.getDatabase(cfg.getString("database"));
				plugin.getLogger().info("Successfully connected to MongoDB.");
			} catch (Exception e) {
				plugin.getServer().getPluginManager().disablePlugin(plugin);
				plugin.getLogger().info("Disabling FFA because an error occurred while trying to connect to MongoDB.");
			}

			userProfiles = mongoDatabase.getCollection("userProfiles");
		});
	}

	public void disconnect() {
		CompletableFuture.runAsync(() -> mongoClient.close());
	}

	public MongoCollection<Document> getUserProfiles() {
		return userProfiles;
	}
}

package net.crackpixel.winstreaks.managers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.crackpixel.winstreaks.StrikeWinstreaks;
import org.bson.Document;

/**
 * This Project is property of kayalust © 2022
 * Redistribution of this Project is not allowed
 *
 * @author kayalust
 * 3/6/2022 / 3:14 PM
 * StrikeWinstreaks / net.crackpixel.winstreaks.database
 */

@Getter
@RequiredArgsConstructor
public class MongoManager {

    private final StrikeWinstreaks plugin;

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> players;

    public void init() {
        this.client = MongoClients.create(plugin.getConfig().getString("mongo.uri"));
        this.database = client.getDatabase(plugin.getConfig().getString("mongo.database"));

        this.players = database.getCollection("players");
    }

    public void shutdown() {
        try {
            Thread.sleep(50L); // This stops the main thread for a bit, stop crying about it
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        this.client.close();
    }
}
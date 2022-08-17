package net.crackpixel.winstreaks.database.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import lombok.RequiredArgsConstructor;
import net.crackpixel.winstreaks.StrikeWinstreaks;
import net.crackpixel.winstreaks.database.DatabaseHandler;
import net.crackpixel.winstreaks.models.KitData;
import net.crackpixel.winstreaks.models.PlayerData;
import org.bson.Document;

@RequiredArgsConstructor
public class MongoDatabaseHandler implements DatabaseHandler {

    private final StrikeWinstreaks plugin;

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> players;

    public void connect() {
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

    @Override
    public void savePlayerData(PlayerData playerData) {
        this.players.replaceOne(
                Filters.eq("_id", playerData.getUuid().toString()),
                playerData.toBson(),
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public void loadPlayerData(PlayerData data) {
        StrikePracticeAPI api = StrikePractice.getAPI();

        Document document = this.players.find(Filters.eq("_id", data.getUuid().toString())).first();

        if (document == null) {
            plugin.getPlayerDataManager().savePlayerData(data);
            plugin.getPlayerDataManager().getPlayerData().putIfAbsent(data.getUuid(), data);
            return;
        }

        data.setWinstreak(document.getInteger("overallWinstreak", 0));
        data.setBestWinstreak(document.getInteger("overallBestWinstreak", 0));

        Document dataDocument = (Document) document.get("kitData");

        for (String key : dataDocument.keySet()) {
            Document kitDocument = (Document) dataDocument.get(key);

            KitData kitData = new KitData();
            kitData.setWinstreak(kitDocument.getInteger("winstreak", 0));
            kitData.setBestWinstreak(kitDocument.getInteger("bestWinstreak", 0));

            data.getKitData().put(api.getKit(key), kitData);
        }

        plugin.getPlayerDataManager().getPlayerData().putIfAbsent(data.getUuid(), data);
    }
}

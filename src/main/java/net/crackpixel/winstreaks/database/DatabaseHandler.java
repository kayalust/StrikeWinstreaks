package net.crackpixel.winstreaks.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.crackpixel.winstreaks.StrikeWinstreaks;
import net.crackpixel.winstreaks.models.PlayerData;
import org.bson.Document;

/**
 * I WAS GONNA ADD SOMETHING HERE, BUT THEN i dislike mysql
 */

public interface DatabaseHandler {

    void connect();

    void shutdown();

    void savePlayerData(PlayerData playerData);

    void loadPlayerData(PlayerData playerData);
}
package net.crackpixel.winstreaks.managers;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.battlekit.BattleKit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.crackpixel.winstreaks.StrikeWinstreaks;
import net.crackpixel.winstreaks.data.KitData;
import net.crackpixel.winstreaks.listener.PlayerListener;
import net.crackpixel.winstreaks.data.PlayerData;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

/**
 * This Project is property of kayalust © 2022
 * Redistribution of this Project is not allowed
 *
 * @author kayalust
 * 3/6/2022 / 3:10 PM
 * StrikeWinstreaks / net.crackpixel.winstreaks.data
 */

@Getter
@RequiredArgsConstructor
public class DataManager {

    private final StrikeWinstreaks plugin;

    private final Map<UUID, PlayerData> playerData = new ConcurrentHashMap<>();

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), plugin);
    }

    @SneakyThrows
    public void savePlayerData(PlayerData data) {
        ForkJoinPool.commonPool().execute(() -> plugin.getMongoManager().getPlayers().replaceOne(Filters.eq("_id", data.getUuid().toString()), data.toBson(), new ReplaceOptions().upsert(true)));
    }

    @SneakyThrows
    public void loadPlayerData(PlayerData data) {
        StrikePracticeAPI api = StrikePractice.getAPI();

        ForkJoinPool.commonPool().execute(() -> {
            Document document = plugin.getMongoManager().getPlayers().find(Filters.eq("_id", data.getUuid().toString())).first();

            if (document == null) {
                this.savePlayerData(data);
                playerData.putIfAbsent(data.getUuid(), data);
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

            playerData.putIfAbsent(data.getUuid(), data);
        });
    }

    public PlayerData getByPlayer(Player player) {
        return playerData.get(player.getUniqueId());
    }
}

package net.crackpixel.winstreaks.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.crackpixel.winstreaks.StrikeWinstreaks;
import net.crackpixel.winstreaks.listener.PlayerListener;
import net.crackpixel.winstreaks.models.PlayerData;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
public class PlayerDataManager {

    private final StrikeWinstreaks plugin;

    private final Map<UUID, PlayerData> playerData = new ConcurrentHashMap<>();

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), plugin);
    }

    public void savePlayerData(PlayerData data) {
        plugin.getDatabaseHandler().savePlayerData(data);
    }

    public void loadPlayerData(PlayerData data) {
        plugin.getDatabaseHandler().loadPlayerData(data);
    }

    public PlayerData getDataByUUID(UUID uuid) {
        return playerData.get(uuid);
    }
}

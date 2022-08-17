package net.crackpixel.winstreaks;

import lombok.Getter;
import net.crackpixel.winstreaks.database.DatabaseHandler;
import net.crackpixel.winstreaks.database.impl.MongoDatabaseHandler;
import net.crackpixel.winstreaks.managers.PlayerDataManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class StrikeWinstreaks extends JavaPlugin {

    @Getter
    private static StrikeWinstreaks instance;

    private PlayerDataManager playerDataManager;
    private DatabaseHandler databaseHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.databaseHandler = new MongoDatabaseHandler(this);
        this.databaseHandler.connect();

        this.playerDataManager = new PlayerDataManager(this);
        this.playerDataManager.init();
    }

    @Override
    public void onDisable() {
        playerDataManager.getPlayerData().values().forEach(playerDataManager::savePlayerData);
        databaseHandler.shutdown();

        instance = null;
    }
}

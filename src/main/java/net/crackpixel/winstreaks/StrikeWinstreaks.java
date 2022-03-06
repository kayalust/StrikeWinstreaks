package net.crackpixel.winstreaks;

import lombok.Getter;
import net.crackpixel.winstreaks.managers.MongoManager;
import net.crackpixel.winstreaks.managers.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class StrikeWinstreaks extends JavaPlugin {

    @Getter
    private static StrikeWinstreaks instance;

    private DataManager dataManager;
    private MongoManager mongoManager;

    @Override
    public void onEnable() {
        instance = this;

        mongoManager = new MongoManager(this);
        mongoManager.init();

        dataManager = new DataManager(this);
        dataManager.init();
    }

    @Override
    public void onDisable() {
        dataManager.getPlayerData().values().forEach(dataManager::savePlayerData);
        mongoManager.shutdown();

        instance = null;
    }
}

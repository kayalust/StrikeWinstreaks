package net.crackpixel.winstreaks.listener;

import ga.strikepractice.events.DuelEndEvent;
import lombok.RequiredArgsConstructor;
import net.crackpixel.winstreaks.StrikeWinstreaks;
import net.crackpixel.winstreaks.managers.PlayerDataManager;
import net.crackpixel.winstreaks.models.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.ForkJoinPool;

/**
 * This Project is property of kayalust © 2022
 * Redistribution of this Project is not allowed
 *
 * @author kayalust
 * 3/6/2022 / 3:12 PM
 * StrikeWinstreaks / net.crackpixel.winstreaks.listener
 */

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final StrikeWinstreaks plugin;

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        PlayerData data = new PlayerData(event.getUniqueId());
        plugin.getPlayerDataManager().loadPlayerData(data);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData().get(event.getPlayer().getUniqueId());
        ForkJoinPool.commonPool().execute(() -> plugin.getPlayerDataManager().savePlayerData(data));
    }

    @EventHandler
    public void onEnd(DuelEndEvent event) {
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();

        PlayerData winnerData = playerDataManager.getPlayerData().get(event.getWinner().getUniqueId());
        PlayerData loserData = playerDataManager.getPlayerData().get(event.getLoser().getUniqueId());

        if (winnerData.getWinstreak() >= winnerData.getBestWinstreak()) winnerData.setBestWinstreak(winnerData.getWinstreak());
        winnerData.setWinstreak(winnerData.getWinstreak() + 1);
        loserData.setWinstreak(0);

        playerDataManager.savePlayerData(winnerData);
        playerDataManager.savePlayerData(loserData);
    }
}

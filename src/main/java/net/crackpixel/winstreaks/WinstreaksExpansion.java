package net.crackpixel.winstreaks;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class WinstreaksExpansion extends PlaceholderExpansion {

    private StrikeWinstreaks plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "sws";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        String args = params.toLowerCase();

        if (args.equals("winstreak")) {
            return String.valueOf(plugin.getPlayerDataManager().getDataByUUID(player.getUniqueId()).getWinstreak());
        } else if (args.equals("bestwinstreak")) {
            return String.valueOf(plugin.getPlayerDataManager().getDataByUUID(player.getUniqueId()).getBestWinstreak());
        }

        return "Invalid request";
    }
}

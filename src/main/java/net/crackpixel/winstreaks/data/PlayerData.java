package net.crackpixel.winstreaks.data;

import ga.strikepractice.battlekit.BattleKit;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This Project is property of kayalust © 2022
 * Redistribution of this Project is not allowed
 *
 * @author kayalust
 * 3/6/2022 / 3:09 PM
 * StrikeWinstreaks / net.crackpixel.winstreaks.stats
 */

@Getter @Setter
public class PlayerData {
    private UUID uuid;
    private int winstreak, bestWinstreak;

    private final Map<BattleKit, KitData> kitData = new HashMap<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public Document toBson() {
        Document document = new Document();

        for (Map.Entry<BattleKit, KitData> entry : kitData.entrySet()) {
            Document dataDocument = new Document();

            dataDocument.put("winstreak", entry.getValue().getWinstreak());
            dataDocument.put("bestWinstreak", entry.getValue().getBestWinstreak());

            document.put(entry.getKey().getName(), dataDocument);
        }

        return new Document().append("_id", uuid.toString())
                .append("overallWinstreak", winstreak)
                .append("overallBestWinstreak", bestWinstreak)
                .append("kitData", document);
    }
}
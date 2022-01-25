package core.viewers;

import java.util.List;

import core.battle.HasBattleView;
import core.battlefield.Battlefield;
import core.battlefield.BattlefieldCreature;
import core.battlefield.ObjectStatus;
import utils.Constants;

public class BattlefieldViewer extends Viewer {

    private static final int ROW_SIZE = Constants.BATTLEFIELD_VIEW_SIZE.value;
    private static final int CREATURE_VIEW_LENGTH = Constants.BATTLE_VIEW_LENGTH.value + 2;
    private static final int CREATURE_VIEW_HEIGHT = Constants.BATTLE_VIEW_HEIGHT.value;
    private static final int CREATURE_OFFSET = Constants.MAP_OFFSET.value;

    public static String getBattleFieldView(Battlefield battlefield) {
        StringBuilder view = new StringBuilder();

        view.append(lineWithAngles());

        List<BattlefieldCreature> creatures;

        creatures = battlefield.getSecondSide().getCreatures(ObjectStatus.ALIVE);
        view.append(getCreaturesRow(creatures));

        view.append(line("=", "|", "|", true));

        creatures = battlefield.getFirstSide().getCreatures(ObjectStatus.ALIVE);
        view.append(getCreaturesRow(creatures));

        view.append(lineWithAngles());
        return view.toString();
    }

    private static StringBuilder getViewForCreatures(List<? extends HasBattleView> creaturesOnPosition) {
        int creaturesInOneRow = (ROW_SIZE - CREATURE_OFFSET) / (CREATURE_VIEW_LENGTH + CREATURE_OFFSET);
        int printedCreaturesInPosition = 0;
        StringBuilder view = new StringBuilder();

        while (printedCreaturesInPosition < creaturesOnPosition.size()) {
            int creaturesCounter = 0;
            for (int i = 0; i < CREATURE_VIEW_HEIGHT; i++) {
                StringBuilder row = new StringBuilder();
                int printedCreaturesInRow = 0;
                row.append("|");
                while (printedCreaturesInRow < creaturesInOneRow) {
                    row.append(" ".repeat(CREATURE_OFFSET));
                    if (creaturesOnPosition.size() > printedCreaturesInPosition + printedCreaturesInRow) {
                        row.append(creaturesOnPosition.get(printedCreaturesInPosition + printedCreaturesInRow).getBattleView().get(i));
                        creaturesCounter += 1;
                    } else {
                        row.append(" ".repeat(CREATURE_VIEW_LENGTH));
                    }
                    printedCreaturesInRow += 1;
                }
                row.append(" ".repeat(ROW_SIZE - (row.length() + 1))).append("|\n");
                view.append(row);
            }
            printedCreaturesInPosition += creaturesCounter / CREATURE_VIEW_HEIGHT;
            view.append(emptyLine());
        }

        return view;
    }

    public static StringBuilder getCreaturesRow(List<? extends HasBattleView> creatures) {
        StringBuilder view = new StringBuilder();
        if (creatures.size() > 0) {
            view.append("|").append(" ".repeat(CREATURE_OFFSET))
                    .append(" ".repeat(ROW_SIZE - CREATURE_OFFSET - 2)).append("|\n");
        }
        view.append(getViewForCreatures(creatures));
        return view;
    }
}

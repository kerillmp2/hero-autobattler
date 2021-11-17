package core.battle;

import java.util.List;
import java.util.stream.Collectors;

import core.battlefield.Battlefield;
import core.battlefield.BattlefieldCreature;
import core.battlefield.ObjectStatus;
import core.battlefield.Position;
import core.utils.Constants;

public class BattleMap {

    private static final int ROW_SIZE = Constants.BATTLEFIELD_VIEW_SIZE.value;
    private static final int CREATURE_VIEW_LENGTH = Constants.BATTLE_VIEW_LENGTH.value + 2;
    private static final int CREATURE_VIEW_HEIGHT = Constants.BATTLE_VIEW_HEIGHT.value;
    private static final int CREATURE_OFFSET = 4;

    public static String getBattleFieldView(Battlefield battlefield) {
        StringBuilder view = new StringBuilder();

        view.append("-".repeat(ROW_SIZE)).append("\n");

        List<BattlefieldCreature> creatures;

        creatures = battlefield.getSecondSide().getCreaturesOnPosition(Position.THIRD_LINE).stream().filter(c -> c.hasStatus(ObjectStatus.ALIVE)).collect(Collectors.toList());
        view.append(getCreaturesRowOnPosition(creatures, Position.THIRD_LINE));

        creatures = battlefield.getSecondSide().getCreaturesOnPosition(Position.SECOND_LINE).stream().filter(c -> c.hasStatus(ObjectStatus.ALIVE)).collect(Collectors.toList());
        view.append(getCreaturesRowOnPosition(creatures, Position.SECOND_LINE));

        creatures = battlefield.getSecondSide().getCreaturesOnPosition(Position.FIRST_LINE).stream().filter(c -> c.hasStatus(ObjectStatus.ALIVE)).collect(Collectors.toList());
        view.append(getCreaturesRowOnPosition(creatures, Position.FIRST_LINE));

        view.append("=".repeat(ROW_SIZE)).append("\n");
        view.append("|").append(" ".repeat(ROW_SIZE - 2)).append("|\n");
        view.append("=".repeat(ROW_SIZE)).append("\n");

        creatures = battlefield.getFirstSide().getCreaturesOnPosition(Position.FIRST_LINE).stream().filter(c -> c.hasStatus(ObjectStatus.ALIVE)).collect(Collectors.toList());
        view.append(getCreaturesRowOnPosition(creatures, Position.FIRST_LINE));

        creatures = battlefield.getFirstSide().getCreaturesOnPosition(Position.SECOND_LINE).stream().filter(c -> c.hasStatus(ObjectStatus.ALIVE)).collect(Collectors.toList());
        view.append(getCreaturesRowOnPosition(creatures, Position.SECOND_LINE));

        creatures = battlefield.getFirstSide().getCreaturesOnPosition(Position.THIRD_LINE).stream().filter(c -> c.hasStatus(ObjectStatus.ALIVE)).collect(Collectors.toList());
        view.append(getCreaturesRowOnPosition(creatures, Position.THIRD_LINE));

        view.append("-".repeat(ROW_SIZE)).append("\n");
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
            view.append("|").append(" ".repeat(ROW_SIZE - 2)).append("|\n");
        }

        return view;
    }

    public static StringBuilder getCreaturesRowOnPosition(List<? extends HasBattleView> creatures, Position position) {
        StringBuilder view = new StringBuilder();
        if (creatures.size() > 0) {
            view.append("|").append(" ".repeat(CREATURE_OFFSET)).append(position.name)
                    .append(" ".repeat(ROW_SIZE - CREATURE_OFFSET - 2 - position.name.length())).append("|\n");
        }
        view.append(getViewForCreatures(creatures));
        return view;
    }
}

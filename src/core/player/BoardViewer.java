package core.player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import core.battle.BattleMap;
import core.battlefield.Position;
import core.traits.Trait;
import core.utils.Constants;
import core.controllers.utils.MessageController;

public class BoardViewer {

    private static final int ROW_SIZE = Constants.BATTLEFIELD_VIEW_SIZE.value;
    private static final int OFFSET = Constants.MAP_OFFSET.value;

    public static void showBoardView(Board board, int limit) {
        StringBuilder boardView = new StringBuilder();
        boardView.append(getBoardViewHeader(board, limit));
        for (Position position : Position.values()) {
            boardView.append(BattleMap.getCreaturesRowOnPosition(board.getCreaturesOnPosition(position), position));
        }
        boardView.append(getBoardViewFooter(board));
        MessageController.print(boardView.toString());
    }

    private static StringBuilder getBoardViewHeader(Board board, int limit) {
        StringBuilder header = new StringBuilder();
        header.append("+").append("-".repeat(ROW_SIZE - 2)).append("+").append("\n");
        StringBuilder row = new StringBuilder();
        row.append("|").append(" ".repeat(OFFSET)).append("Ваше поле")
                .append(" ".repeat(OFFSET)).append("[").append(board.getAllCreatures().size()).append(" / ").append(limit).append("]");
        row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
        header.append(row);

        header.append("+").append("-".repeat(ROW_SIZE - 2)).append("+").append("\n");
        return header;
    }

    private static StringBuilder getBoardViewFooter(Board board) {
        StringBuilder footer = new StringBuilder();
        footer.append("+").append("-".repeat(ROW_SIZE - 2)).append("+").append("\n");
        footer.append(getTraitsView(board.getTraits()));
        footer.append("+").append("-".repeat(ROW_SIZE - 2)).append("+").append("\n");
        return footer;
    }

    private static StringBuilder getTraitsView(Map<Trait, Integer> traits) {
        StringBuilder traitsView = new StringBuilder();
        int maxLen = 24;
        for (Map.Entry<Trait, Integer> entry : traits.entrySet()) {
            Trait trait = entry.getKey();
            int traitNum = entry.getValue();
            int levels = trait.getLevels().size();
            StringBuilder traitRow = new StringBuilder();
            traitRow.append("|").append(" ".repeat(OFFSET)).append(trait.getName()).append(": ").append(traitNum);
            traitRow.append(" ".repeat(maxLen - traitRow.length())).append("[");
            for(int i = 0; i < levels; i++) {
                int nextLvl = i < levels - 1 ? trait.getLevels().get(i + 1) : 100;
                if (traitNum >= trait.getLevels().get(i) && traitNum <= nextLvl) {
                    traitRow.append("(");
                }
                traitRow.append(trait.getLevels().get(i));
                if (traitNum >= trait.getLevels().get(i) && traitNum <= nextLvl) {
                    traitRow.append(")");
                }
                if (i < levels - 1) {
                    traitRow.append(" / ");
                }
            }
            traitRow.append("] ");

            //AUTO SPLIT TRAIT INFO
            /*int lengthBeforeInfo = traitRow.length();
            StringBuilder infoRow = new StringBuilder();
            List<String> splittedInfo = Arrays.asList(trait.getInfo().split(" "));
            for (int i = 0; i < splittedInfo.size(); i++) {
                String nextWord = splittedInfo.get(i);
                if (lengthBeforeInfo + infoRow.length() + nextWord.length() + OFFSET <= ROW_SIZE - 1) {
                    infoRow.append(nextWord).append(" ");
                } else {
                    traitRow.append(infoRow);
                    traitRow.append(" ".repeat(ROW_SIZE - infoRow.length() - lengthBeforeInfo - 1)).append("|\n");
                    traitRow.append("|").append(" ".repeat(lengthBeforeInfo - 1));
                    infoRow = new StringBuilder();
                    infoRow.append(nextWord).append(" ");
                }
            }
            traitRow.append(infoRow);
            traitRow.append(" ".repeat(ROW_SIZE - infoRow.length() - lengthBeforeInfo - 1)).append("|\n");*/

            int lengthBeforeInfo = traitRow.length();
            List<String> splittedInfo = Arrays.asList(trait.getInfo().split("\n"));
            for (int i = 0; i < splittedInfo.size(); i++) {
                String nextRow = splittedInfo.get(i);
                traitRow.append(nextRow).append(" ".repeat(ROW_SIZE - lengthBeforeInfo - nextRow.length() - 1)).append("|\n");
                if (i != splittedInfo.size() - 1) {
                    traitRow.append("|").append(" ".repeat(lengthBeforeInfo - 1));
                }
            }
            traitsView.append(traitRow);
        }
        return traitsView;
    }
}

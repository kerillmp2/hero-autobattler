package core.player;

import java.util.Map;

import core.battle.BattleMap;
import core.battlefield.Position;
import core.traits.Trait;
import core.utils.Constants;
import core.utils.MessageController;

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
        for (Map.Entry<Trait, Integer> entry : traits.entrySet()) {
            Trait trait = entry.getKey();
            int levels = trait.getLevels().size();
            StringBuilder traitRow = new StringBuilder();
            traitRow.append("|").append(" ".repeat(OFFSET)).append(trait.getName()).append(": ").append(entry.getValue());
            traitRow.append(" ".repeat(OFFSET)).append("[");
            for(int i = 0; i < levels; i++) {
                traitRow.append(trait.getLevels().get(i));
                if (i < levels - 1) {
                    traitRow.append(" / ");
                }
            }
            traitRow.append("]");
            traitRow.append(" ").append(trait.getInfo());
            traitRow.append(" ".repeat(ROW_SIZE - traitRow.length() - 1)).append("|\n");
            traitsView.append(traitRow);
        }
        return traitsView;
    }
}

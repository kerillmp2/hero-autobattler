package core.player;

import core.battle.BattleMap;
import core.battlefield.Position;
import core.utils.Constants;
import core.utils.MessageController;

public class BoardViewer {

    private static final int ROW_SIZE = Constants.BATTLEFIELD_VIEW_SIZE.value;
    private static final int OFFSET = 4;

    public static void showBoardView(Board board) {
        StringBuilder boardView = new StringBuilder();
        boardView.append(getBoardViewHeader());
        for (Position position : Position.values()) {
            boardView.append(BattleMap.getCreaturesRowOnPosition(board.getCreaturesOnPosition(position), position));
        }
        boardView.append(getBoardViewFooter());
        MessageController.print(boardView.toString());
    }

    private static StringBuilder getBoardViewHeader() {
        StringBuilder header = new StringBuilder();
        header.append("-".repeat(ROW_SIZE)).append("\n");
        StringBuilder row = new StringBuilder();
        row.append("|").append(" ".repeat(OFFSET)).append("Ваше поле");
        row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
        header.append(row);
        header.append("-".repeat(ROW_SIZE)).append("\n");
        return header;
    }

    private static StringBuilder getBoardViewFooter() {
        StringBuilder footer = new StringBuilder();
        footer.append("-".repeat(ROW_SIZE)).append("\n");
        return footer;
    }
}

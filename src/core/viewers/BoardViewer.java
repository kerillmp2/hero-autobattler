package core.viewers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.player.Bench;
import core.player.Board;
import core.battlefield.Position;
import core.creature.Creature;
import core.traits.Trait;
import utils.Constants;
import core.controllers.utils.MessageController;

public class BoardViewer extends Viewer {

    private static final int ROW_SIZE = Constants.BATTLEFIELD_VIEW_SIZE.value;
    private static final int OFFSET = Constants.MAP_OFFSET.value;

    public static void showBoardView(Board board, Bench bench, int limit) {
        StringBuilder boardView = new StringBuilder();
        boardView.append(getBoardViewHeader(board, limit));
        for (Position position : Position.values()) {
            boardView.append(BattlefieldViewer.getCreaturesRowOnPosition(board.getCreaturesOnPosition(position), position));
        }
        boardView.append(getBoardViewFooter(board));

        MessageController.print(boardView.toString());
        MessageController.print(getBenchView(bench, false));
    }

    public static void showBoardView(Board board, int limit) {
        StringBuilder boardView = new StringBuilder();
        boardView.append(getBoardViewHeader(board, limit));
        for (Position position : Position.values()) {
            boardView.append(BattlefieldViewer.getCreaturesRowOnPosition(board.getCreaturesOnPosition(position), position));
        }
        boardView.append(getBoardViewFooter(board));

        MessageController.print(boardView.toString());
    }

    private static String getBenchView(Bench bench, boolean enumerate) {
        int limit = Constants.BENCH_SIZE.value;
        String header = "Your bench";
        Window window = new Window();
        window.lineWithAngles();
        window.line(header + " ".repeat(Constants.MAX_NAME_LEN.value - header.length() + 2) + "[" + (limit - bench.getFreeSpace()) + " / " + limit + "]");
        window.lineWithAngles();
        window.list(
                bench.getCreaturesWithDummys().stream().map(c -> c.getShopView(true)).filter(c -> !c.equals("Empty") && !c.equals("Пусто")).collect(Collectors.toList()),
                enumerate, 1, false, false);
        window.lineWithAngles();
        return window.getView();
    }

    private static StringBuilder getBoardViewHeader(Board board, int limit) {
        StringBuilder header = new StringBuilder();
        String headerLine = "Your board";
        header.append(lineWithAngles());
        StringBuilder row = new StringBuilder();
        row.append("|").append(" ".repeat(OFFSET)).append(headerLine).append(" ".repeat(Constants.MAX_NAME_LEN.value - headerLine.length() + 2))
                .append("[").append(board.getAllCreatures().size()).append(" / ").append(limit).append("]");
        row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
        header.append(row);

        header.append(lineWithAngles());
        return header;
    }

    private static StringBuilder getBoardViewFooter(Board board) {
        StringBuilder footer = new StringBuilder();
        footer.append(lineWithAngles());
        footer.append(getTraitsView(board.getTraits()));
        footer.append(lineWithAngles());
        return footer;
    }

    private static StringBuilder getTraitsView(Map<Trait, Integer> traits) {
        StringBuilder traitsView = new StringBuilder();
        int maxLen = 30;
        for (Map.Entry<Trait, Integer> entry : traits.entrySet()) {
            Trait trait = entry.getKey();
            int traitNum = entry.getValue();
            int levels = trait.getLevels().size();
            StringBuilder traitRow = new StringBuilder();
            traitRow.append("|").append(" ".repeat(OFFSET)).append(trait.getName()).append(": ").append(traitNum);
            traitRow.append(" ".repeat(maxLen - traitRow.length())).append("[");
            for(int i = 0; i < levels; i++) {
                int nextLvl = i < levels - 1 ? trait.getLevels().get(i + 1) : 100;
                if (traitNum >= trait.getLevels().get(i) && traitNum <= nextLvl && Constants.SHOW_CURRENT_TRAIT_LEVEL.value == 1) {
                    traitRow.append("(");
                }
                traitRow.append(trait.getLevels().get(i));
                if (traitNum >= trait.getLevels().get(i) && traitNum <= nextLvl && Constants.SHOW_CURRENT_TRAIT_LEVEL.value == 1) {
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

    public static String benchBoardSimpleView(List<Creature> boardCreatures, List<Creature> benchCreatures) {
        Window window = new Window();
        window.lineWithAngles();
        window.emptyLine();
        window.line("0. Back");
        window.emptyLine();
        window.line("Creatures on board:");
        window.emptyLine();
        window.list(boardCreatures.stream().map(c -> c.getShopView(true)).collect(Collectors.toList()), true, 1, true, false);
        window.line("Creatures on bench:");
        window.emptyLine();
        window.list(benchCreatures.stream().map(c -> c.getShopView(true)).collect(Collectors.toList()), true, boardCreatures.size() + 1, true, false);
        window.lineWithAngles();
        return window.getView();
    }
}

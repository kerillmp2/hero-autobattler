package core.viewers;

import core.controllers.utils.MessageController;
import core.item.Item;
import utils.Constants;

public class ItemViewer extends Viewer {

    private static final int LENGTH = Constants.ITEM_VIEW_LENGTH.value;
    private static final int HEIGHT = Constants.ITEM_VIEW_HEIGHT.value;
    private static final int OFFSET = Constants.ITEM_VIEW_OFFSET.value;

    public static String getItemView(Item item) {
        Window window = new Window(LENGTH, OFFSET);
        window.lineWithAngles();
        window.line(item.getNameWithRarity());
        window.lineWithAngles();

        String[] nameParts = item.getDescription().split(" ");
        StringBuilder line = new StringBuilder();
        int curLength = 0;
        for (int i = 0; i < nameParts.length; i++) {
            int curHeight = window.getCurrentHeight();
            String currentPart = nameParts[i];
            if (curHeight >= HEIGHT - 1) {
                break;
            }
            if (currentPart.length() > LENGTH) {
                continue;
            }
            if (curLength > 0) {
                if (currentPart.length() + curLength < LENGTH - 2 - OFFSET * 2) {
                    line.append(" ").append(currentPart);
                    curLength += currentPart.length() + 1;
                } else {
                    window.line(line.toString());
                    line = new StringBuilder();
                    curLength = 0;
                    i--;
                    continue;
                }
            } else {
                line.append(currentPart);
                curLength += currentPart.length();
            }
            if (currentPart.endsWith("$")) {
                window.line(line.toString());
                line = new StringBuilder();
                curLength = 0;
            }
        }
        window.line(line.toString());
        int currentHeight = window.getCurrentHeight();
        int rowsRemains = HEIGHT - currentHeight;
        while (rowsRemains > 1) {
            window.emptyLine();
            rowsRemains--;
        }
        window.lineWithAngles();
        MessageController.print(window.getView());
        return window.getView();
    }

}

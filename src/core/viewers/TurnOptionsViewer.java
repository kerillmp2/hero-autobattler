package core.viewers;

import java.util.List;

import core.controllers.utils.MessageController;
import utils.Constants;
import utils.HasName;

public class TurnOptionsViewer extends Viewer {

    private static final int ROW_SIZE = Constants.SHOP_VIEW_SIZE.value;
    private static final int OFFSET = Constants.MAP_OFFSET.value;

    public static String getOptionsView(List<? extends HasName> options) {
        StringBuilder view = new StringBuilder();
        int counter = 0;
        view.append(lineWithAngles());
        view.append(emptyLine());
        for (HasName option : options) {
            StringBuilder row = new StringBuilder();
            row.append("|").append(" ".repeat(OFFSET));
            row.append(String.format("%d. %s", counter, option.getName()));
            row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
            view.append(row);
            view.append(emptyLine());
            counter += 1;
        }
        view.append(lineWithAngles());
        return view.toString();
    }
}

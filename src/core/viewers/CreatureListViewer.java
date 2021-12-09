package core.viewers;

import java.util.List;

import core.creature.Creature;
import core.shop.HasShopView;
import utils.Constants;
import utils.Pair;

public class CreatureListViewer extends Viewer {

    private static final int ROW_SIZE = Constants.SHOP_VIEW_SIZE.value;
    private static final int OFFSET = Constants.MAP_OFFSET.value;

    public static String getCreatureListView(List<? extends HasShopView> creatures, Pair<String, Integer>... additionalOptions) {
        int counter = 0;
        StringBuilder view = new StringBuilder();
        for (HasShopView creature : creatures) {
            counter += 1;
            StringBuilder row = new StringBuilder();
            row.append("|").append(" ".repeat(OFFSET));
            row.append(String.format("%d. %s", counter, creature.getShopView()));
            row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
            view.append(row);
            view.append(emptyLine());
        }

        for (Pair<String, Integer> option : additionalOptions) {
            counter += 1;
            StringBuilder row = new StringBuilder();
            row.append("|").append(" ".repeat(OFFSET));
            row.append(String.format("%d. %s (%d)", counter, option.first, option.second));
            row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
            view.append(row);
            view.append(emptyLine());
        }
        return view.toString();
    }
}

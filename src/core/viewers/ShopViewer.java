package core.viewers;

import java.util.List;

import core.shop.HasShopView;
import utils.Constants;
import utils.Pair;

public class ShopViewer extends Viewer {
    private static final int ROW_SIZE = Constants.SHOP_VIEW_SIZE.value;
    private static final int OFFSET = Constants.MAP_OFFSET.value;

    public static String getShopView(int shopLevel, int money, List<? extends HasShopView> items, Pair<String, Integer>... additionalOptions) {
        StringBuilder view = new StringBuilder();
        view.append(getHeader(shopLevel, money));
        view.append(emptyLine());
        view.append(getBody(items, additionalOptions));
        view.append(getFooter());
        return view.toString();
    }

    public static String getHeader(int shopLevel, int money) {
        StringBuilder view = new StringBuilder();
        StringBuilder row = new StringBuilder();
        String currentMoney = "You have (" + money + ") coins";

        view.append(lineWithAngles());
        row.append("|").append(" ".repeat(OFFSET)).append("Shop [Level ").append(shopLevel).append("]");
        row.append(" ".repeat(ROW_SIZE - row.length() - currentMoney.length() - OFFSET - 1));
        row.append(currentMoney).append(" ".repeat(OFFSET)).append("|\n");
        view.append(row);
        view.append(lineWithAngles());

        return view.toString();
    }

    public static String getBody(List<? extends HasShopView> items, Pair<String, Integer>... additionalOptions) {
        int counter = 0;
        StringBuilder view = new StringBuilder();
        StringBuilder row = new StringBuilder();
        row.append("|").append(" ".repeat(OFFSET));
        row.append("0. Back");
        row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
        view.append(row);
        view.append(emptyLine());
        for (HasShopView item : items) {
            counter += 1;
            row = new StringBuilder();
            row.append("|").append(" ".repeat(OFFSET));
            row.append(String.format("%d. %s", counter, item.getShopView()));
            row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
            view.append(row);
            view.append(emptyLine());
        }

        for (Pair<String, Integer> option : additionalOptions) {
            counter += 1;
            row = new StringBuilder();
            row.append("|").append(" ".repeat(OFFSET));
            row.append(String.format("%d. %s (%d)", counter, option.first, option.second));
            row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
            view.append(row);
            view.append(emptyLine());
        }
        return view.toString();
    }

    public static String getFooter() {
        return lineWithAngles();
    }
}

package core.viewers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        Window window = new Window();
        window.list(items.stream().map(HasShopView::getShopView).collect(Collectors.toList()), true, 0, true, true);
        window.list(Arrays.stream(additionalOptions).map(option -> String.format("%s (%d)", option.first, option.second)).collect(Collectors.toList()), true, items.size() + 1, true, false);
        return window.getView();
    }

    public static String getFooter() {
        return lineWithAngles();
    }
}

package core.shop;

import utils.Constants;

public class ShopView {
    private static final int ROW_SIZE = Constants.SHOP_VIEW_SIZE.value;
    private static final int HEADER_OFFSET = 2;

    public static String getHeader(int shopLevel, int money) {
        StringBuilder view = new StringBuilder();
        StringBuilder row = new StringBuilder();
        String currentMoney = "Монет осталось (" + money + ")";

        view.append("-".repeat(ROW_SIZE)).append("\n");
        row.append("|").append(" ".repeat(HEADER_OFFSET)).append("Магазин [Уровень ").append(shopLevel).append("]");
        row.append(" ".repeat(ROW_SIZE - row.length() - currentMoney.length() - HEADER_OFFSET - 1));
        row.append(currentMoney).append(" ".repeat(HEADER_OFFSET)).append("|\n");
        view.append(row);
        view.append("-".repeat(ROW_SIZE)).append("\n");

        return view.toString();
    }

    public static String getFooter() {
        return "-".repeat(ROW_SIZE) + "\n";
    }
}

package core.shop;

import java.util.ArrayList;
import java.util.List;

public class ShopLine<T extends HasShopView> {
    public List<ShopItem<T>> items;

    public ShopLine(List<ShopItem<T>> items) {
        this.items = items;
    }

    public ShopLine() {
        this(new ArrayList<>());
    }

    public String getView() {
        StringBuilder view = new StringBuilder();
        for (ShopItem<T> item : items) {
            view.append(item.getItem().getShopView()).append("\n");
        }
        return view.toString();
    }
}

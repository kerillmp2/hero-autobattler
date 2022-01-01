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

    public boolean isEmpty() {
        for (ShopItem<T> item : items) {
            if (!item.getItem().getName().equals("Empty")
                    && !item.getItem().getName().equals("Пусто")
                    && !item.getItem().getName().equals("Продано")
                    && !item.getItem().getName().equals("Sold")) {
                return false;
            }
        }
        return true;
    }
}

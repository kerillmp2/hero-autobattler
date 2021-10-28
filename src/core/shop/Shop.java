package core.shop;

import java.util.Collections;
import java.util.List;

public abstract class Shop<T extends HasShopView> {
    protected List<ShopItem<T>> itemPool;
    protected ShopLine<T> currentLine;
    protected int shopLineLength;

    public Shop(List<ShopItem<T>> itemPool, int shopLineLength) {
        this.itemPool = itemPool;
        this.shopLineLength = shopLineLength;
        this.currentLine = generateLine(shopLineLength);
    }

    private ShopLine<T> generateLine(int length) {
        ShopLine<T> shopLine = new ShopLine<>();
        Collections.shuffle(itemPool);
        int lineSize = Math.min(length, itemPool.size());
        for (int i = 0; i < lineSize; i++) {
            shopLine.items.add(itemPool.get(i));
        }
        return shopLine;
    }

    public void refreshLine() {
        this.currentLine = generateLine(shopLineLength);
    }

    public void regenerate() {

    }

    public List<ShopItem<T>> getItemPool() {
        return itemPool;
    }

    public ShopLine<T> getCurrentLine() {
        return currentLine;
    }

    public int getShopLineLength() {
        return shopLineLength;
    }
}

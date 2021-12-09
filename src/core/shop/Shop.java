package core.shop;

import java.util.Collections;
import java.util.List;

public abstract class Shop<T extends HasShopView> {
    public List<ShopItem<T>> itemPool;
    public ShopLine<T> currentLine;
    public int shopLineLength;
    public int shopLevel;

    public Shop(List<ShopItem<T>> itemPool, int shopLineLength, int shopLevel) {
        this.itemPool = itemPool;
        this.shopLineLength = shopLineLength;
        this.shopLevel = shopLevel;
        this.currentLine = generateLine(shopLineLength, shopLevel);
    }

    protected ShopLine<T> generateLine(int length) {
        ShopLine<T> shopLine = new ShopLine<>();
        shufflePool();
        int lineSize = Math.min(length, itemPool.size());
        for (int i = 0; i < lineSize; i++) {
            shopLine.items.add(itemPool.get(i));
        }
        return shopLine;
    }

    protected ShopLine<T> generateLine(int length, int tavernLevel) {
        return generateLine(length);
    }

    public void refreshLine() {
        this.currentLine = generateLine(shopLineLength, shopLevel);
    }

    public void regenerate() {

    }

    public void changeItemToDummy(int index) {

    }

    protected void shufflePool() {
        Collections.shuffle(itemPool);
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

    public void incrementShopLevel() {
        shopLevel++;
    }
}

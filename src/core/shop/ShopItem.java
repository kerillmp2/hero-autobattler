package core.shop;

import core.creature.Creature;

public class ShopItem<T extends HasShopView> {
    private T item;
    private int cost;

    public ShopItem(T item, int cost) {
        this.cost = cost;
        this.item = item;
    }

    public int getCost() {
        return cost;
    }

    public T getItem() {
        return item;
    }

    public static ShopItem<Creature> creatureDummy() {
        Creature dummy = Creature.shopDummy();
        return new ShopItem<>(dummy, dummy.getSellingCost());
    }
}

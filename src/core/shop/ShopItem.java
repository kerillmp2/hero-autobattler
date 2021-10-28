package core.shop;

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

    public void setCost(int cost) {
        this.cost = cost;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}

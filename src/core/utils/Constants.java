package core.utils;

public enum Constants {
    UNDEFINED("UNDEFINED", 0, 0),
    BATTLE_VIEW_LENGTH("BATTLE_VIEW_LENGTH", 1, 14),
    BATTLE_VIEW_HEIGHT("BATTLE_VIEW_HEIGHT", 2, 8),
    BATTLEFIELD_VIEW_SIZE("BATTLEFIELD_VIEW_SIZE", 3, 100),
    SHOP_VIEW_SIZE("SHOP_VIEW_SIZE", 4, 100);

    public String name;
    public int id;
    public int value;

    Constants(String name, int id, int value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }
}

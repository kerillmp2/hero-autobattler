package core.utils;

public enum Constants {
    UNDEFINED("UNDEFINED", 0, 0),
    BATTLE_VIEW_LENGTH("BATTLE_VIEW_LENGTH", 1, 20),
    BATTLE_VIEW_HEIGHT("BATTLE_VIEW_HEIGHT", 2, 9),
    BATTLEFIELD_VIEW_SIZE("BATTLEFIELD_VIEW_SIZE", 3, 100),
    SHOP_VIEW_SIZE("SHOP_VIEW_SIZE", 4, 100),
    MAP_OFFSET("MAP_OFFSET",5, 3);

    public String name;
    public int id;
    public int value;

    Constants(String name, int id, int value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }
}

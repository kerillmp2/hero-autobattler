package core.utils;

public enum Constants {
    UNDEFINED("UNDEFINED", 0, 0),
    BATTLE_VIEW_LENGTH("BATTLE_VIEW_LENGTH", 1, 20),
    BATTLE_VIEW_HEIGHT("BATTLE_VIEW_HEIGHT", 2, 10),
    BATTLEFIELD_VIEW_SIZE("BATTLEFIELD_VIEW_SIZE", 3, 120),
    SHOP_VIEW_SIZE("SHOP_VIEW_SIZE", 4, 120),
    MAP_OFFSET("MAP_OFFSET",5, 4),
    MAX_NAME_LEN("MAX_NAME_LEN", 6, 14),
    MAX_TRAIT_NAME_LEN("MAX_TRAIT_NAME_LEN", 7, 10),
    AD_HP_LEN("AD_HP_LEN", 8, 22),
    MAX_STATS_LEN("MAX_STATS_LEN", 9, 60);

    public String name;
    public int id;
    public int value;

    Constants(String name, int id, int value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }
}

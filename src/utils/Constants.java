package utils;

public enum Constants {
    UNDEFINED("UNDEFINED", 0, 0),

    //View константы
    BATTLE_VIEW_LENGTH("BATTLE_VIEW_LENGTH", 1, 16),
    BATTLE_VIEW_HEIGHT("BATTLE_VIEW_HEIGHT", 2, 10),
    BATTLEFIELD_VIEW_SIZE("BATTLEFIELD_VIEW_SIZE", 3, 120),
    SHOP_VIEW_SIZE("SHOP_VIEW_SIZE", 4, 120),
    MAP_OFFSET("MAP_OFFSET",5, 4),
    MAX_NAME_LEN("MAX_NAME_LEN", 6, 14),
    MAX_TRAIT_NAME_LEN("MAX_TRAIT_NAME_LEN", 7, 10),
    AD_HP_LEN("AD_HP_LEN", 8, 22),
    MAX_STATS_LEN("MAX_STATS_LEN", 9, 60),
    SHOW_CURRENT_TRAIT_LEVEL("SHOW_CURRENT_TRAIT_LEVEL", 10, 0),
    SHOW_POSITION_NAMES("SHOW_POSITION_NAMES", 11, 0),
    PRINT_MESSAGES_IN_CONTROLLER("PRINT_MESSAGES_IN_CONTROLLER", 12, 0),

    //Боевые константы
    MANA_AFTER_TAKING_DAMAGE("MANA_AFTER_TAKING_DAMAGE", 100, 9),
    MANA_AFTER_DEALING_DAMAGE("MANA_AFTER_DEALING_DAMAGE", 101, 20),


    //Другие игровые константы
    BENCH_SIZE("BENCH_SIZE", 199, 7),
    BATTLE_TURN_LIMIT("BATTLE_TURN_LIMIT", 200, 50),
    MONEY_AFTER_BATTLE("MONEY_AFTER_BATTLE", 201, 2),
    MONEY_FOR_WIN("MONEY_FOR_WIN", 202, 1),
    ;

    public String name;
    public int id;
    public int value;

    Constants(String name, int id, int value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }
}

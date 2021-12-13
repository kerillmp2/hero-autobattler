package utils;

public enum Constants {
    UNDEFINED("UNDEFINED", 0, 0),

    //View константы
    BATTLE_VIEW_LENGTH("BATTLE_VIEW_LENGTH", 1, 16),
    BATTLE_VIEW_HEIGHT("BATTLE_VIEW_HEIGHT", 2, 9),
    BATTLE_VIEW_OFFSET("BATTLE_VIEW_OFFSET", 3, 1),
    ITEM_VIEW_LENGTH("ITEM_VIEW_LENGTH", 4, 22),
    ITEM_VIEW_HEIGHT("ITEM_VIEW_HEIGHT", 5, 10),
    ITEM_VIEW_OFFSET("ITEM_VIEW_OFFSET", 6, 1),
    BATTLEFIELD_VIEW_SIZE("BATTLEFIELD_VIEW_SIZE", 7, 140),
    SHOP_VIEW_SIZE("SHOP_VIEW_SIZE", 8, 140),
    MAP_OFFSET("MAP_OFFSET",9, 4),
    MAX_NAME_LEN("MAX_NAME_LEN", 10, 14),
    MAX_TRAIT_NAME_LEN("MAX_TRAIT_NAME_LEN", 11, 10),
    AD_HP_LEN("AD_HP_LEN", 12, 22),
    MAX_STATS_LEN("MAX_STATS_LEN", 13, 60),
    SHOW_CURRENT_TRAIT_LEVEL("SHOW_CURRENT_TRAIT_LEVEL", 14, 0),
    SHOW_POSITION_NAMES("SHOW_POSITION_NAMES", 15, 0),

    //PRINT константы
    PRINT_MESSAGES_IN_CONTROLLER("PRINT_MESSAGES_IN_CONTROLLER", 100, 0),
    PRINT_ENG_MESSAGES("PRINT_ENG_MESSAGES", 101, 1),
    PRINT_RU_MESSAGES("PRINT_RU_MESSAGES", 102, 0),

    //Боевые константы
    MANA_AFTER_TAKING_DAMAGE("MANA_AFTER_TAKING_DAMAGE", 200, 9),
    MANA_AFTER_DEALING_DAMAGE("MANA_AFTER_DEALING_DAMAGE", 201, 20),


    //Другие игровые константы
    BENCH_SIZE("BENCH_SIZE", 300, 5),
    BATTLE_TURN_LIMIT("BATTLE_TURN_LIMIT", 301, 50),
    MONEY_AFTER_BATTLE("MONEY_AFTER_BATTLE", 302, 2),
    MONEY_FOR_WIN("MONEY_FOR_WIN", 303, 1),
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

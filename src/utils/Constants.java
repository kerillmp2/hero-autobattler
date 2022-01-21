package utils;

public enum Constants {
    UNDEFINED("UNDEFINED", 0, 0),

    //View константы
    BATTLE_VIEW_LENGTH("BATTLE_VIEW_LENGTH", 1, 18),
    BATTLE_VIEW_HEIGHT("BATTLE_VIEW_HEIGHT", 2, 11),
    BATTLE_VIEW_OFFSET("BATTLE_VIEW_OFFSET", 3, 1),
    ITEM_VIEW_LENGTH("ITEM_VIEW_LENGTH", 4, 24),
    ITEM_VIEW_HEIGHT("ITEM_VIEW_HEIGHT", 5, 12),
    ITEM_VIEW_OFFSET("ITEM_VIEW_OFFSET", 6, 1),
    BATTLEFIELD_VIEW_SIZE("BATTLEFIELD_VIEW_SIZE", 7, 148),
    SHOP_VIEW_SIZE("SHOP_VIEW_SIZE", 8, 148),
    MAP_OFFSET("MAP_OFFSET",9, 4),
    MAX_NAME_LEN("MAX_NAME_LEN", 10, 14),
    MAX_TRAIT_NAME_LEN("MAX_TRAIT_NAME_LEN", 11, 10),
    AD_HP_LEN("AD_HP_LEN", 12, 22),
    MAX_STATS_LEN("MAX_STATS_LEN", 13, 60),
    SHOW_CURRENT_TRAIT_LEVEL("SHOW_CURRENT_TRAIT_LEVEL", 14, 0),

    //PRINT константы
    PRINT_MESSAGES_IN_CONTROLLER("PRINT_MESSAGES_IN_CONTROLLER", 100, 0),
    PRINT_ENG_MESSAGES("PRINT_ENG_MESSAGES", 101, 1),
    PRINT_RU_MESSAGES("PRINT_RU_MESSAGES", 102, 0),
    PRINT_CREATURE_STATS_IN_BATTLE("PRINT_CREATURE_STATS", 103, 0),

    //Боевые константы
    MANA_AFTER_TAKING_DAMAGE("MANA_AFTER_TAKING_DAMAGE", 200, 14),
    MANA_AFTER_DEALING_DAMAGE("MANA_AFTER_DEALING_DAMAGE", 201, 22),

    //Другие игровые константы
    BENCH_SIZE("BENCH_SIZE", 300, 5),
    BATTLE_TURN_LIMIT("BATTLE_TURN_LIMIT", 301, 100),
    MONEY_AFTER_BATTLE("MONEY_AFTER_BATTLE", 302, 2),
    MONEY_FOR_WIN("MONEY_FOR_WIN", 303, 1),
    PLAYER_HP("PLAYER_HP", 304, 30),
    PLAYER_MONEY("PLAYER_MONEY", 305, 1),

    //STATISTIC
    COLLECT_STATISTIC("COLLECT_STATISTIC", 400, 0),
    EACH_BATTLE_STATISTIC("EACH_BATTLE_STATISTIC", 401, 0),
    PRINT_TOP_STATISTIC("PRINT_TOP", 402, 12)
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

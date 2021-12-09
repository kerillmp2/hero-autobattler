package core.action;

import utils.Tag;

public enum ActionTag implements Tag {
    UNDEFINED("UNDEFINED", 0),
    BASIC_ATTACK("BASIC_ATTACK", 1),
    HAS_VALUE("HAS_VALUE", 2),
    TAKE_BASIC_DAMAGE("TAKE_BASIC_DAMAGE", 3),
    DELETE_AFTER_RESOLVE("DELETE_AFTER_RESOLVE", 4),
    GENERATE_ADD_POISON_AFTER_ATTACK("GENERATE_ADD_POISON_AFTER_ATTACK", 5),
    ADD_POISON_AFTER_ATTACK("ADD_POISON_AFTER_ATTACK", 6),
    APPLY_POISON_DAMAGE("APPLY_POISON_DAMAGE", 7),
    TURNS_LEFT("TURNS_LEFT", 8),
    CHOOSE_MAIN_ACTION("CHOOSE_MAIN_ACTION", 9),
    USE_SKILL("USE_SKILL", 10),
    ADD_MANA("ADD_MANA", 11),
    HEAL("HEAL", 12);

    private String name;
    private int id;

    ActionTag(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

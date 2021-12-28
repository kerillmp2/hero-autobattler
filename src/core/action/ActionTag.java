package core.action;

import core.creature.stat.Stat;
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
    HEAL_FLOAT("HEAL", 11),
    HEAL_PERCENT_OF_MAX("HEAL_PERCENT_OF_MAX", 12),
    HEAL_PERCENT_OF_MISSING("HEAL_PERCENT_OF_MISSING", 13),
    DEAL_DAMAGE_TO_ALL_ENEMIES("DEAL_DAMAGE_TO_ALL_ENEMIES", 14),
    ADD_MANA("ADD_MANA", 15),
    ADD_FLOAT_STAT("ADD_STAT", 16),
    ADD_PERCENTAGE_STAT("ADD_PERCENTAGE_STAT", 17),
    DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY("DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY", 19),
    ATTACK("Attack", 100),
    PHYSICAL_ARMOR("Physical armor", 101),
    MAGIC_ARMOR("Magic armor", 102),
    SPELL_POWER("Spell power", 103),
    SPEED("Speed", 104),
    PERCENTAGE("PERCENTAGE", 200);

    private String name;
    private int id;

    ActionTag(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static ActionTag addStat(Stat stat) {
        if (stat == Stat.ATTACK) {
            return ATTACK;
        }
        if (stat == Stat.PHYSICAL_ARMOR) {
            return PHYSICAL_ARMOR;
        }
        if (stat == Stat.MAGIC_ARMOR) {
            return MAGIC_ARMOR;
        }
        if (stat == Stat.SPEED) {
            return SPEED;
        }
        return UNDEFINED;
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

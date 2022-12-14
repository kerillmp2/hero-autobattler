package core.action;

import core.creature.stat.Stat;
import utils.Tag;

public enum ActionTag implements Tag {
    UNDEFINED("UNDEFINED", 0),
    BASIC_ATTACK("BASIC_ATTACK", 1),
    HAS_VALUE("HAS_VALUE", 2),
    TAKE_PHYSICAL_DAMAGE("TAKE_BASIC_DAMAGE", 3),
    TAKE_MAGIC_DAMAGE("TAKE_MAGIC_DAMAGE", 4),
    TAKE_TRUE_DAMAGE("TAKE_TRUE_DAMAGE", 21),
    DELETE_AFTER_RESOLVE("DELETE_AFTER_RESOLVE", 5),
    GENERATE_ADD_POISON_AFTER_ATTACK("GENERATE_ADD_POISON_AFTER_ATTACK", 6),
    ADD_POISON_AFTER_ATTACK("ADD_POISON_AFTER_ATTACK", 7),
    APPLY_POISON_DAMAGE("APPLY_POISON_DAMAGE", 8),
    TURNS_LEFT("TURNS_LEFT", 9),
    CHOOSE_MAIN_ACTION("CHOOSE_MAIN_ACTION", 10),
    USE_SKILL("USE_SKILL", 11),
    HEAL_FLOAT("HEAL", 12),
    HEAL_PERCENT_OF_MAX("HEAL_PERCENT_OF_MAX", 13),
    HEAL_PERCENT_OF_MISSING("HEAL_PERCENT_OF_MISSING", 14),
    DEAL_MAGIC_DAMAGE_TO_ALL_ENEMIES("DEAL_DAMAGE_TO_ALL_ENEMIES", 15),
    ADD_FLOAT_STAT("ADD_STAT", 17),
    ADD_PERCENTAGE_STAT("ADD_PERCENTAGE_STAT", 18),
    REDUCE_FLOAT_STAT("REDUCE_FLOAT_STAT", 19),
    REDUCE_PERCENTAGE_STAT("REDUCE_PERCENTAGE_STAT", 20),
    DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY("DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY", 21),
    APPLY_BURN_DAMAGE("APPLY_BURN_DAMAGE", 22),
    ADD_BURN("ADD_BURN", 23),
    ATTACK("Attack", 100),
    PHYSICAL_ARMOR("Physical armor", 101),
    MAGIC_ARMOR("Magic armor", 102),
    SPELL_POWER("Spell power", 103),
    SPEED("Speed", 104),
    MANA("Mana", 105),
    HP("HP", 106),
    DEAL_MAGIC_DAMAGE("DEAL_MAGIC_DAMAGE", 107),
    BASIC_ATTACK_BUFF("BASIC_ATTACK_BUFF", 108),
    USE_BOUNCING_SKILL("USE_BOUNCING_SKILL", 109),
    DEAL_PHYSICAL_DAMAGE("DEAL_PHYSICAL_DAMAGE", 110),
    BASIC_ATTACK_RESPONSE("BASIC_ATTACK_RESPONSE", 111),
    PARRY("PARRY", 112),
    BASED_ON_STAT("BASED_ON_STAT", 113),
    ADD_PHYSICAL_BARRIER("ADD_PHYSICAL_BARRIER", 114),
    ADD_MAGIC_BARRIER("ADD_MAGIC_BARRIER", 115),
    ADD_BARRIER("ADD_BARRIER", 116),
    SUMMON_CREATURE_WITH_VALUE("SUMMON_CREATURE_WITH_VALUE", 117),
    CHANCE("CHANCE", 201);

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
        if (stat == Stat.MANA) {
            return MANA;
        }
        if (stat == Stat.SPELL_POWER) {
            return SPELL_POWER;
        }
        if (stat == Stat.HP) {
            return HP;
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

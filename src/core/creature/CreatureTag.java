package core.creature;

import utils.Tag;

public enum CreatureTag implements Tag {
    UNDEFINED("UNDEFINED", 0),
    // Poisonous
    POISONOUS("POISONOUS", 2),
    // Eater
    ADD_PERMANENT_PERCENTAGE_HP_BEFORE_BATTLE("ADD_PERMANENT_HP_BEFORE_BATTLE", 3),
    ADD_PERMANENT_FLOAT_HP_BEFORE_BATTLE("ADD_PERMANENT_FLOAT_HP_BEFORE_BATTLE", 4),
    // Robot
    ADD_TEMP_PERCENTAGE_HP_BEFORE_BATTLE("ADD_TEMP_HP_BEFORE_BATTLE", 5),
    ADD_TEMP_PERCENTAGE_ATTACK_BEFORE_BATTLE("ADD_TEMP_ATTACK_BEFORE_BATTLE", 6),
    ADD_TEMP_PARM_BEFORE_BATTLE("ADD_TEMP_PARM_BEFORE_BATTLE", 7),
    ADD_TEMP_MARM_BEFORE_BATTLE("ADD_TEMP_MARM_BEFORE_BATTLE", 8),
    // Student
    ADD_MANA_AFTER_ALLY_USED_SKILL("ADD_MANA_AFTER_ALLY_USED_SKILL", 9),
    // Fireborn
    BURN_BUFF("BURN_BUFF", 10),
    // Demon
    BURN_PERCENTAGE_MANA_ON_ATTACK("BURN_PERCENTAGE_MANA_ON_ATTACK", 11),
    BURN_FLOAT_MANA_ON_ATTACK("BURN_FLOAT_MANA_ON_ATTACK", 12),
    // Alchemist
    BOUNCING_SKILL("BOUNCING_SKILL", 14),
    // Duelist
    COUNTERATTACK_CHANCE("COUNTER_ATTACK_CHANCE", 23),
    COUNTERATTACK_DAMAGE("COUNTERATTACK_DAMAGE", 24),
    PARRY("PARRY", 25),
    // Frostborn
    ADDITIONAL_MAGIC_DAMAGE_ON_ATTACK("ADDITIONAL_MAGIC_DAMAGE_ON_ATTACK", 26),
    PERCENTAGE_SLOW_ON_ATTACK("PERCENTAGE_SLOW_ON_ATTACK", 27),

    // Basic
    HAVE_BASIC_ATTACK("HAVE_BASIC_ATTACK", 1),
    TAKE_MORE_MAGIC_DAMAGE("TAKE_MORE_MAGIC_DAMAGE", 15),
    TAKE_MORE_PHYSICAL_DAMAGE("TAKE_MORE_PHYSICAL_DAMAGE", 16),
    TAKE_MORE_TRUE_DAMAGE("TAKE_MORE_TRUE_DAMAGE", 17),
    TAKE_MORE_DAMAGE("TAKE_MORE_DAMAGE", 17),
    TAKE_LESS_MAGIC_DAMAGE("TAKE_MORE_MAGIC_DAMAGE", 19),
    TAKE_LESS_PHYSICAL_DAMAGE("TAKE_MORE_PHYSICAL_DAMAGE", 20),
    TAKE_LESS_TRUE_DAMAGE("TAKE_MORE_TRUE_DAMAGE", 21),
    TAKE_LESS_DAMAGE("TAKE_LESS_DAMAGE", 22),
    PHYSICAL_BARRIER("PHYSICAL_BARRIER", 23),
    MAGIC_BARRIER("MAGIC_BARRIER", 24),
    BARRIER("BARRIER", 25);

    private String name;
    private int id;

    CreatureTag(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static CreatureTag byName(String name) {
        for (CreatureTag status : CreatureTag.values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }
        return UNDEFINED;
    }

    public static CreatureTag byId(int id) {
        for (CreatureTag status : CreatureTag.values()) {
            if (status.id == id) {
                return status;
            }
        }
        return UNDEFINED;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}

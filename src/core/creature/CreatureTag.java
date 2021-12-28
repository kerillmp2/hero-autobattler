package core.creature;

import utils.Tag;

public enum CreatureTag implements Tag {
    UNDEFINED("UNDEFINED", 0),
    HAVE_BASIC_ATTACK("HAVE_BASIC_ATTACK", 1),
    POISONOUS("POISONOUS", 2),
    ADD_PERMANENT_HP_BEFORE_BATTLE("ADD_PERMANENT_HP_BEFORE_BATTLE", 3),
    ADD_TEMP_RANDOM_STAT_BEFORE_BATTLE("ADD_TEMP_RANDOM_STAT_BEFORE_BATTLE", 4),
    ADD_TEMP_HP_BEFORE_BATTLE("ADD_TEMP_HP_BEFORE_BATTLE", 5),
    ADD_TEMP_ATTACK_BEFORE_BATTLE("ADD_TEMP_ATTACK_BEFORE_BATTLE", 6),
    ADD_TEMP_PARM_BEFORE_BATTLE("ADD_TEMP_PARM_BEFORE_BATTLE", 7),
    ADD_TEMP_MARM_BEFORE_BATTLE("ADD_TEMP_MARM_BEFORE_BATTLE", 8),
    ADD_MANA_AFTER_ALLY_USED_SKILL("ADD_MANA_AFTER_ALLY_USED_SKILL", 9);

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

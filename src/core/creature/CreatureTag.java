package core.creature;

import core.utils.Tag;

public enum CreatureTag implements Tag {
    UNDEFINED("UNDEFINED", 0),
    HAVE_BASIC_ATTACK("HAVE_BASIC_ATTACK", 1),
    POISONOUS("POISONOUS", 2),
    EATER("EATER", 3);

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

package core.battlefield;

import core.utils.Tag;

public enum BattlefieldObjectTag implements Tag {
    UNDEFINED("UNDEFINED", 0),
    HAVE_BASIC_ATTACK("HAVE_BASIC_ATTACK", 1),
    POISONOUS("POISONOUS", 2);

    private String name;
    private int id;

    BattlefieldObjectTag(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static BattlefieldObjectTag byName(String name) {
        for (BattlefieldObjectTag status : BattlefieldObjectTag.values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }
        return UNDEFINED;
    }

    public static BattlefieldObjectTag byId(int id) {
        for (BattlefieldObjectTag status : BattlefieldObjectTag.values()) {
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

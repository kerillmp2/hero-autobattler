package core.battlefield;

import core.utils.Tag;

public enum ObjectStatus implements Tag {
    UNDEFINED("UNDEFINED", 0),
    CREATURE("CREATURE", 1),
    NONCREATURE("NONCREATURE", 2),
    ALIVE("ALIVE", 3),
    DEAD("DEAD", 4),
    HERO("HERO", 5),
    UNIT("UNIT", 6),
    FIRST_LINE("FIRST_LINE", 7),
    SECOND_LINE("SECOND_LINE", 8),
    THIRD_LINE("THIRD_LINE", 9),
    FOURTH_LINE("FOURTH_LINE", 10),
    AIR("AIR", 11),
    UNDERGROUND("UNDERGROUND", 12),
    NOT_ON_BATTLEFIELD("NOT_ON_BATTLEFIELD", 13),
    FIRST_SIDE("FIRST_SIDE", 14),
    SECOND_SIDE("SECOND_SIDE", 15);

    private String name;
    private int id;

    ObjectStatus(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static ObjectStatus byName(String name) {
        for (ObjectStatus status : ObjectStatus.values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }
        return UNDEFINED;
    }

    public static ObjectStatus byId(int id) {
        for (ObjectStatus status : ObjectStatus.values()) {
            if (status.id == id) {
                return status;
            }
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

    public static ObjectStatus byPosition(Position position) {
        return ObjectStatus.byName(position.name);
    }
}

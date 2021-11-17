package core.creature;

import core.utils.Tag;

public enum Stat implements Tag {
    UNDEFINED("UNDEFINED", "UNDEFINED", 0),
    HP("Здоровье", "HP", 1),
    ATTACK("Атака", "А", 2),
    PHYSICAL_ARMOR("Физическая защита", "PDef", 3),
    MAGIC_ARMOR("Магическая защита", "MDef", 4),
    SPELL_POWER("Сила умений", "AP", 5),
    SPEED("Скорость", "Speed", 6);

    private String name;
    private String shortName;
    private int id;

    Stat(String name, String shortName, int id) {
        this.name = name;
        this.shortName = shortName;
        this.id = id;
    }

    public static Stat byName(String name) {
        for (Stat mark : Stat.values()) {
            if (mark.name.equals(name)) {
                return mark;
            }
        }
        return UNDEFINED;
    }

    public static Stat byId(int id) {
        for (Stat mark : Stat.values()) {
            if (mark.id == id) {
                return mark;
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

    public String getShortName() {
        return shortName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

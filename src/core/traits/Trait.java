package core.traits;

import core.utils.Tag;

public enum Trait implements Tag {
    UNDEFINED("UNDEFINED", 0),
    HUMAN("Человек", 1),
    WARRIOR("Воин", 12),
    ASSASSIN("Ассасин", 13),
    DEFENDER("Защитник", 14);

    private String name;
    private int id;

    Trait(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Trait byName(String name) {
        for (Trait trait : Trait.values()) {
            if (trait.name.equals(name)) {
                return trait;
            }
        }
        return UNDEFINED;
    }

    public static Trait byId(int id) {
        for (Trait trait : Trait.values()) {
            if (trait.id == id) {
                return trait;
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

    @Override
    public String toString() {
        return this.name;
    }
}

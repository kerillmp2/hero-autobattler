package core.creature.stat;

import java.util.Arrays;

import core.action.ActionTag;
import core.controllers.utils.RandomController;
import utils.Tag;

public enum Stat implements Tag {
    UNDEFINED("UNDEFINED", "UNDEFINED", 0, 1000),
    HP("HP", "HP", 1, 4.5),
    ATTACK("Attack", "А", 2, 1.1),
    PHYSICAL_ARMOR("Physical armor", "PArm", 3, 1),
    MAGIC_ARMOR("Magic armor", "MArm", 4, 1.8),
    SPELL_POWER("Spell power", "AP", 5, 1.4),
    SPEED("Speed", "Speed", 6, 6),
    MANA("Mana", "Mana", 7, 0);

    private String name;
    private String shortName;
    private int id;
    private double value;
    private static int maxId;

    Stat(String name, String shortName, int id, double value) {
        this.name = name;
        this.shortName = shortName;
        this.id = id;
        this.value = value;
        updateMaxId(id);
    }

    public static void updateMaxId(int id) {
        maxId = Math.max(id, maxId);
    }

    public static Stat byName(String name) {
        for (Stat mark : Stat.values()) {
            if (mark.name.equals(name)) {
                return mark;
            }
        }
        return UNDEFINED;
    }

    public static Stat byTag(ActionTag tag) {
        switch (tag) {
            case ATTACK: {
                return ATTACK;
            }
            case HP: {
                return HP;
            }
            case PHYSICAL_ARMOR: {
                return PHYSICAL_ARMOR;
            }
            case MAGIC_ARMOR: {
                return MAGIC_ARMOR;
            }
            case MANA: {
                return MANA;
            }
            case SPEED: {
                return SPEED;
            }
            case SPELL_POWER: {
                return SPELL_POWER;
            }
            default:
                return UNDEFINED;
        }
    }

    public static Stat byId(int id) {
        for (Stat mark : Stat.values()) {
            if (mark.id == id) {
                return mark;
            }
        }
        return UNDEFINED;
    }

    public static Stat getRandomStat() {
        int statId = RandomController.randomInt(1, maxId, true);
        return byId(statId);
    }

    public static Stat getRandomStatExclusive(Stat... stats) {
        int counter = 0;
        while (counter < 100) {
            int statId = RandomController.randomInt(1, maxId, true);
            if (Arrays.stream(stats).noneMatch(s -> s.id == statId)) {
                return byId(statId);
            } else {
                counter++;
            }
        }
        return UNDEFINED;
    }

    public static Stat getRandomStatFrom(Stat... stats) {
        int counter = 0;
        while (counter < 100) {
            int statId = RandomController.randomInt(1, maxId, true);
            if (Arrays.stream(stats).anyMatch(s -> s.id == statId)) {
                return byId(statId);
            } else {
                counter++;
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

    public double getValue() {
        return value;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

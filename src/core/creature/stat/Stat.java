package core.creature.stat;

import java.util.Arrays;

import core.controllers.utils.RandomController;
import utils.Tag;

public enum Stat implements Tag {
    UNDEFINED("UNDEFINED", "UNDEFINED", 0),
    HP("Hit points", "HP", 1),
    ATTACK("Attack", "А", 2),
    PHYSICAL_ARMOR("Physical defense", "PDef", 3),
    MAGIC_ARMOR("Magic defense", "MDef", 4),
    SPELL_POWER("Spell power", "AP", 5),
    SPEED("Speed", "Speed", 6),
    MANA("Mana", "Mana", 7);

    private String name;
    private String shortName;
    private int id;
    private static int maxId;

    Stat(String name, String shortName, int id) {
        this.name = name;
        this.shortName = shortName;
        this.id = id;
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

    public String getShortName() {
        return shortName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

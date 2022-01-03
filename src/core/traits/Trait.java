package core.traits;

import java.util.Arrays;
import java.util.List;

import utils.Tag;

public enum Trait implements Tag {
    UNDEFINED("UNDEFINED", 0, "UNDEFINED"),
    KNIGHT("Knight", 1, "All creatures gains [+2 / +3 / +5] attack", 2, 4, 6),
    POISONOUS("Poisonous", 2, "Poisonous gains [+1 / +2 / +4] poison", 2, 3, 4),
    DEMON("Demon", 3, "???", 2, 4, 6),
    ROBOT("Bot", 4, "At the beginning of the battle Bots gets random buffs [1 / 3 / 7] times from following:\n+8 health, +2 attack, +1 physical and magic armor", 1, 2, 3),
    FROSTBORN("Frostborn", 5, "???", 2, 3, 5),
    STUDENT("Student", 7, "Students gains [5 / 11 / 23] mana when an ally uses ability", 2, 3, 5),
    BEAST("Beast", 8, "???", 2, 4, 6),
    SPIRIT("Spirit", 9, "???", 1, 2, 3),

    WARRIOR("Warrior", 12, "Warriors gains [+1 / +2 / +3] attack and [+1 / +2 / +3] physical defends", 2, 3, 4),
    ASSASSIN("Assassin", 13, "Assassins gains [+2 / +3 / +4] attack and [+10 / +20 / +40] speed", 2, 3, 4),
    DEFENDER("Guardian", 14, "All creatures gains [+2 / +4 / +6] physical armor", 2, 3, 4),
    MAGE("Mage", 15, "Mages gains [+4 / +7 / +10] spell power", 2, 3, 4),
    EATER("Eater", 16, "Eaters permanently gains [+4 / +6 / +10] health before each battle", 2, 4, 5),
    ALCHEMIST("Alchemist", 17, "???", 1, 2, 3),
    FIREBORN("Fireborn", 18, "???", 2, 3, 4),
    ARCHER("Archer", 19, "???", 2, 4, 5);

    private String name;
    private int id;
    private String info;
    private List<Integer> levels;

    Trait(String name, int id, String info, Integer... levels) {
        this.name = name;
        this.id = id;
        this.info = info;
        this.levels = Arrays.asList(levels);
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

    public List<Integer> getLevels() {
        return this.levels;
    }

    public String getInfo() {
        return info;
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

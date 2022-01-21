package core.traits;

import java.util.Arrays;
import java.util.List;

import utils.Tag;

public enum Trait implements Tag {
    UNDEFINED("UNDEFINED", 0, "UNDEFINED"),
    KNIGHT("Knight", 1, "All creatures gains [+2 / +3 / +5] attack", 2, 4, 6),
    POISONOUS("Poisonous", 2, "Poisonous gains [+1 / +2 / +5] poison", 2, 3, 4),
    DEMON("Demon", 3, "After attacking demons burn [12% / 25% / 40%] or target's max mana", 2, 4, 6),
    ROBOT("Bot", 4, "At the beginning of the battle Bots gets random buffs [1 / 3 / 7] times from following:\n+7% health, +9% attack, +1 physical and magic armor", 1, 2, 3),
    FROSTBORN("Frostborn", 5, "???", 2, 3, 5),
    STUDENT("Student", 7, "Students gains [8 / 17 / 25] mana when an ally uses ability", 2, 3, 5),
    BEAST("Beast", 8, "???", 2, 4, 6),
    SPIRIT("Spirit", 9, "???", 1, 2, 3),
    PIRATE("Pirate", 10, "???", 2, 4, 5),
    CULTIST("Cultist", 11, "???", 3, 4, 5),
    UNDEAD("Undead", 12, "???", 2, 4),

    WARRIOR("Warrior", 100, "Warriors gains [+15% / +20% / +30%] attack and [+2 / +3 / +5] physical armor", 2, 3, 4),
    ASSASSIN("Assassin", 101, "Assassins gains [+20% / +30% / +40%] attack and [+15% / +25% / +50%] speed", 2, 3, 4),
    DEFENDER("Defender", 102, "All creatures gains [+2 / +4 / +8] physical and magic armor", 2, 3, 4),
    MAGE("Mage", 103, "Mages gains [+2 / +4 / +7] and [+20% / +45% / +75%] spell power", 2, 3, 4),
    EATER("Eater", 104, "Eaters permanently gains [+3% / +6% / +10%] health before each battle", 2, 4, 5),
    ALCHEMIST("Alchemist", 105, "???", 1, 2, 3),
    FIREBORN("Fireborn", 106, "???", 2, 3, 4),
    ARCHER("Archer", 107, "???", 2, 4, 5),
    DUELIST("Duelist", 108, "???", 2, 3, 5),
    SUMMONER("Summoner", 109, "???", 2, 3, 4),
    HEALER("Healer", 110, "???", 2, 4, 5);

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

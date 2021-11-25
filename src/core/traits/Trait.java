package core.traits;

import java.util.Arrays;
import java.util.List;

import core.utils.Tag;

public enum Trait implements Tag {
    UNDEFINED("UNDEFINED", 0, "UNDEFINED"),
    KING_GUARD("Королевский страж", 1, "Все существа получают [+2 / +3 / +5] атаки", 2, 4, 6),
    POISONOUS("Ядовитый", 2, "Ядовитые получают [+1 / +2 / +3] яда при атаке", 2, 3, 4),
    DEMON("Демон", 3, "Демоны получают ???", 2, 4),
    ROBOT("Робот", 4, "На время боя роботы получают случайные усиления [1 / 3 / 7] раз из следующих:\n+5 здоровья, +2 атаки, +1 физической зашиты", 1, 2, 3),
    FROSTBORN("Хладорождённый", 5, "Хладорождённые получают ???", 1, 2, 3),

    WARRIOR("Воин", 12, "Воины получают [+1 / +2 / +3] атаки и [+1 / +2 / +3] физической защиты", 2, 3, 4),
    ASSASSIN("Ассасин", 13, "Ассасины получают [+2 / +3 / +4] атаки и [+10 / +20 / +40] скорости", 2, 3, 4),
    DEFENDER("Защитник", 14, ""),
    MAGE("Маг", 15, "Маги получают [+4 / +7 / +10] силы умений", 2, 3, 4),
    EATER("Обжора", 16, "Обжоры навсегда получают [+4 / +6 / +10] здоровья перед боем", 2, 4, 5);

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

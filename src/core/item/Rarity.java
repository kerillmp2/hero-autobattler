package core.item;

public enum Rarity {
    UNDEFINED("Undefined", "Undef", 0),
    COMMON("Common", "C", 1),
    UNCOMMON("Uncommon", "U", 2),
    RARE("Rare", "R", 3),
    EPIC("Epic", "SR", 4),
    LEGENDARY("Legendary", "SSR", 5);

    private String name;
    private String shortName;
    private int value;

    Rarity(String name, String shortName, int value) {
        this.name = name;
        this.shortName = shortName;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public int getValue() {
        return value;
    }

    public static Rarity byValue(int value) {
        for (Rarity rarity : Rarity.values()) {
            if (rarity.getValue() == value) {
                return rarity;
            }
        }
        return UNDEFINED;
    }
}

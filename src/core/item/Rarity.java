package core.item;

public enum Rarity {
    UNDEFINED("Undefined", "Undef"),
    COMMON("Common", "C"),
    Rare("Rare", "R"),
    EPIC("Epic", "SR"),
    LEGENDARY("Legendary", "SSR");

    private String name;
    private String shortName;

    Rarity(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}

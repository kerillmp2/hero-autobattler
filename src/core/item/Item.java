package core.item;

import core.creature.Creature;

public class Item {
    String name;
    String description;
    Rarity rarity;
    EquipAction equipAction;
    private int value;

    private Item(String name, String description, Rarity rarity, EquipAction equipAction, int value) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.equipAction = equipAction;
        this.value = value;
    }

    public static Item newItem(String name, String description, Rarity rarity, EquipAction equipAction) {
        int value = 0;
        switch (rarity) {
            case COMMON:
                value = 1;
                break;
            case UNCOMMON:
                value = 3;
                break;
            case RARE:
                value = 9;
                break;
            case EPIC:
                value = 12;
                break;
            case LEGENDARY:
                value = 20;
        }
        return new Item(name, description, rarity, equipAction, value);
    }

    public void equipOn(Creature creature) {
        equipAction.onEquip(creature);
        creature.addItem(this);
    }

    public String getName() {
        return name;
    }

    public String getNameWithRarity() {
        return name + " [" + rarity.getShortName() + "]";
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getValue() {
        return value;
    }
}

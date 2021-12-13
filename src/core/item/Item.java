package core.item;

import core.creature.Creature;

public class Item {
    String name;
    String description;
    Rarity rarity;
    EquipAction equipAction;

    public Item(String name, String description, Rarity rarity, EquipAction equipAction) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.equipAction = equipAction;
    }

    public void equipOn(Creature creature) {
        equipAction.onEquip(creature);
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
}

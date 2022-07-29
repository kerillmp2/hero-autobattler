package core.item;

import core.creature.Creature;

public class Item {
    String name;
    String description;
    Rarity rarity;
    EquipAction equipAction;
    private int value;
    boolean hasView = true;
    boolean inAIPool = true;

    private Item(String name, String description, Rarity rarity, EquipAction equipAction, int value, boolean hasView, boolean inAIPool) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.equipAction = equipAction;
        this.value = value;
        this.hasView = hasView;
        this.inAIPool = inAIPool;
    }

    public static Item newItem(String name, String description, Rarity rarity, EquipAction equipAction, boolean hasView, boolean inAIPool) {
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
        return new Item(name, description, rarity, equipAction, value, hasView, inAIPool);
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

    public boolean hasView() {
        return hasView;
    }

    public boolean inAIPool() {
        return inAIPool;
    }
}

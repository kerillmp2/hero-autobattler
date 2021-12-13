package core.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import core.creature.Creature;
import core.item.Item;
import core.item.ItemFactory;
import core.traits.Trait;

public class ItemController {

    public static List<Item> getItemsFor(Creature creature, int level, int amount) {
        List<Item> pool = new ArrayList<>(getItemsFor(level));
        for (Trait trait : creature.getTraitContainer().getTags()) {
            pool.addAll(getItemsFor(trait, level));
        }
        Collections.shuffle(pool);
        amount = Math.min(pool.size(), amount);
        return pool.subList(0, amount);
    }

    public static List<Item> getItemsFor(Trait trait, int level) {
        return ItemFactory.getItemsForTrait(trait, level);
    }

    public static List<Item> getItemsFor(int level) {
        return ItemFactory.getItemsForLevel(level);
    }
}

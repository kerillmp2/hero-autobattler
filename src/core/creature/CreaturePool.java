package core.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.shop.ShopItem;

public class CreaturePool {
    private static final List<Creature> creaturePool = new ArrayList<>();

    public static void removeCreature(Creature creature) {
        creaturePool.remove(creature);
    }

    public static void addCreature(Creature creature) {
        creaturePool.add(creature);
    }

    public static List<Creature> getCreatures() {
        return creaturePool;
    }

    public static List<ShopItem<Creature>> toShopItems() {
        return creaturePool.stream().map(creature -> new ShopItem<>(creature, creature.getCost())).collect(Collectors.toList());
    }
}

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

    public static void init() {
        final int firstLevelAmount = 10;

        for (int i = 0; i < firstLevelAmount; i++) {
            creaturePool.addAll(getCreaturesWithCost(1));
        }
    }

    public static List<Creature> getCreaturesWithCost(int cost) {
        List<Creature> creatures = new ArrayList<>();
        if (cost == 1) {
            creatures.add(CreatureFactory.dunkan());
            creatures.add(CreatureFactory.salvira());
            creatures.add(CreatureFactory.ignar());
            creatures.add(CreatureFactory.warbot());
            creatures.add(CreatureFactory.kodji());
            creatures.add(CreatureFactory.mira());
        }
        return creatures;
    }
}

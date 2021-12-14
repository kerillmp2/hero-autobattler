package core.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return creaturePool.stream().map(creature -> new ShopItem<>(creature, creature.getSellingCost())).collect(Collectors.toList());
    }

    public static void init() {
        Map<Integer, Integer> amountByCost = new HashMap<>();
        amountByCost.put(1, 1);
        amountByCost.put(2, 15);
        amountByCost.put(3, 1);
        amountByCost.put(4, 1);
        amountByCost.put(5, 9);

        for (Map.Entry<Integer, Integer> costAmount : amountByCost.entrySet()) {
            int cost = costAmount.getKey();
            int amount = costAmount.getValue();
            for (int i = 0; i < amount; i++) {
                creaturePool.addAll(getCreaturesWithCost(cost));
            }
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
        if (cost == 2) {
            creatures.add(CreatureFactory.dummy());
        }
        return creatures;
    }
}

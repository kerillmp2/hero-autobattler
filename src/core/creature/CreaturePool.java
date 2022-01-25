package core.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.shop.ShopItem;

public class CreaturePool {
    private final List<Creature> creaturePool;

    private CreaturePool(List<Creature> creaturePool) {
        this.creaturePool = creaturePool;
    }

    public static CreaturePool forPlayer() {
        return new CreaturePool(initForPlayer());
    }

    public void removeCreature(Creature creature) {
        creaturePool.remove(creature);
    }

    public void addCreature(Creature creature) {
        creaturePool.add(creature);
    }

    public List<Creature> getCreatures() {
        return creaturePool;
    }

    public List<ShopItem<Creature>> toShopItems() {
        return creaturePool.stream().map(creature -> new ShopItem<>(creature, creature.getSellingCost())).collect(Collectors.toList());
    }

    private static List<Creature> initForPlayer() {
        Map<Integer, Integer> amountByCost = new HashMap<>();
        List<Creature> creatures = new ArrayList<>();
        amountByCost.put(1, 60);
        amountByCost.put(2, 30);
        amountByCost.put(3, 15);
        amountByCost.put(4, 1);
        amountByCost.put(5, 9);

        for (Map.Entry<Integer, Integer> costAmount : amountByCost.entrySet()) {
            int cost = costAmount.getKey();
            int amount = costAmount.getValue();
            for (int i = 0; i < amount; i++) {
                creatures.addAll(getPlayerCreaturesWithCost(cost));
            }
        }
        return creatures;
    }

    public static List<Creature> getPlayerCreaturesWithCost(int cost) {
        List<Creature> creatures = new ArrayList<>();
        if (cost == 1) {
            creatures.add(CreatureFactory.dunkan());
            creatures.add(CreatureFactory.salvira());
            creatures.add(CreatureFactory.ignar());
            creatures.add(CreatureFactory.warbot());
            creatures.add(CreatureFactory.kodji());
            creatures.add(CreatureFactory.obby());
            creatures.add(CreatureFactory.leto());
            creatures.add(CreatureFactory.annie());
            creatures.add(CreatureFactory.bolver());
            creatures.add(CreatureFactory.shaya());
            creatures.add(CreatureFactory.mira());
            //creatures.add(CreatureFactory.dummy());
        }
        if (cost == 2) {
            creatures.add(CreatureFactory.rover());
            creatures.add(CreatureFactory.cathyra());
            creatures.add(CreatureFactory.coldy());
            creatures.add(CreatureFactory.jack());
            creatures.add(CreatureFactory.aralis());
            creatures.add(CreatureFactory.weiss());
            creatures.add(CreatureFactory.aramis());
        }
        return creatures;
    }
}

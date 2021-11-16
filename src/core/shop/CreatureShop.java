package core.shop;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;
import core.utils.RandomController;

public class CreatureShop extends Shop<Creature> {
    private CreatureShop(List<ShopItem<Creature>> itemPool, int shopLineLength, int shopLevel) {
        super(itemPool, shopLineLength, shopLevel);
    }

    public static CreatureShop defaultCreatureShop(Player player) {
        return new CreatureShop(
                CreaturePool.toShopItems(),
                5,
                player.getCreatureShopLevel()
        );
    }

    private List<ShopItem<Creature>> filterPoolByCost(int cost) {
        return itemPool.stream().filter(item -> item.getItem().getCost() == cost).collect(Collectors.toList());
    }

    @Override
    protected ShopLine<Creature> generateLine(int length) {
        ShopLine<Creature> shopLine = new ShopLine<>();
        Map<Integer, List<ShopItem<Creature>>> creaturesByCost =  new TreeMap<>();
        creaturesByCost.put(1, filterPoolByCost(1));
        creaturesByCost.put(2, filterPoolByCost(2));
        creaturesByCost.put(3, filterPoolByCost(3));
        creaturesByCost.put(4, filterPoolByCost(4));
        creaturesByCost.put(5, filterPoolByCost(5));
        creaturesByCost.values().forEach(Collections::shuffle);

        Map<Integer, Integer> inlineCostCounter = new TreeMap<>();
        inlineCostCounter.put(1, 0);
        inlineCostCounter.put(2, 0);
        inlineCostCounter.put(3, 0);
        inlineCostCounter.put(4, 0);
        inlineCostCounter.put(5, 0);

        int lineSize = Math.min(length, itemPool.size());
        for (int i = 0; i < lineSize; i++) {
            int creatureCost = generateCostOfCreature();
            int costCounter = inlineCostCounter.get(creatureCost);
            List<ShopItem<Creature>> creatures = creaturesByCost.get(creatureCost);
            // Можем ли взять из пула ещё одно существо стоимостью creatureCost
            if (costCounter + 1 < creatures.size()) {
                ShopItem<Creature> shopCreature = creatures.get(costCounter + 1);
                shopLine.items.add(shopCreature);
                inlineCostCounter.put(creatureCost, costCounter + 1);
            } else {
                i--;
            }
        }
        return shopLine;
    }

    @Override
    public void regenerate() {
        this.itemPool = CreaturePool.toShopItems();
    }

    @Override
    public void changeItemToDummy(int index) {
        if (index < currentLine.items.size()) {
            currentLine.items.set(index, ShopItem.creatureDummy());
        }
    }

    private int generateCostOfCreature() {
        Map<Integer, Integer> chances = getChances();
        int randomInt = RandomController.randomInt(1, 100, true);
        int counter = 0;
        for (int i = 1; i <= 5; i++) {
            counter += chances.get(i);
            if (randomInt <= counter) {
                return i;
            }
        }
        return 1;
    }

    private Map<Integer, Integer> getChances() {
        Map<Integer, Integer> chances = new TreeMap<>();
        chances.put(1, 0);
        chances.put(2, 0);
        chances.put(3, 0);
        chances.put(4, 0);
        chances.put(5, 0);

        switch (this.shopLevel) {
            case 1:
                chances.put(1, 100);
                break;
            case 2:
                chances.put(1, 75);
                chances.put(2, 25);
                break;
            case 3:
                chances.put(1, 55);
                chances.put(2, 30);
                chances.put(3, 15);
                break;
            case 4:
                chances.put(1, 45);
                chances.put(2, 30);
                chances.put(3, 20);
                chances.put(4, 5);
                break;
            case 5:
                chances.put(1, 30);
                chances.put(2, 35);
                chances.put(3, 25);
                chances.put(4, 10);
                break;
            case 6:
                chances.put(1, 19);
                chances.put(2, 35);
                chances.put(3, 30);
                chances.put(4, 15);
                chances.put(5, 1);
                break;
            case 7:
                chances.put(1, 14);
                chances.put(2, 20);
                chances.put(3, 35);
                chances.put(4, 25);
                chances.put(5, 6);
                break;
            case 8:
                chances.put(1, 10);
                chances.put(2, 15);
                chances.put(3, 30);
                chances.put(4, 30);
                chances.put(5, 15);
                break;
            default:
                chances.put(1, 100);
        }

        return chances;
    }
}

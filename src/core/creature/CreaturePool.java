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
        CreaturePool.addCreature(
                Creature.withStats("Воин из степей", 6, 4, 2, 1, 1, 10, 1)
        );
        for (int i = 0; i < 5; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Воин", 5, 3, 2, 2, 1, 10, 1)
            );
        }
        for (int i = 0; i < 2; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Щитоносец", 7, 2, 3, 1, 1, 12, 2)
            );
        }
        Creature rogue = new Creature ("Разбойник", 4, 1, 1, 2, 1, 5, 2, CreatureTag.HAVE_BASIC_ATTACK);
        rogue.addTagValue(CreatureTag.POISONOUS, 1);
        for (int i = 0; i < 2; i++) {
            CreaturePool.addCreature(
                    rogue
            );
        }
    }
}

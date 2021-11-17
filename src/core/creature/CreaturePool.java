package core.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.shop.ShopItem;
import core.traits.Trait;

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
        for (int i = 0; i < 10; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Кекес", 10, 4, 2, 1, 1, 10, 1).wrapTrait(Trait.KING_GUARD)
            );
        }
        for (int i = 0; i < 10; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Дункан", 5, 3, 2, 2, 1, 10, 1).wrapTrait(Trait.KING_GUARD).wrapTrait(Trait.WARRIOR)
            );
        }
        for (int i = 0; i < 2; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Щит", 7, 2, 3, 1, 1, 12, 2).wrapTrait(Trait.KING_GUARD)
            );
        }
        Creature rogue = new Creature ("Сальвира", 4, 1, 1, 2, 1, 5, 2, CreatureTag.HAVE_BASIC_ATTACK).wrapTrait(Trait.ASSASSIN);
        rogue.addTagValue(CreatureTag.POISONOUS, 1);
        for (int i = 0; i < 2; i++) {
            CreaturePool.addCreature(
                    rogue
            );
        }
    }
}

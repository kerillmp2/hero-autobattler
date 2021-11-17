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
        final int firstLevelAmount = 10;

        Creature dummy = new Creature ("DUMMY", 100, 0, 1, 1, 1, 20, 1);
        for (int i = 0; i < firstLevelAmount; i++) {
            CreaturePool.addCreature(dummy);
        }

        //Дункан [Королевский страж, Воин]
        for (int i = 0; i < firstLevelAmount; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Дункан", 10, 4, 2, 2, 1, 10, 1).wrapTrait(Trait.KING_GUARD).wrapTrait(Trait.WARRIOR)
            );
        }

        //Сальвира [Ядовитый, Ассасин]
        Creature salvira = new Creature ("Сальвира", 7, 2, 1, 1, 1, 6, 1, CreatureTag.HAVE_BASIC_ATTACK).wrapTrait(Trait.ASSASSIN).wrapTrait(Trait.POISONOUS);
        salvira.addTagValue(CreatureTag.POISONOUS, 1);
        for (int i = 0; i < firstLevelAmount; i++) {
            CreaturePool.addCreature(salvira);
        }

        //Игнар [Демон, Обжора]
        Creature ignar = new Creature ("Игнар", 12, 1, 2, 2, 1, 15, 1, CreatureTag.HAVE_BASIC_ATTACK).wrapTrait(Trait.DEMON).wrapTrait(Trait.EATER);
        for (int i = 0; i < firstLevelAmount; i++) {
            CreaturePool.addCreature(ignar);
        }

        //Игнар [Демон, Обжора]
        Creature bolwar = new Creature ("Болвар", 5, 1, 2, 2, 1, 15, 1, CreatureTag.HAVE_BASIC_ATTACK).wrapTrait(Trait.DEMON).wrapTrait(Trait.EATER);
        for (int i = 0; i < firstLevelAmount; i++) {
            CreaturePool.addCreature(bolwar);
        }
    }
}

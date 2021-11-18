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

        for (int i = 0; i < firstLevelAmount; i++) {
            Creature dummy = new Creature ("DUMMY", 100, 0, 1, 1, 1, 20, 1);
            CreaturePool.addCreature(dummy);
        }

        //Дункан [Королевский страж, Воин]
        for (int i = 0; i < firstLevelAmount; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Дункан", 10, 4, 1, 0, 1, 10, 1).wrapTrait(Trait.KING_GUARD).wrapTrait(Trait.WARRIOR)
            );
        }

        //Сальвира [Ядовитый, Ассасин]
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature salvira = new Creature ("Сальвира", 7, 2, 0, 0, 1, 6, 1, CreatureTag.HAVE_BASIC_ATTACK).wrapTrait(Trait.ASSASSIN).wrapTrait(Trait.POISONOUS);
            salvira.addTagValue(CreatureTag.POISONOUS, 1);
            CreaturePool.addCreature(salvira);
        }

        //Игнар [Демон, Обжора]
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature ignar = new Creature ("Игнар", 12, 1, 1, 1, 1, 15, 1, CreatureTag.HAVE_BASIC_ATTACK).wrapTrait(Trait.DEMON).wrapTrait(Trait.EATER);
            CreaturePool.addCreature(ignar);
        }

        //Баобот [Робот, Воин]
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature baobot = new Creature ("Баобот", 10, 3, 2, 0, 1, 12, 1, CreatureTag.HAVE_BASIC_ATTACK).wrapTrait(Trait.ROBOT).wrapTrait(Trait.WARRIOR);
            CreaturePool.addCreature(baobot);
        }
    }
}

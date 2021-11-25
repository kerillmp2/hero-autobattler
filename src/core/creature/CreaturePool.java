package core.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.creature.skills.CreatureSkillFactory;
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

        //Тестовое существо
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature dummy = new Creature ("DUMMY", 100, 0, 1, 1, 1, 200, 1, 1, CreatureTag.HAVE_BASIC_ATTACK)
                    .wrapTrait(Trait.ROBOT);
            CreaturePool.addCreature(dummy);
        }

        //Дункан [Королевский страж, Воин]
        for (int i = 0; i < firstLevelAmount; i++) {
            CreaturePool.addCreature(
                    Creature.withStats("Дункан", 30, 6, 2, 0, 0, 100, 100, 1)
                            .wrapTrait(Trait.KING_GUARD)
                            .wrapTrait(Trait.WARRIOR)
                            .wrapSkill(CreatureSkillFactory.dunkanSkill())
            );
        }

        //Сальвира [Ядовитый, Ассасин]
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature salvira = new Creature ("Сальвира", 26, 4, 0, 0, 2, 90, 100, 1, CreatureTag.HAVE_BASIC_ATTACK)
                    .wrapTrait(Trait.ASSASSIN)
                    .wrapTrait(Trait.POISONOUS)
                    .wrapSkill(CreatureSkillFactory.salviraSkill());
            salvira.addTagValue(CreatureTag.POISONOUS, 1);
            CreaturePool.addCreature(salvira);
        }

        //Игнар [Демон, Обжора]
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature ignar = new Creature ("Игнар", 45, 5, 0, 1, 5, 120, 100, 1, CreatureTag.HAVE_BASIC_ATTACK)
                    .wrapTrait(Trait.DEMON)
                    .wrapTrait(Trait.EATER)
                    .wrapSkill(CreatureSkillFactory.ignarSkill());
            CreaturePool.addCreature(ignar);
        }

        //Варбот [Робот, Воин]
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature warbot = new Creature ("Варбот", 31, 5, 2, 0, 2, 110, 100, 1, CreatureTag.HAVE_BASIC_ATTACK)
                    .wrapTrait(Trait.ROBOT)
                    .wrapTrait(Trait.WARRIOR)
                    .wrapSkill(CreatureSkillFactory.warbotSkill());
            CreaturePool.addCreature(warbot);
        }

        //Коджи [Хладорождённый, Маг]
        for (int i = 0; i < firstLevelAmount; i++) {
            Creature kodji = new Creature ("Коджи", 20, 3, 1, 3, 5, 95, 30, 1, CreatureTag.HAVE_BASIC_ATTACK)
                    .wrapTrait(Trait.FROSTBORN)
                    .wrapTrait(Trait.MAGE)
                    .wrapSkill(CreatureSkillFactory.kodjiSkill());
            CreaturePool.addCreature(kodji);
        }
    }
}

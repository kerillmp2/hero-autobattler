package core.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;
import core.controllers.utils.RandomController;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.creature.stat.StatsContainer;

import static core.controllers.utils.RandomController.*;
import static core.creature.stat.Stat.*;

public class ItemFactory {

    //All creatures
    public static Item snowball() {
        int throwingDamage = randomInt(4, 6, true);
        return ItemTemplate.empty().withName("Snowball").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(2, 3, true), false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_END_TURN)
                        .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY, throwingDamage)
                        .withPrefix("%s throws snowball at %s!").build())
                .addDescription("At the end of the turn throw snowball that deals %d physical damage to random enemy").addValue(throwingDamage)
                .build();
    }

    public static Item ironKnife() {
        return ItemTemplate.empty().withName("Iron Knife").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, randomInt(2, 4, true), false)
                .withStatChange(SPEED, randomInt(8, 14, true), false)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironSword() {
        return ItemTemplate.empty().withName("Iron sword").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, randomInt(4, 6, true), false)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironStaff() {
        int manaGain = RandomController.randomInt(1, 3, true);
        return ItemTemplate.empty().withName("Iron staff").withRarity(Rarity.COMMON)
                .withStatChange(SPELL_POWER, randomInt(1, 3, true), false)
                .withAction(ActionFactory.addManaAction(null, manaGain, ResolveTime.AFTER_ATTACK))
                .addDescription("+%d Mana after attack").addValue(manaGain)
                .withRandomStatChange(1)
                .build();
    }

    public static Item oldBook() {
        int manaGain = RandomController.randomInt(2, 5, true);
        return ItemTemplate.empty().withName("Old book").withRarity(Rarity.UNCOMMON)
                .withRandomStatChange(1)
                .addDescription("+%d Mana after attack").addValue(manaGain)
                .withAction(ActionFactory.addManaAction(null, manaGain, ResolveTime.AFTER_ATTACK))
                .build();
    }

    public static Item ironArmor() {
        return ItemTemplate.empty().withName("Iron armor").withRarity(Rarity.COMMON)
                .withStatChange(HP, randomInt(20, 28, true), false)
                .withStatChange(PHYSICAL_ARMOR, randomInt(1, 3, true), false)
                .withStatChange(SPEED, (-1) * randomInt(10, 15, true), true)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironShield() {
        return ItemTemplate.empty().withName("Iron shield").withRarity(Rarity.COMMON)
                .withStatChange(Stat.HP, RandomController.randomInt(17, 24, true), false)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(1, 2, true), false)
                .withRandomStatChange(1)
                .build();
    }

    public static Item leatherBoots() {
        return ItemTemplate.empty().withName("Leather Boots").withRarity(Rarity.COMMON)
                .withStatChange(Stat.SPEED, RandomController.randomInt(7, 15, true), true)
                .withStatChange(Stat.PHYSICAL_ARMOR, 1, false)
                .withStatChange(Stat.MAGIC_ARMOR, 1, 2, false)
                .build();
    }

    public static Item leatherCloak() {
        return ItemTemplate.empty().withName("Leather cloak").withRarity(Rarity.COMMON)
                .withStatChange(Stat.HP, 15, 21, false)
                .withStatChange(Stat.MAGIC_ARMOR, 2, 3, false)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironLance() {
        int speedAfterAttackAmount = randomInt(4, 7, true);
        return ItemTemplate.empty().withName("Iron lance").withRarity(Rarity.UNCOMMON)
                .withStatChange(Stat.ATTACK, RandomController.randomInt(3, 5, true), false)
                .withRandomStatChange(1)
                .withAction(ActionFactory.addStatAction(null, SPEED, speedAfterAttackAmount, ResolveTime.AFTER_ATTACK))
                .addDescription("+%d Speed after attack").addValue(speedAfterAttackAmount)
                .build();
    }

    public static Item emeraldAmulet() {
        return ItemTemplate.empty().withName("Emerald amulet").withRarity(Rarity.UNCOMMON)
                .withStatChange(Stat.HP, RandomController.randomInt(12, 16, true), true)
                .withRandomStatChange(2)
                .build();
    }

    public static Item topazAmulet() {
        return ItemTemplate.empty().withName("Topaz amulet").withRarity(Rarity.UNCOMMON)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(2, 4, true), false)
                .withStatChange(Stat.MAGIC_ARMOR, RandomController.randomInt(2, 4, true), false)
                .withRandomStatChange(2)
                .build();
    }

    public static Item sapphireAmulet() {
        int manaGain = RandomController.randomInt(2, 4, true);
        return ItemTemplate.empty().withName("Sapphire amulet").withRarity(Rarity.UNCOMMON)
                .withStatChange(Stat.MANA, (-1) * RandomController.randomInt(10, 15, true), true)
                .withRandomStatChange(2)
                .withAction(ActionFactory.addManaAction(null, manaGain, ResolveTime.AFTER_ATTACK))
                .addDescription("+%d Mana after attack").addValue(manaGain)
                .build();
    }

    public static Item rubyAmulet() {
        return ItemTemplate.empty().withName("Ruby amulet").withRarity(Rarity.UNCOMMON)
                .withStatChange(Stat.ATTACK, RandomController.randomInt(16, 22, true), true)
                .withStatChange(Stat.HP, RandomController.randomInt(7, 10, true), false)
                .withRandomStatChange(2)
                .build();
    }

    public static Item repairKit() {
        int hpRegen = randomInt(8, 11, true);
        return ItemTemplate.empty().withName("Repair kit").withRarity(Rarity.UNCOMMON)
                .withStatChange(HP, randomInt(10, 12), true)
                .withRandomStatChange(1)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                .wrapTag(ActionTag.HEAL_PERCENT_OF_MISSING, hpRegen).build())
                .addDescription("Heals %d%% of missing health at the start of the turn").addValue(hpRegen)
                .build();
    }

    public static Item backpack() {
        return ItemTemplate.empty().withName("Backpack").withRarity(Rarity.RARE)
                .withStatChange(HP, 15, 20, false)
                .withStatChange(ATTACK, 1, 2, false)
                .withStatChange(SPELL_POWER, 2, 3, false)
                .withStatChange(PHYSICAL_ARMOR, 1, 2, false)
                .withStatChange(MAGIC_ARMOR, 1, 3, false)
                .withStatChange(SPEED, 5, 10, false)
                .withRandomStatChange(4)
                .build();
    }

    public static Item poisonBlade() {
        int poisonAmount = randomInt(2, 3, true);
        return ItemTemplate.empty().withName("Poison blade").withRarity(Rarity.RARE)
                .withStatChange(ATTACK, randomInt(19, 25), true)
                .withStatChange(SPEED, randomInt(10, 15), true)
                .addDescription("+%d Poison").addValue(poisonAmount)
                .buildWithAction(c -> c.addTagValue(CreatureTag.POISONOUS, poisonAmount));
    }

    public static Item ironHeart() {
        int armorGain = randomInt(1, 2, true);
        return ItemTemplate.empty().withName("Iron heart").withRarity(Rarity.EPIC)
                .withStatChange(PHYSICAL_ARMOR, randomInt(1, 3, true), false)
                .withStatChange(MAGIC_ARMOR, randomInt(2, 4, true), false)
                .addDescription("+%d physical armor after taking physical damage").addValue(armorGain)
                .withAction(ActionFactory.addStatAction(null, PHYSICAL_ARMOR, armorGain, ResolveTime.AFTER_TAKING_PHYSICAL_DAMAGE))
                .build();
    }

    public static Item rageModule() {
        int attackGain = randomInt(7, 12, true);
        return ItemTemplate.empty().withName("Rage module").withRarity(Rarity.EPIC)
                .withStatChange(ATTACK, 15, 18, true)
                .withStatChange(HP, 10, 15, true)
                .withRandomStatChange(2)
                .withAction(ActionFactory.addStatAction(null, ATTACK, attackGain, ResolveTime.AFTER_DEALING_DAMAGE, true))
                .addDescription("+%d%% attack damage after dealing damage").addValue(attackGain)
                .build();
    }

    public static Item mindModule() {
        int manaOnAttack = RandomController.randomInt(4, 8, true);
        int manaOnDamage = RandomController.randomInt(3, 6, true);
        return ItemTemplate.empty().withName("Mind module").withRarity(Rarity.EPIC)
                .withRandomStatChange(3)
                .withStatChange(Stat.MANA, (-1) * RandomController.randomInt(24, 27, true), true)
                .addDescription("+%d Mana after attack").addValue(manaOnAttack)
                .withAction(ActionFactory.addManaAction(null, manaOnAttack, ResolveTime.AFTER_ATTACK))
                .addDescription("+%d Mana after taking damage").addValue(manaOnDamage)
                .withAction(ActionFactory.addManaAction(null, manaOnDamage, ResolveTime.AFTER_TAKING_DAMAGE))
                .build();
    }

    public static Item heatModule() {
        int damageAmount = 20;
        int addAttack = randomInt(3, 5);
        int addSpeed = randomInt(7, 10);
        return ItemTemplate.empty().withName("Heat module").withRarity(Rarity.EPIC)
                .withRandomStatChange(3)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.TAKE_BASIC_DAMAGE, damageAmount).build())
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.ATTACK, addAttack).wrapTag(ActionTag.ADD_FLOAT_STAT).build())
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.SPEED, addSpeed).wrapTag(ActionTag.ADD_FLOAT_STAT).build())
                .addDescription("When turn starts take %d damage, gain %d attack and %d speed")
                .addValue(damageAmount).addValue(addAttack).addValue(addSpeed)
                .build();
    }

    public static Item throwingDagger() {
        int throwingDamagePercent = randomInt(30, 35, true);
        int throwingNumber = randomInt(2, 3, true);
        ItemTemplate itemTemplate = ItemTemplate.empty().withName("Throwing daggers").withRarity(Rarity.EPIC)
                .withStatChange(ATTACK, randomInt(15, 20), true)
                .withStatChange(SPEED, randomInt(10, 15), true)
                .addDescription("After attack throws %d daggers, each dagger deals %d%% damage to random enemy")
                .addValue(throwingNumber).addValue(throwingDamagePercent);
        for (int i = 0; i < throwingNumber; i++) {
            itemTemplate
                    .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACK)
                            .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY, throwingDamagePercent)
                            .wrapTag(ActionTag.PERCENTAGE)
                            .withPrefix("%s throws dagger to %s!")
                            .build());
        }
        return itemTemplate.build();
    }

    public static Item dragonRay() {
        int damage = RandomController.randomInt(30, 50, true);
        return ItemTemplate.empty().withName("Dragon ray").withRarity(Rarity.LEGENDARY)
                .addDescription("Deals %d magic damage to each enemy after attack").addValue(damage)
                .withAction(ActionFactory.dealDamageToAllEnemiesAction(null, damage, ResolveTime.AFTER_ATTACK))
                .build();
    }


    private static class ItemTemplate {
        private String name;
        private Rarity rarity;
        private String descriptionTemplate;
        private List<Integer> values;
        private List<StatsContainer.StatChange> statChanges;
        private List<Action> actions;

        private ItemTemplate(String name, Rarity rarity, String descriptionTemplate, List<Integer> values, List<StatsContainer.StatChange> statChanges, List<Action> actions) {
            this.name = name;
            this.rarity = rarity;
            this.descriptionTemplate = descriptionTemplate;
            this.values = values;
            this.statChanges = statChanges;
            this.actions = actions;
        }

        public static ItemTemplate empty() {
            return new ItemTemplate("", Rarity.UNDEFINED, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        public ItemTemplate withName(String name) {
            this.name = name;
            return this;
        }

        public ItemTemplate withDescription(String description) {
            this.descriptionTemplate = description;
            return this;
        }

        public ItemTemplate withAction(Action action) {
            this.actions.add(action);
            return this;
        }

        public ItemTemplate addValue(int value) {
            this.values.add(value);
            return this;
        }

        public ItemTemplate withRarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public ItemTemplate withStatChange(Stat stat, int amount, boolean isPercentage) {
            statChanges.add(new StatsContainer.StatChange(stat, StatChangeSource.PERMANENT, amount, isPercentage));
            return this;
        }

        private String descriptionForStatChange(Stat stat, int amount, boolean isPercentage) {
            String symbol = amount > 0 ? "+" : "";
            String percent = isPercentage ? "%%" : "";
            return symbol + amount + percent + " " + stat.getName();
        }

        public ItemTemplate withStatChange(Stat stat, int from, int to, boolean isPercentage) {
            return withStatChange(stat, randomInt(from, to, true), isPercentage);
        }

        public ItemTemplate withRandomStatChange(int from, int to, boolean isPercentage, Stat... stats) {
            if (stats.length == 0) {
                return this;
            }
            return withStatChange(randomElementOf(stats), from, to, isPercentage);
        }

        public ItemTemplate withRandomStatChange(int value) {
            Map<Stat, Double> buffs = new HashMap<>();
            while (value > 0) {
                int rawAmount = randomInt(1, value, true);
                Stat stat = Stat.getRandomStatExclusive(MANA);
                double currentAmount = buffs.getOrDefault(stat, 0.0);
                double additionalAmount = stat.getValue() * rawAmount;
                buffs.put(stat, currentAmount + additionalAmount);
                value -= rawAmount;
            }
            for (Stat stat : buffs.keySet()) {
                withStatChange(stat, buffs.get(stat).intValue(), false);
            }
            return this;
        }

        public ItemTemplate addDescription(String additionalDescription) {
            String beginDescriptionLine = descriptionTemplate.length() > 0 ? "$ " : "";
            descriptionTemplate += (beginDescriptionLine + additionalDescription);
            return this;
        }

        public Item build() {
            return buildWithAction(c -> {});
        }

        public Item buildWithAction(EquipAction equipAction) {
            Map<Stat, Integer> floatChanges = new TreeMap<>();
            Map<Stat, Integer> percentageChanges = new TreeMap<>();
            List<StatsContainer.StatChange> recalculatedStatChanges = new ArrayList<>();
            for (StatsContainer.StatChange statChange : statChanges) {
                Stat stat = statChange.getStat();
                if (statChange.isPercentage()) {
                    int current = percentageChanges.getOrDefault(stat, 0);
                    percentageChanges.put(stat, current + statChange.getAmount());
                } else {
                    int current = floatChanges.getOrDefault(stat, 0);
                    floatChanges.put(stat, current + statChange.getAmount());
                }
            }
            for (Map.Entry<Stat, Integer> change : floatChanges.entrySet()) {
                recalculatedStatChanges.add(
                        new StatsContainer.StatChange(change.getKey(), StatChangeSource.PERMANENT, change.getValue(), false)
                );
                addDescription(descriptionForStatChange(change.getKey(), change.getValue(), false));
            }
            for (Map.Entry<Stat, Integer> change : percentageChanges.entrySet()) {
                recalculatedStatChanges.add(
                        new StatsContainer.StatChange(change.getKey(), StatChangeSource.PERMANENT, change.getValue(), true)
                );
                addDescription(descriptionForStatChange(change.getKey(), change.getValue(), true));
            }
            return Item.newItem(this.name, String.format(this.descriptionTemplate, values.toArray()), rarity,
                    creature -> {
                        equipAction.onEquip(creature);
                        for (StatsContainer.StatChange statChange : recalculatedStatChanges) {
                            creature.apply(statChange);
                        }
                        for (Action action : actions) {
                            creature.addAction(action);
                        }
                    });
        }
    }
}

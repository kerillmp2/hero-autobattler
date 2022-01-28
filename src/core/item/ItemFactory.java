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
import core.controllers.LevelController;
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
                .withStatChange(HP, randomInt(18, 22, true), false)
                .withStatChange(PHYSICAL_ARMOR, randomInt(1, 3, true), false)
                .withStatChange(SPEED, (-1) * randomInt(10, 15, true), true)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironShield() {
        return ItemTemplate.empty().withName("Iron shield").withRarity(Rarity.COMMON)
                .withStatChange(Stat.HP, RandomController.randomInt(14, 18, true), false)
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
                .withStatChange(Stat.MAGIC_ARMOR, 1, 2, false)
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
                .withStatChange(Stat.HP, RandomController.randomInt(14, 16, true), true)
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

    public static Item repairModule() {
        int hpRegen = 1;
        return ItemTemplate.empty().withName("Repair module").withRarity(Rarity.UNCOMMON)
                .withStatChange(HP, randomInt(15, 22, true), false)
                .withStatChange(PHYSICAL_ARMOR, 1, false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_TAKING_DAMAGE)
                .wrapTag(ActionTag.HEAL_FLOAT, hpRegen).build())
                .addDescription("Heals for %d%% health after taking any damage").addValue(hpRegen)
                .build();
    }

    public static Item poisonBlade() {
        int poisonAmount = randomInt(1, 2, true);
        return ItemTemplate.empty().withName("Poison blade").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(10, 15), true)
                .withStatChange(SPEED, randomInt(10, 15), true)
                .addDescription("+%d Poison").addValue(poisonAmount)
                .buildWithAction(c -> c.addTagValue(CreatureTag.POISONOUS, poisonAmount));
    }

    public static Item potionKit() {
        int bouncesAmount = 1;
        return ItemTemplate.empty().withName("Potion kit").withRarity(Rarity.UNCOMMON)
                .withStatChange(SPELL_POWER, randomInt(10, 13), true)
                .withRandomStatChange(1)
                .addDescription("+%d Skill bounces").addValue(bouncesAmount)
                .buildWithAction(c -> c.addTagValue(CreatureTag.BOUNCING_SKILL, bouncesAmount));
    }

    public static Item fireShard() {
        int burnBuff = randomInt(3, 4, true);
        return ItemTemplate.empty().withName("Fire shard").withRarity(Rarity.UNCOMMON)
                .withStatChange(SPELL_POWER, randomInt(8, 12, true), true)
                .withStatChange(ATTACK, randomInt(7, 10, true), true)
                .withRandomStatChange(1)
                .addDescription("Skills applies +%d Burn on targets").addValue(burnBuff)
                .buildWithAction(c -> c.addTagValue(CreatureTag.BURN_BUFF, burnBuff));
    }

    public static Item demonClaw() {
        int manaBurn = randomInt(3, 5, true);
        return ItemTemplate.empty().withName("Demon claw").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(10, 12, true), true)
                .withStatChange(SPEED, randomInt(10, 15, true), false)
                .withRandomStatChange(1)
                .addDescription("Attacks burns %d targets mana").addValue(manaBurn)
                .buildWithAction(c -> c.addTagValue(CreatureTag.BURN_FLOAT_MANA_ON_ATTACK, manaBurn));
    }

    public static Item chickenLeg() {
        int hpRegen = randomInt(4, 6);
        return ItemTemplate.empty().withName("Chicken leg").withRarity(Rarity.UNCOMMON)
                .withStatChange(HP, randomInt(7, 10), true)
                .withStatChange(ATTACK, randomInt(1, 2), false)
                .withRandomStatChange(1)
                .addDescription("Heals %d%% of max health at the end of the turn").addValue(hpRegen)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.HEAL_PERCENT_OF_MAX, hpRegen).build())
                .build();
    }

    public static Item bronzeFork() {
        int damage = randomInt(2, 3);
        int additionalHpBuff = randomInt(2, 4);
        return ItemTemplate.empty().withName("Bronze fork").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, damage, false)
                .withRandomStatChange(1)
                .withDescription("Gains %d permanent health when battle starts").addValue(additionalHpBuff)
                .buildWithAction(c -> c.addTagValue(CreatureTag.ADD_PERMANENT_FLOAT_HP_BEFORE_BATTLE, additionalHpBuff));
    }

    public static Item frostShard() {
        int damage = randomInt(3, 5);
        return ItemTemplate.empty().withName("Frost shard").withRarity(Rarity.UNCOMMON)
                .withStatChange(SPELL_POWER, randomInt(2, 4), false)
                .withStatChange(MAGIC_ARMOR, 1, false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACK)
                        .wrapTag(ActionTag.BASIC_ATTACK_BUFF).wrapTag(ActionTag.DEAL_MAGIC_DAMAGE, damage).build())
                .addDescription("Attacks deals +%d magic damage").addValue(damage)
                .build();
    }

    public static Item iceShield() {
        int physArmor = randomInt(1, 2);
        int magicArmor = 3 - physArmor + randomInt(0, 1);
        int slow = randomInt(5, 8);
        return ItemTemplate.empty().withName("Ice shield").withRarity(Rarity.UNCOMMON)
                .withStatChange(PHYSICAL_ARMOR, physArmor, false)
                .withStatChange(MAGIC_ARMOR, magicArmor, false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACKED)
                        .wrapTag(ActionTag.BASIC_ATTACK_RESPONSE).wrapTag(ActionTag.REDUCE_FLOAT_STAT).wrapTag(ActionTag.SPEED, slow).build())
                .addDescription("When attacked, attacker slows by %d").addValue(slow)
                .build();
    }

    public static Item backpack() {
        return ItemTemplate.empty().withName("Backpack").withRarity(Rarity.RARE)
                .withStatChange(HP, 9, 12, false)
                .withStatChange(ATTACK, 1, 2, false)
                .withStatChange(SPELL_POWER, 2, 3, false)
                .withStatChange(PHYSICAL_ARMOR, 1, 2, false)
                .withStatChange(MAGIC_ARMOR, 1, 3, false)
                .withStatChange(SPEED, 5, 10, false)
                .withRandomStatChange(2)
                .build();
    }

    public static Item ironHeart() {
        int armorGain = 1;
        return ItemTemplate.empty().withName("Iron heart").withRarity(Rarity.EPIC)
                .withStatChange(PHYSICAL_ARMOR, randomInt(3, 4, true), false)
                .withStatChange(MAGIC_ARMOR, randomInt(2, 4, true), false)
                .addDescription("+%d physical armor after attacked").addValue(armorGain)
                .withAction(ActionFactory.addStatAction(null, PHYSICAL_ARMOR, armorGain, ResolveTime.AFTER_ATTACKED))
                .build();
    }

    public static Item rageModule() {
        int attackGain = randomInt(7, 12, true);
        return ItemTemplate.empty().withName("Rage module").withRarity(Rarity.EPIC)
                .withStatChange(ATTACK, 10, 15, true)
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
                .withRandomStatChange(4)
                .withStatChange(Stat.MANA, (-1) * RandomController.randomInt(24, 27, true), true)
                .addDescription("+%d Mana after attack").addValue(manaOnAttack)
                .withAction(ActionFactory.addManaAction(null, manaOnAttack, ResolveTime.AFTER_ATTACK))
                .addDescription("+%d Mana after taking damage").addValue(manaOnDamage)
                .withAction(ActionFactory.addManaAction(null, manaOnDamage, ResolveTime.AFTER_TAKING_DAMAGE))
                .build();
    }

    public static Item heatModule() {
        int damageAmount = randomInt(20, 25);
        int addAttack = randomInt(10, 14);
        int addSpeed = randomInt(10, 14);
        return ItemTemplate.empty().withName("Heat module").withRarity(Rarity.EPIC)
                .withRandomStatChange(2)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.TAKE_TRUE_DAMAGE, damageAmount).build())
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.ATTACK, addAttack).wrapTag(ActionTag.ADD_PERCENTAGE_STAT).build())
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.SPEED, addSpeed).wrapTag(ActionTag.ADD_FLOAT_STAT).build())
                .addDescription("When turn starts lose %d hp, gain %d%% attack and %d%% speed")
                .addValue(damageAmount).addValue(addAttack).addValue(addSpeed)
                .build();
    }

    public static Item silverFork() {
        int damage = randomInt(10, 13);
        int additionalHpBuff = randomInt(5, 8);
        int magArmor = randomInt(1, 3);
        return ItemTemplate.empty().withName("Silver fork").withRarity(Rarity.EPIC)
                .withStatChange(ATTACK, damage, true)
                .withStatChange(MAGIC_ARMOR, magArmor, false)
                .withDescription("Gains %d permanent health when battle starts").addValue(additionalHpBuff)
                .buildWithAction(c -> c.addTagValue(CreatureTag.ADD_PERMANENT_FLOAT_HP_BEFORE_BATTLE, additionalHpBuff));
    }

    public static Item throwingDagger() {
        int throwingDamagePercent = randomInt(33, 44, true);
        int throwingNumber = 2;
        ItemTemplate itemTemplate = ItemTemplate.empty().withName("Throwing daggers").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, randomInt(12, 15), true)
                .withStatChange(SPEED, randomInt(12, 15), true)
                .addDescription("After attack throws %d daggers, each dagger deals %d%% damage to random enemy")
                .addValue(throwingNumber).addValue(throwingDamagePercent);
        for (int i = 0; i < throwingNumber; i++) {
            itemTemplate
                    .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACK)
                            .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY, throwingDamagePercent)
                            .wrapTag(ActionTag.BASED_ON_STAT, throwingDamagePercent)
                            .wrapTag(ActionTag.ATTACK)
                            .withPrefix("%s throws dagger to %s!")
                            .build());
        }
        return itemTemplate.build();
    }

    public static Item dragonRay() {
        int damage = RandomController.randomInt(13, 15, true);
        return ItemTemplate.empty().withName("Dragon ray").withRarity(Rarity.LEGENDARY)
                .addDescription("Deals %d magic damage to each enemy after attack").addValue(damage)
                .withAction(ActionFactory.dealDamageToAllEnemiesAction(null, damage, ResolveTime.AFTER_ATTACK))
                .withRandomStatChange(6)
                .build();
    }

    public static Item bottomlessBag() {
        int buffs = randomInt(3, 4, true);
        return ItemTemplate.empty().withName("Bottomless Bag").withRarity(Rarity.LEGENDARY)
                .addDescription("Take another artifact!")
                .withRandomStatChange(buffs)
                .buildWithAction(LevelController::selectItem);
    }

    public static Item mythrilFork() {
        int damage = randomInt(16, 20);
        int additionalHpBuff = randomInt(5, 7);
        int magArmor = randomInt(3, 4);
        int damagePercent = randomInt(2, 3);
        return ItemTemplate.empty().withName("Mythril fork").withRarity(Rarity.LEGENDARY)
                .withStatChange(ATTACK, damage, true)
                .withStatChange(MAGIC_ARMOR, magArmor, false)
                .addDescription("Gains %d%% permanent health when battle starts").addValue(additionalHpBuff)
                .addDescription("Attacks deals %d%% of health as magic damage").addValue(damagePercent)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACK)
                        .wrapTag(ActionTag.BASIC_ATTACK_BUFF)
                        .wrapTag(ActionTag.DEAL_MAGIC_DAMAGE)
                        .wrapTag(ActionTag.BASED_ON_STAT, damagePercent)
                        .wrapTag(ActionTag.HP).build())
                .buildWithAction(c -> c.addTagValue(CreatureTag.ADD_PERMANENT_PERCENTAGE_HP_BEFORE_BATTLE, additionalHpBuff));
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

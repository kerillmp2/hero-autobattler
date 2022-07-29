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
import core.controllers.ItemController;
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
        int throwingDamage = randomInt(15, 24, true);
        return ItemTemplate.empty().withName("Snowball").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(5, 8, true), false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_END_TURN)
                        .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY, throwingDamage)
                        .withPrefix("%s throws snowball at %s!").build())
                .addDescription("At the end of the turn throw snowball that deals %d physical damage to random enemy").addValue(throwingDamage)
                .build();
    }

    public static Item ironKnife() {
        return ItemTemplate.empty().withName("Iron Knife").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, randomInt(5, 8, true), false)
                .withStatChange(SPEED, randomInt(8, 14, true), false)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironSword() {
        return ItemTemplate.empty().withName("Iron sword").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, randomInt(7, 10, true), false)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironStaff() {
        int manaGain = RandomController.randomInt(1, 3, true);
        return ItemTemplate.empty().withName("Iron staff").withRarity(Rarity.COMMON)
                .withStatChange(SPELL_POWER, randomInt(4, 9, true), false)
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
                .withStatChange(HP, randomInt(20, 30, true), false)
                .withStatChange(PHYSICAL_ARMOR, randomInt(2, 4, true), false)
                .withStatChange(SPEED, (-1) * randomInt(15, 19, true), true)
                .withRandomStatChange(1)
                .build();
    }

    public static Item ironShield() {
        return ItemTemplate.empty().withName("Iron shield").withRarity(Rarity.COMMON)
                .withStatChange(Stat.HP, RandomController.randomInt(18, 22, true), false)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(3, 4, true), false)
                .withRandomStatChange(1)
                .build();
    }

    public static Item leatherBoots() {
        return ItemTemplate.empty().withName("Leather Boots").withRarity(Rarity.COMMON)
                .withStatChange(Stat.SPEED, RandomController.randomInt(7, 15, true), true)
                .withStatChange(Stat.PHYSICAL_ARMOR, randomInt(2, 3), false)
                .withStatChange(Stat.MAGIC_ARMOR, randomInt(2, 4), false)
                .build();
    }

    public static Item leatherCloak() {
        return ItemTemplate.empty().withName("Leather cloak").withRarity(Rarity.COMMON)
                .withStatChange(Stat.HP, 20, 25, false)
                .withStatChange(Stat.MAGIC_ARMOR, 3, 5, false)
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
                .withStatChange(Stat.HP, RandomController.randomInt(12, 17, true), true)
                .withRandomStatChange(2)
                .build();
    }

    public static Item topazAmulet() {
        return ItemTemplate.empty().withName("Topaz amulet").withRarity(Rarity.UNCOMMON)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(3, 5, true), false)
                .withStatChange(Stat.MAGIC_ARMOR, RandomController.randomInt(4, 6, true), false)
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
                .withStatChange(Stat.ATTACK, RandomController.randomInt(10, 15, true), true)
                .withStatChange(Stat.HP, RandomController.randomInt(10, 15, true), false)
                .withRandomStatChange(2)
                .build();
    }

    public static Item repairModule() {
        int hpRegen = randomInt(1, 2);
        return ItemTemplate.empty().withName("Repair module").withRarity(Rarity.UNCOMMON)
                .withStatChange(HP, randomInt(18, 25, true), false)
                .withStatChange(PHYSICAL_ARMOR, randomInt(1, 2), false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_TAKING_DAMAGE)
                .wrapTag(ActionTag.HEAL_FLOAT, hpRegen).build())
                .addDescription("Heals for %d%% health after taking any damage").addValue(hpRegen)
                .build();
    }

    public static Item poisonBlade() {
        int poisonAmount = randomInt(2, 3);
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
        int burnBuff = randomInt(15, 20, true);
        return ItemTemplate.empty().withName("Fire shard").withRarity(Rarity.UNCOMMON)
                .withStatChange(SPELL_POWER, randomInt(10, 14, true), true)
                .withStatChange(ATTACK, randomInt(7, 10, true), true)
                .withRandomStatChange(1)
                .addDescription("Applies +d%% Burn stacks").addValue(burnBuff)
                .buildWithAction(c -> c.addTagValue(CreatureTag.PERCENTAGE_BURN_BUFF, burnBuff));
    }

    public static Item demonClaw() {
        int manaBurn = randomInt(4, 7, true);
        return ItemTemplate.empty().withName("Demon claw").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(10, 12, true), true)
                .withStatChange(SPEED, randomInt(10, 15, true), false)
                .withRandomStatChange(1)
                .addDescription("Attacks burns %d targets mana").addValue(manaBurn)
                .buildWithAction(c -> c.addTagValue(CreatureTag.BURN_FLOAT_MANA_ON_ATTACK, manaBurn));
    }

    public static Item chickenLeg() {
        int hpRegen = randomInt(3, 5);
        return ItemTemplate.empty().withName("Chicken leg").withRarity(Rarity.UNCOMMON)
                .withStatChange(HP, randomInt(7, 10), true)
                .withStatChange(ATTACK, randomInt(2, 4), false)
                .withRandomStatChange(1)
                .addDescription("Heals %d%% of max health at the end of the turn").addValue(hpRegen)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.HEAL_PERCENT_OF_MAX, hpRegen).build())
                .build();
    }

    public static Item bronzeFork() {
        int damage = randomInt(3, 5);
        int additionalHpBuff = randomInt(4, 6);
        return ItemTemplate.empty().withName("Bronze fork").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, damage, false)
                .withRandomStatChange(1)
                .withDescription("Gains %d permanent health when battle starts").addValue(additionalHpBuff)
                .buildWithAction(c -> c.addTagValue(CreatureTag.ADD_PERMANENT_FLOAT_HP_BEFORE_BATTLE, additionalHpBuff));
    }

    public static Item frostShard() {
        int damage = randomInt(5, 7);
        return ItemTemplate.empty().withName("Frost shard").withRarity(Rarity.UNCOMMON)
                .withStatChange(SPELL_POWER, randomInt(5, 9), false)
                .withStatChange(MAGIC_ARMOR, randomInt(2, 3), false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACK)
                        .wrapTag(ActionTag.BASIC_ATTACK_BUFF).wrapTag(ActionTag.DEAL_MAGIC_DAMAGE, damage).build())
                .addDescription("Attacks deals +%d magic damage").addValue(damage)
                .build();
    }

    public static Item iceShield() {
        int physArmor = randomInt(3, 5);
        int magicArmor = 6 - physArmor + randomInt(0, 1);
        int slow = randomInt(5, 8);
        return ItemTemplate.empty().withName("Ice shield").withRarity(Rarity.UNCOMMON)
                .withStatChange(PHYSICAL_ARMOR, physArmor, false)
                .withStatChange(MAGIC_ARMOR, magicArmor, false)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACKED)
                        .wrapTag(ActionTag.BASIC_ATTACK_RESPONSE).wrapTag(ActionTag.REDUCE_FLOAT_STAT).wrapTag(ActionTag.SPEED, slow).build())
                .addDescription("When attacked, attacker slows by %d").addValue(slow)
                .build();
    }

    public static Item woodenLongBow() {
        int attack = randomInt(15, 20);
        int speed = randomInt(4, 8) - attack;
        return ItemTemplate.empty().withName("Wooden longbow").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(10, 12), true)
                .withStatChange(SPEED, speed, true)
                .withRandomStatChange(1)
                .build();
    }

    public static Item poisonArrows() {
        int poisonAmount = randomInt(2, 3);
        return ItemTemplate.empty().withName("Poison arrows").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(10, 12), true)
                .withStatChange(SPEED, randomInt(12, 15), false)
                .addDescription("+%d Poison").addValue(poisonAmount)
                .buildWithAction(c -> c.addTagValue(CreatureTag.POISONOUS, poisonAmount));
    }

    public static Item fireArrows() {
        int burnBuff = randomInt(5, 7, true);
        return ItemTemplate.empty().withName("Fire arrows").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, randomInt(8, 15), true)
                .withStatChange(SPEED, randomInt(7, 10), false)
                .addDescription("Attacks applies %d burn on targets").addValue(burnBuff)
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_ATTACK)
                        .wrapTag(ActionTag.ADD_BURN, burnBuff)
                        .wrapTag(ActionTag.BASIC_ATTACK_BUFF)
                        .build()));
    }

    public static Item lostSoul() {
        int dodgeIncrease = randomInt(1, 2);
        int speed = randomInt(12, 20) - dodgeIncrease * 5;
        return ItemTemplate.empty().withName("Lost soul").withRarity(Rarity.UNCOMMON)
                .withStatChange(SPEED, speed, false)
                .addDescription("+%d%% chance to dodge attack").addValue(dodgeIncrease)
                .withRandomStatChange(2)
                .build();
    }

    public static Item redSoul() {
        int additionalAttack = randomInt(5, 8);
        return ItemTemplate.empty().withName("Red soul").withRarity(Rarity.RARE)
                .withStatChange(SPEED, randomInt(12, 17), true)
                .withStatChange(ATTACK, randomInt(15, 19), true)
                .addDescription("After creature dodges attack, it gains %d attack").addValue(additionalAttack)
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_DODGE)
                        .wrapTag(ActionTag.ADD_FLOAT_STAT)
                        .wrapTag(ActionTag.ATTACK, additionalAttack)
                        .build()));
    }

    public static Item greenSoul() {
        int hpRegen = randomInt(15, 20);
        return ItemTemplate.empty().withName("Green soul").withRarity(Rarity.RARE)
                .withStatChange(SPEED, randomInt(12, 17), true)
                .withStatChange(HP, randomInt(8, 12), true)
                .addDescription("After creature dodges attack, it restores %d health").addValue(hpRegen)
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_DODGE)
                        .wrapTag(ActionTag.HEAL_FLOAT, hpRegen)
                        .build()));
    }

    public static Item yellowSoul() {
        int barrier = randomInt(14, 22);
        return ItemTemplate.empty().withName("Yellow soul").withRarity(Rarity.RARE)
                .withStatChange(SPEED, randomInt(12, 17), true)
                .withStatChange(PHYSICAL_ARMOR, randomInt(2, 3), false)
                .withStatChange(MAGIC_ARMOR, randomInt(2, 4), false)
                .addDescription("After creature dodges attack, it gains +%d barrier").addValue(barrier)
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_DODGE)
                        .wrapTag(ActionTag.ADD_BARRIER, barrier)
                        .build()));
    }

    public static Item blueSoul() {
        int manaGain = randomInt(20, 25);
        return ItemTemplate.empty().withName("Blue soul").withRarity(Rarity.RARE)
                .withStatChange(SPEED, randomInt(12, 17), true)
                .withStatChange(SPELL_POWER, randomInt(3, 6), false)
                .addDescription("After creature dodges attack, it gains +%d mana").addValue(manaGain)
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.AFTER_DODGE)
                        .wrapTag(ActionTag.ADD_FLOAT_STAT)
                        .wrapTag(ActionTag.MANA, manaGain)
                        .build()));
    }

    public static Item backpack() {
        return ItemTemplate.empty().withName("Backpack").withRarity(Rarity.RARE)
                .withStatChange(HP, 10, 16, false)
                .withStatChange(ATTACK, 2, 3, false)
                .withStatChange(SPELL_POWER, 5, 6, false)
                .withStatChange(PHYSICAL_ARMOR, 2, 3, false)
                .withStatChange(MAGIC_ARMOR, 2, 4, false)
                .withStatChange(SPEED, 5, 10, false)
                .withRandomStatChange(3)
                .build();
    }

    public static Item ironHeart() {
        int armorGain = 2;
        return ItemTemplate.empty().withName("Iron heart").withRarity(Rarity.EPIC)
                .withStatChange(PHYSICAL_ARMOR, randomInt(5, 8, true), false)
                .withStatChange(MAGIC_ARMOR, randomInt(5, 8, true), false)
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
                .withAction(ActionFactory.addStatAction(null, ATTACK, attackGain, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE, true))
                .addDescription("+%d%% attack damage after dealing physical damage").addValue(attackGain)
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
        int damageAmount = randomInt(40, 60);
        int addAttack = randomInt(5, 8) + damageAmount / 10;
        int addSpeed = randomInt(5, 10) + damageAmount / 10;
        return ItemTemplate.empty().withName("Heat module").withRarity(Rarity.EPIC)
                .withRandomStatChange(2)
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.TAKE_TRUE_DAMAGE, damageAmount).build())
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.ATTACK, addAttack).wrapTag(ActionTag.ADD_PERCENTAGE_STAT).build())
                .withAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_START_TURN)
                        .wrapTag(ActionTag.SPEED, addSpeed).wrapTag(ActionTag.ADD_FLOAT_STAT).build())
                .addDescription("When turn starts take %d true damage, gain %d%% attack and %d%% speed")
                .addValue(damageAmount).addValue(addAttack).addValue(addSpeed)
                .build();
    }

    public static Item silverFork() {
        int damage = randomInt(10, 13);
        int additionalHpBuff = randomInt(10, 15);
        int magArmor = randomInt(4, 7);
        return ItemTemplate.empty().withName("Silver fork").withRarity(Rarity.EPIC)
                .withStatChange(ATTACK, damage, true)
                .withStatChange(MAGIC_ARMOR, magArmor, false)
                .withDescription("Gains %d permanent health when battle starts").addValue(additionalHpBuff)
                .buildWithAction(c -> c.addTagValue(CreatureTag.ADD_PERMANENT_FLOAT_HP_BEFORE_BATTLE, additionalHpBuff));
    }

    public static Item throwingDaggers() {
        int throwingNumber = randomInt(1, 3);
        int throwingDamagePercent = randomInt(25, 30) / throwingNumber;
        int attackAmount = randomInt(12, 15) - throwingNumber;
        int speedAmount = randomInt(12, 15) - throwingNumber;
        ItemTemplate itemTemplate = ItemTemplate.empty().withName("Throwing daggers").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, attackAmount, true)
                .withStatChange(SPEED, speedAmount, true)
                .addDescription("After attack throws %d daggers, each dagger deals %d%% damage to random enemy")
                .addValue(throwingNumber).addValue(throwingDamagePercent);
        for (int i = 0; i < throwingNumber; i++) {
            itemTemplate.withAction(ActionFactory.ActionBuilder.empty()
                    .withTime(ResolveTime.AFTER_ATTACK)
                    .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY, throwingDamagePercent)
                    .wrapTag(ActionTag.BASED_ON_STAT, throwingDamagePercent)
                    .wrapTag(ActionTag.ATTACK)
                    .withPrefix("%s throws dagger at %s!")
                    .build());
        }
        return itemTemplate.build();
    }

    public static Item dragonRay() {
        int damage = RandomController.randomInt(25, 35, true);
        return ItemTemplate.empty().withName("Dragon ray").withRarity(Rarity.LEGENDARY)
                .addDescription("Deals %d magic damage to each enemy after attack").addValue(damage)
                .withAction(ActionFactory.dealDamageToAllEnemiesAction(null, damage, ResolveTime.AFTER_ATTACK))
                .withRandomStatChange(8)
                .build();
    }

    public static Item bottomlessBag() {
        int buffs = randomInt(3, 4, true);
        return ItemTemplate.empty().withName("Bottomless Bag").withRarity(Rarity.LEGENDARY)
                .addDescription("Take another artifact!")
                .withRandomStatChange(buffs)
                .removeView()
                .removeFromAIPool()
                .buildWithAction(ItemController::selectItem);
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

    // --------------------------------------------------------------------------------------------------------------------------
    // Pirate treasures
    // --------------------------------------------------------------------------------------------------------------------------
    public static Item seaweed() {
        int floatHpBuff = randomInt(3, 4);
        int additionalMagicArmor = 1;
        int chance = 40 + randomInt(0, 10);
        return ItemTemplate.empty().withName("Seaweed").withRarity(Rarity.COMMON)
                .withStatChange(HP, floatHpBuff, false)
                .addDescription("When battle starts has a %d%% chance to gain %d magic armor until battle ends").addValue(chance).addValue(additionalMagicArmor)
                .removeView()
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_BATTLE_START)
                        .wrapTag(ActionTag.CHANCE, chance)
                        .wrapTag(ActionTag.ADD_FLOAT_STAT)
                        .wrapTag(ActionTag.MAGIC_ARMOR, additionalMagicArmor)
                        .withPrefix("%s eats seaweed!")
                        .build()));
    }

    public static Item turtleShell() {
        int floatHPBuff = randomInt(2, 3);
        int additionalPhysicalArmor = 1;
        int chance = 40 + randomInt(0, 5);
        return ItemTemplate.empty().withName("Turtle shell").withRarity(Rarity.COMMON)
                .withStatChange(HP, floatHPBuff, false)
                .addDescription("When battle starts has a %d%% chance to gain %d physical armor until battle ends").addValue(chance).addValue(additionalPhysicalArmor)
                .removeView()
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_BATTLE_START)
                        .wrapTag(ActionTag.CHANCE, chance)
                        .wrapTag(ActionTag.ADD_FLOAT_STAT)
                        .wrapTag(ActionTag.PHYSICAL_ARMOR, additionalPhysicalArmor)
                        .withPrefix("%s covered by turtle shell!")
                        .build()));
    }

    public static Item bluntKnife() {
        int additionalAttack = randomInt(1, 2);
        int chance = 70 + randomInt(0, 10) - 20 * additionalAttack;
        return ItemTemplate.empty().withName("Blunt knife").withRarity(Rarity.COMMON)
                .addDescription("When battle starts has a %d%% chance to gain %d attack until battle ends").addValue(chance).addValue(additionalAttack)
                .removeView()
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_BATTLE_START)
                        .wrapTag(ActionTag.CHANCE, chance)
                        .wrapTag(ActionTag.ADD_FLOAT_STAT)
                        .wrapTag(ActionTag.ATTACK, additionalAttack)
                        .withPrefix("%s swings with blunt knife!")
                        .build()));
    }

    public static Item emptyBottle() {
        int throwingDamage = randomInt(7, 11);
        int chance = 40 + randomInt(0, 10);
        return ItemTemplate.empty().withName("Empty bottle").withRarity(Rarity.COMMON)
                .addDescription("When battle starts has a %d%% chance to trow bottle at random enemy and deal %d physical damage").addValue(chance).addValue(throwingDamage)
                .removeView()
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_BATTLE_START)
                        .wrapTag(ActionTag.CHANCE, chance)
                        .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY, throwingDamage)
                        .withPrefix("%s throws empty bottle at %s!")
                        .build()));
    }

    public static Item saltyWater() {
        int manaGain = randomInt(3, 6);
        int chance = 55 + randomInt(0, 10) - manaGain * 2;
        return ItemTemplate.empty().withName("Salty water").withRarity(Rarity.COMMON)
                .addDescription("When battle starts has a %d%% chance to gain %d mana").addValue(chance).addValue(manaGain)
                .removeView()
                .buildWithAction(c -> c.addAction(ActionFactory.ActionBuilder.empty().withTime(ResolveTime.ON_BATTLE_START)
                        .wrapTag(ActionTag.CHANCE, chance)
                        .wrapTag(ActionTag.ADD_FLOAT_STAT)
                        .wrapTag(ActionTag.MANA, manaGain)
                        .withPrefix("%s drinks salty water!")
                        .build()));
    }

    public static Item rustyCoin() {
        int magicArmor = 1;
        int chanceIncrease = randomInt(1, 3);
        return ItemTemplate.empty().withName("Rusty coin").withRarity(Rarity.COMMON)
                .withStatChange(MAGIC_ARMOR, magicArmor)
                .addDescription("Increase all current chances by +%d%%").addValue(chanceIncrease)
                .removeView()
                .buildWithAction(c -> c.addTagValue(CreatureTag.INCREASE_CHANCE, chanceIncrease));
    }

    public static Item woodenClub() {
        int attack = 1;
        int chance = 40 + randomInt(0, 10);
        int chanceIncrease = randomInt(1, 3);
        return ItemTemplate.empty().withName("Wooden club").withRarity(Rarity.UNCOMMON)
                .withStatChange(ATTACK, attack)
                .addDescription("Increase all current chances by +%d%%").addValue(chanceIncrease)
                .removeView()
                .buildWithAction(c -> c.addTagValue(CreatureTag.INCREASE_CHANCE, chanceIncrease));
    }

    // --------------------------------------------------------------------------------------------------------------------------
    //
    // --------------------------------------------------------------------------------------------------------------------------


    private static class ItemTemplate {
        private String name;
        private Rarity rarity;
        private String descriptionTemplate;
        private List<Integer> values;
        private List<StatsContainer.StatChange> statChanges;
        private List<Action> actions;
        private boolean hasView;
        private boolean inAIPool;

        private ItemTemplate(String name, Rarity rarity, String descriptionTemplate, List<Integer> values, List<StatsContainer.StatChange> statChanges, List<Action> actions, boolean hasView, boolean inAIPool) {
            this.name = name;
            this.rarity = rarity;
            this.descriptionTemplate = descriptionTemplate;
            this.values = values;
            this.statChanges = statChanges;
            this.actions = actions;
            this.hasView = hasView;
            this.inAIPool = inAIPool;
        }

        public static ItemTemplate empty() {
            return new ItemTemplate("", Rarity.UNDEFINED, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), true, true);
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

        public ItemTemplate withStatChange(Stat stat, int amount) {
            return withStatChange(stat, amount, false);
        }

        public ItemTemplate withStatChange(Stat stat, int amount, boolean isPercentage) {
            if (amount != 0) {
                statChanges.add(new StatsContainer.StatChange(stat, StatChangeSource.PERMANENT, amount, isPercentage));
            }
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

        public ItemTemplate removeView() {
            this.hasView = false;
            return this;
        }

        public ItemTemplate removeFromAIPool() {
            this.inAIPool = false;
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
                    }, this.hasView, this.inAIPool);
        }
    }
}

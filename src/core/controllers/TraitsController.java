package core.controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import core.battlefield.BattlefieldCreature;
import core.battlefield.BattlefieldSide;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreaturePool;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.item.Item;
import core.item.Rarity;
import core.traits.Trait;
import core.traits.TraitContainer;
import utils.Constants;

public class TraitsController {
    private final TraitContainer traitContainer;
    private Rarity piratesTreasure = Rarity.UNDEFINED;

    private TraitsController(TraitContainer traitContainer) {
        this.traitContainer = traitContainer;
    }

    public static TraitsController build() {
        return new TraitsController(new TraitContainer());
    }

    public void updateTraitBuffs(List<Creature> allCreatures) {
        allCreatures.forEach(Creature::clearAllChangesFromTraits);
        setPiratesTreasure(Rarity.UNDEFINED);
        updateKnightBuff(allCreatures);
        updateWarriorBuff(allCreatures);
        updatePoisonousBuff(allCreatures);
        updateEaterBuff(allCreatures);
        updateRobotBuffs(allCreatures);
        updateMagesBuff(allCreatures);
        updateFrostBornBuff(allCreatures);
        updateAssassinBuff(allCreatures);
        updateStudentsBuff(allCreatures);
        updateDefenderBuff(allCreatures);
        updateDemonBuff(allCreatures);
        updateAlchemistsBuff(allCreatures);
        updateDuelistsBuff(allCreatures);
        updateArchersBuff(allCreatures);
        updateFirebornBuff(allCreatures);
        updateSpiritBuff(allCreatures);
        updateBeastsBuff(allCreatures);
        updateSummonersBuff(allCreatures);
        updatePiratesBuff();
    }

    public void updateDefenderBuff(List<Creature> allCreatures) {
        //"All creatures gain [+15% / +30% / +50] physical and magic armor"
        int defendersNum = getTraitValue(Trait.DEFENDER);
        if (defendersNum >= Trait.DEFENDER.getLevels().get(0) && defendersNum < Trait.DEFENDER.getLevels().get(1)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 15, true));
            allCreatures.forEach(c -> c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.DEFENDER_TRAIT, 15, true));
        }
        if (defendersNum >= Trait.DEFENDER.getLevels().get(1) && defendersNum < Trait.DEFENDER.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 30, true));
            allCreatures.forEach(c -> c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.DEFENDER_TRAIT, 30, true));
        }
        if (defendersNum >= Trait.DEFENDER.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 50, true));
            allCreatures.forEach(c -> c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.DEFENDER_TRAIT, 50, true));
        }
    }

    private void updateKnightBuff(List<Creature> allCreatures) {
        // After using skill knight gains barrier equals [10% / 22% / 35%] of max health
        int knight = getTraitValue(Trait.KNIGHT);
        if (knight >= Trait.KNIGHT.getLevels().get(0) && knight < Trait.KNIGHT.getLevels().get(1)) {
            allCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.BARRIER_AFTER_SKILL, StatChangeSource.KNIGHT_TRAIT, 10));
        }
        if (knight >= Trait.KNIGHT.getLevels().get(1) && knight < Trait.KNIGHT.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.BARRIER_AFTER_SKILL, StatChangeSource.KNIGHT_TRAIT, 22));
        }
        if (knight >= Trait.KNIGHT.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.BARRIER_AFTER_SKILL, StatChangeSource.KNIGHT_TRAIT, 35));
        }
    }

    private void updateWarriorBuff(List<Creature> allCreatures) {
        // At the end of the turn warrior gains [+5% / +10% / +15%] attack and [+1 / +3 / +6] physical armor
        int warriorsNum = getTraitValue(Trait.WARRIOR);
        List<Creature> warriors = getCreaturesByTrait(allCreatures, Trait.WARRIOR);
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(0) && warriorsNum < Trait.WARRIOR.getLevels().get(1)) {
            warriors.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERCENTAGE_ATTACK_AFTER_TURN, StatChangeSource.WARRIOR_TRAIT, 5));
            warriors.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_ARMOR_AFTER_TURN, StatChangeSource.WARRIOR_TRAIT, 1));
        }
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(1) && warriorsNum < Trait.WARRIOR.getLevels().get(2)) {
            warriors.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERCENTAGE_ATTACK_AFTER_TURN, StatChangeSource.WARRIOR_TRAIT, 10));
            warriors.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_ARMOR_AFTER_TURN, StatChangeSource.WARRIOR_TRAIT, 3));
        }
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(2)) {
            warriors.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERCENTAGE_ATTACK_AFTER_TURN, StatChangeSource.WARRIOR_TRAIT, 15));
            warriors.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_ARMOR_AFTER_TURN, StatChangeSource.WARRIOR_TRAIT, 6));
        }
    }

    private void updatePoisonousBuff(List<Creature> allCreatures) {
        // Poisonous gains [+3 / +6 / +10] poison
        int poisonousNum = getTraitValue(Trait.POISONOUS);
        List<Creature> poisonousCreatures = getCreaturesByTrait(allCreatures, Trait.POISONOUS);
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(0) && poisonousNum < Trait.POISONOUS.getLevels().get(0)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 3));
        }
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(1) && poisonousNum < Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 6));
        }
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 10));
        }
    }

    private void updateDemonBuff(List<Creature> allCreatures) {
        // After attacking demons burn [12% / 25% / 40%] or target's max mana
        int deomnsNum = getTraitValue(Trait.DEMON);
        List<Creature> demonCreatures = getCreaturesByTrait(allCreatures, Trait.DEMON);
        if (deomnsNum >= Trait.DEMON.getLevels().get(0) && deomnsNum < Trait.DEMON.getLevels().get(1)) {
            demonCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.BURN_PERCENTAGE_MANA_ON_ATTACK, StatChangeSource.DEMON_TRAIT, 12));
        }
        if (deomnsNum >= Trait.DEMON.getLevels().get(1) && deomnsNum < Trait.DEMON.getLevels().get(2)) {
            demonCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.BURN_PERCENTAGE_MANA_ON_ATTACK, StatChangeSource.DEMON_TRAIT, 25));
        }
        if (deomnsNum >= Trait.DEMON.getLevels().get(2)) {
            demonCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.BURN_PERCENTAGE_MANA_ON_ATTACK, StatChangeSource.DEMON_TRAIT, 40));
        }
    }

    private void updateEaterBuff(List<Creature> allCreatures) {
        //EATER: Eaters permanently gains [+3% / +6% / +10%] health before each battle
        int eatersNum = getTraitValue(Trait.EATER);
        List<Creature> eaters = getCreaturesByTrait(allCreatures, Trait.EATER);
        if (eatersNum >= Trait.EATER.getLevels().get(0) && eatersNum < Trait.EATER.getLevels().get(1)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_PERCENTAGE_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 3));
        }
        if (eatersNum >= Trait.EATER.getLevels().get(1) && eatersNum < Trait.EATER.getLevels().get(2)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_PERCENTAGE_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 6));
        }
        if (eatersNum >= Trait.EATER.getLevels().get(2)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_PERCENTAGE_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 10));
        }
    }

    private void updateRobotBuffs(List<Creature> allCreatures) {
        //ROBOT: На время боя роботы получают случайные усиления [1 / 3 / 7] раз из следующих:
        //+7% здоровья, +5% атаки, +2 физической и магической зашиты
        int robotsNum = getTraitValue(Trait.ROBOT);
        List<Creature> robots = getCreaturesByTrait(allCreatures, Trait.ROBOT);
        int numberOfRobotBuffs = 0;
        if (robotsNum >= Trait.ROBOT.getLevels().get(0) && robotsNum < Trait.ROBOT.getLevels().get(0)) {
            numberOfRobotBuffs = 1;
        }
        if (robotsNum >= Trait.ROBOT.getLevels().get(1) && robotsNum < Trait.ROBOT.getLevels().get(2)) {
            numberOfRobotBuffs = 3;
        }
        if (robotsNum >= Trait.ROBOT.getLevels().get(2)) {
            numberOfRobotBuffs = 7;
        }
        for (Creature robot : robots) {
            for (int i = 0; i < numberOfRobotBuffs; i++) {
                Stat stat = Stat.getRandomStatFrom(Stat.HP, Stat.ATTACK, Stat.PHYSICAL_ARMOR);
                if (stat == Stat.HP) {
                    robot.applyCreatureTagChange(CreatureTag.ADD_TEMP_PERCENTAGE_HP_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 7);
                }
                if (stat == Stat.ATTACK) {
                    robot.applyCreatureTagChange(CreatureTag.ADD_TEMP_PERCENTAGE_ATTACK_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 5);
                }
                if (stat == Stat.PHYSICAL_ARMOR) {
                    robot.applyCreatureTagChange(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 2);
                    robot.applyCreatureTagChange(CreatureTag.ADD_TEMP_MARM_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 2);
                }
            }
        }
    }

    private void updateMagesBuff(List<Creature> allCreatures) {
        // Mages gain [+5 / +10 / +20] and [+15% / +30% / +60%] spell power
        int magesNum = getTraitValue(Trait.MAGE);
        List<Creature> mages = getCreaturesByTrait(allCreatures, Trait.MAGE);
        if (magesNum >= Trait.MAGE.getLevels().get(0) && magesNum < Trait.MAGE.getLevels().get(1)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 5));
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 15, true));
        }
        if (magesNum >= Trait.MAGE.getLevels().get(1) && magesNum < Trait.MAGE.getLevels().get(2)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 10));
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 30, true));
        }
        if (magesNum >= Trait.MAGE.getLevels().get(2)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 20));
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 60, true));
        }
    }

    private void updateFrostBornBuff(List<Creature> allCreatures) {
        int frostbornsNum = getTraitValue(Trait.FROSTBORN);
        // Frostborn creatures deals [30% / 55% / 80%] magic damage on attack, based on their Spell power.
        // Attacks also slows target by [5% / 10% / 15%]
        List<Creature> frostborns = getCreaturesByTrait(allCreatures, Trait.FROSTBORN);
        if (frostbornsNum >= Trait.FROSTBORN.getLevels().get(0) && frostbornsNum < Trait.FROSTBORN.getLevels().get(1)) {
            frostborns.forEach(c -> {
                c.applyCreatureTagChange(CreatureTag.ADDITIONAL_MAGIC_DAMAGE_ON_ATTACK, StatChangeSource.FROSTBORN_TRAIT, 30);
                c.applyCreatureTagChange(CreatureTag.PERCENTAGE_SLOW_ON_ATTACK, StatChangeSource.FROSTBORN_TRAIT, 5);
            });
        }
        if (frostbornsNum >= Trait.FROSTBORN.getLevels().get(1) && frostbornsNum < Trait.FROSTBORN.getLevels().get(2)) {
            frostborns.forEach(c -> {
                c.applyCreatureTagChange(CreatureTag.ADDITIONAL_MAGIC_DAMAGE_ON_ATTACK, StatChangeSource.FROSTBORN_TRAIT, 55);
                c.applyCreatureTagChange(CreatureTag.PERCENTAGE_SLOW_ON_ATTACK, StatChangeSource.FROSTBORN_TRAIT, 10);
            });
        }
        if (frostbornsNum >= Trait.FROSTBORN.getLevels().get(2)) {
            frostborns.forEach(c -> {
                c.applyCreatureTagChange(CreatureTag.ADDITIONAL_MAGIC_DAMAGE_ON_ATTACK, StatChangeSource.FROSTBORN_TRAIT, 80);
                c.applyCreatureTagChange(CreatureTag.PERCENTAGE_SLOW_ON_ATTACK, StatChangeSource.FROSTBORN_TRAIT, 15);
            });
        }
    }

    private void updateAssassinBuff(List<Creature> allCreatures) {
        // Assassins gain [+20% / +30% / +40%] attack and [+15% / +25% / +50%] speed
        int assassinsNum = getTraitValue(Trait.ASSASSIN);
        List<Creature> assassins = getCreaturesByTrait(allCreatures, Trait.ASSASSIN);
        if (assassinsNum >= Trait.ASSASSIN.getLevels().get(0) && assassinsNum < Trait.ASSASSIN.getLevels().get(1)) {
            assassins.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.ASSASSIN_TRAIT, 20, true));
            assassins.forEach(c -> c.applyBuff(Stat.SPEED, StatChangeSource.ASSASSIN_TRAIT, 15, true));
        }
        if (assassinsNum >= Trait.ASSASSIN.getLevels().get(1) && assassinsNum < Trait.ASSASSIN.getLevels().get(2)) {
            assassins.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.ASSASSIN_TRAIT, 30, true));
            assassins.forEach(c -> c.applyBuff(Stat.SPEED, StatChangeSource.ASSASSIN_TRAIT, 25, true));
        }
        if (assassinsNum >= Trait.ASSASSIN.getLevels().get(2)) {
            assassins.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.ASSASSIN_TRAIT, 40, true));
            assassins.forEach(c -> c.applyBuff(Stat.SPEED, StatChangeSource.ASSASSIN_TRAIT, 50, true));
        }
    }

    private void updateStudentsBuff(List<Creature> allCreatures) {
        int studentsNum = getTraitValue(Trait.STUDENT);
        List<Creature> students = getCreaturesByTrait(allCreatures, Trait.STUDENT);
        if (studentsNum >= Trait.STUDENT.getLevels().get(0) && studentsNum < Trait.STUDENT.getLevels().get(1)) {
            students.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL, StatChangeSource.STUDENT_TRAIT, 8));
        }
        if (studentsNum >= Trait.STUDENT.getLevels().get(1) && studentsNum < Trait.STUDENT.getLevels().get(2)) {
            students.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL, StatChangeSource.STUDENT_TRAIT, 17));
        }
        if (studentsNum >= Trait.STUDENT.getLevels().get(2)) {
            students.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL, StatChangeSource.STUDENT_TRAIT, 25));
        }
    }

    private void updateAlchemistsBuff(List<Creature> allCreatures) {
        int alchemistsNum = getTraitValue(Trait.ALCHEMIST);
        List<Creature> alchemists = getCreaturesByTrait(allCreatures, Trait.ALCHEMIST);
        if (alchemistsNum >= Trait.ALCHEMIST.getLevels().get(0) && alchemistsNum < Trait.ALCHEMIST.getLevels().get(1)) {
            alchemists.forEach(c -> c.applyCreatureTagChange(CreatureTag.BOUNCING_SKILL, StatChangeSource.ALCHEMIST_TRAIT, 1));
        }
        if (alchemistsNum >= Trait.ALCHEMIST.getLevels().get(1) && alchemistsNum < Trait.ALCHEMIST.getLevels().get(2)) {
            alchemists.forEach(c -> c.applyCreatureTagChange(CreatureTag.BOUNCING_SKILL, StatChangeSource.ALCHEMIST_TRAIT, 2));
        }
        if (alchemistsNum >= Trait.ALCHEMIST.getLevels().get(2)) {
            alchemists.forEach(c -> c.applyCreatureTagChange(CreatureTag.BOUNCING_SKILL, StatChangeSource.ALCHEMIST_TRAIT, 4));
        }
    }

    private void updateDuelistsBuff(List<Creature> allCreatures) {
        //Duelists have [25% / 40% / 60%] chance to counterattack with [50% / 65% / 80%] attack damage
        int duelistsNum = getTraitValue(Trait.DUELIST);
        List<Creature> duelists = getCreaturesByTrait(allCreatures, Trait.DUELIST);
        if (duelistsNum >= Trait.DUELIST.getLevels().get(0) && duelistsNum < Trait.DUELIST.getLevels().get(1)) {
            duelists.forEach(c -> c.applyCreatureTagChange(CreatureTag.COUNTERATTACK_CHANCE, StatChangeSource.DUELISTS_TRAIT, 25));
            duelists.forEach(c -> c.applyCreatureTagChange(CreatureTag.COUNTERATTACK_DAMAGE, StatChangeSource.DUELISTS_TRAIT, 50));
        }
        if (duelistsNum >= Trait.DUELIST.getLevels().get(1) && duelistsNum < Trait.DUELIST.getLevels().get(2)) {
            duelists.forEach(c -> c.applyCreatureTagChange(CreatureTag.COUNTERATTACK_CHANCE, StatChangeSource.DUELISTS_TRAIT, 40));
            duelists.forEach(c -> c.applyCreatureTagChange(CreatureTag.COUNTERATTACK_DAMAGE, StatChangeSource.DUELISTS_TRAIT, 60));
        }
        if (duelistsNum >= Trait.DUELIST.getLevels().get(2)) {
            duelists.forEach(c -> c.applyCreatureTagChange(CreatureTag.COUNTERATTACK_CHANCE, StatChangeSource.DUELISTS_TRAIT, 60));
            duelists.forEach(c -> c.applyCreatureTagChange(CreatureTag.COUNTERATTACK_DAMAGE, StatChangeSource.DUELISTS_TRAIT, 80));
        }
    }

    private void updateArchersBuff(List<Creature> allCreatures) {
        // Archers have [15% / 30% / 50%] to attack additional time
        int archersNum = getTraitValue(Trait.ARCHER);
        List<Creature> archers = getCreaturesByTrait(allCreatures, Trait.ARCHER);
        if (archersNum >= Trait.ARCHER.getLevels().get(0) && archersNum < Trait.ARCHER.getLevels().get(1)) {
            archers.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADDITIONAL_ATTACK_CHANCE, StatChangeSource.ARCHER_TRAIT, 15));
        }
        if (archersNum >= Trait.ARCHER.getLevels().get(1) && archersNum < Trait.ARCHER.getLevels().get(2)) {
            archers.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADDITIONAL_ATTACK_CHANCE, StatChangeSource.ARCHER_TRAIT, 30));
        }
        if (archersNum >= Trait.ARCHER.getLevels().get(2)) {
            archers.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADDITIONAL_ATTACK_CHANCE, StatChangeSource.ARCHER_TRAIT, 50));
        }
    }

    private void updateFirebornBuff(List<Creature> allCreatures) {
        // Fireborns applies [15% / 35% / 60%] more burn
        int firbornsNum = getTraitValue(Trait.FIREBORN);
        List<Creature> fireborns = getCreaturesByTrait(allCreatures, Trait.FIREBORN);
        if (firbornsNum >= Trait.FIREBORN.getLevels().get(0) && firbornsNum < Trait.FIREBORN.getLevels().get(1)) {
            fireborns.forEach(c -> c.applyCreatureTagChange(CreatureTag.PERCENTAGE_BURN_BUFF, StatChangeSource.FIREBORN_TRAIT, 15));
        }
        if (firbornsNum >= Trait.FIREBORN.getLevels().get(1) && firbornsNum < Trait.FIREBORN.getLevels().get(2)) {
            fireborns.forEach(c -> c.applyCreatureTagChange(CreatureTag.PERCENTAGE_BURN_BUFF, StatChangeSource.FIREBORN_TRAIT, 35));
        }
        if (firbornsNum >= Trait.FIREBORN.getLevels().get(2)) {
            fireborns.forEach(c -> c.applyCreatureTagChange(CreatureTag.PERCENTAGE_BURN_BUFF, StatChangeSource.FIREBORN_TRAIT, 60));
        }
    }

    private void updateSpiritBuff(List<Creature> allCreatures) {
        //Spirits have chance to doge attack equals [3% / 6% / 12%] of their speed
        //Spirits convert [50% / 100% / 150%] of their spell power to speed
        int spiritsNum = getTraitValue(Trait.SPIRIT);
        List<Creature> spirits = getCreaturesByTrait(allCreatures, Trait.SPIRIT);
        if (spiritsNum >= Trait.SPIRIT.getLevels().get(0) && spiritsNum < Trait.SPIRIT.getLevels().get(1)) {
            spirits.forEach(c -> c.applyCreatureTagChange(CreatureTag.SPELL_POWER_TO_SPEED, StatChangeSource.SPIRIT_TRAIT, 50));
            spirits.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERCENTAGE_SPEED_TO_DODGE, StatChangeSource.SPIRIT_TRAIT, 3));
        }
        if (spiritsNum >= Trait.SPIRIT.getLevels().get(1) && spiritsNum < Trait.SPIRIT.getLevels().get(2)) {
            spirits.forEach(c -> c.applyCreatureTagChange(CreatureTag.SPELL_POWER_TO_SPEED, StatChangeSource.SPIRIT_TRAIT, 100));
            spirits.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERCENTAGE_SPEED_TO_DODGE, StatChangeSource.SPIRIT_TRAIT, 6));
        }
        if (spiritsNum >= Trait.SPIRIT.getLevels().get(2)) {
            spirits.forEach(c -> c.applyCreatureTagChange(CreatureTag.SPELL_POWER_TO_SPEED, StatChangeSource.SPIRIT_TRAIT, 150));
            spirits.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERCENTAGE_SPEED_TO_DODGE, StatChangeSource.SPIRIT_TRAIT, 12));
        }
    }

    private void updateBeastsBuff(List<Creature> allCreatures) {
        // Beasts deal [1% / 2% / 4%] more damage for each 100 health they have
        int beastsNum = getTraitValue(Trait.BEAST);
        List<Creature> beasts = getCreaturesByTrait(allCreatures, Trait.BEAST);
        if (beastsNum >= Trait.BEAST.getLevels().get(0) && beastsNum < Trait.BEAST.getLevels().get(1)) {
            beasts.forEach(c -> c.applyCreatureTagChange(CreatureTag.PERCENTAGE_OF_ADDITIONAL_DAMAGE_BY_HP, StatChangeSource.BEASTS_TRAIT, 1));
        }
        if (beastsNum >= Trait.BEAST.getLevels().get(1) && beastsNum < Trait.BEAST.getLevels().get(2)) {
            beasts.forEach(c -> c.applyCreatureTagChange(CreatureTag.PERCENTAGE_OF_ADDITIONAL_DAMAGE_BY_HP, StatChangeSource.BEASTS_TRAIT, 2));
        }
        if (beastsNum >= Trait.BEAST.getLevels().get(2)) {
            beasts.forEach(c -> c.applyCreatureTagChange(CreatureTag.PERCENTAGE_OF_ADDITIONAL_DAMAGE_BY_HP, StatChangeSource.BEASTS_TRAIT, 4));
        }
    }

    private void updateSummonersBuff(List<Creature> allCreatures) {
        // At the start of the battle summon [Common / Rare / Epic / Legendary] creature on your side
        int summonersNum = getTraitValue(Trait.SUMMONER);
        List<Creature> summoners = getCreaturesByTrait(allCreatures, Trait.SUMMONER);
        if (summonersNum >= Trait.SUMMONER.getLevels().get(0) && summonersNum < Trait.SUMMONER.getLevels().get(1)) {
            Creature summoner = summoners.get(0);
            summoner.applyCreatureTagChange(CreatureTag.SUMMON_CREATURE_WITH_VALUE, StatChangeSource.SUMMONER_TRAIT, Rarity.COMMON.getValue());
        }
        if (summonersNum >= Trait.SUMMONER.getLevels().get(1) && summonersNum < Trait.SUMMONER.getLevels().get(2)) {
            Creature summoner = summoners.get(0);
            summoner.applyCreatureTagChange(CreatureTag.SUMMON_CREATURE_WITH_VALUE, StatChangeSource.SUMMONER_TRAIT, Rarity.RARE.getValue());
        }
        if (summonersNum >= Trait.SUMMONER.getLevels().get(2) && summonersNum < Trait.SUMMONER.getLevels().get(3)) {
            Creature summoner = summoners.get(0);
            summoner.applyCreatureTagChange(CreatureTag.SUMMON_CREATURE_WITH_VALUE, StatChangeSource.SUMMONER_TRAIT, Rarity.EPIC.getValue());
        }
        if (summonersNum >= Trait.SUMMONER.getLevels().get(3)) {
            Creature summoner = summoners.get(0);
            summoner.applyCreatureTagChange(CreatureTag.SUMMON_CREATURE_WITH_VALUE, StatChangeSource.SUMMONER_TRAIT, Rarity.LEGENDARY.getValue());
        }
    }

    private void updatePiratesBuff() {
        // When battle ends pirates discover unique [Common / Uncommon / Rare / Epic / Legendary] item
        int piratesNum = getTraitValue(Trait.PIRATE);
        if (piratesNum >= Trait.PIRATE.getLevels().get(0) && piratesNum < Trait.PIRATE.getLevels().get(1)) {
            setPiratesTreasure(Rarity.COMMON);
        }
        if (piratesNum >= Trait.PIRATE.getLevels().get(1) && piratesNum < Trait.PIRATE.getLevels().get(2)) {
            setPiratesTreasure(Rarity.UNCOMMON);
        }
        if (piratesNum >= Trait.PIRATE.getLevels().get(2) && piratesNum < Trait.PIRATE.getLevels().get(3)) {
            setPiratesTreasure(Rarity.RARE);
        }
        if (piratesNum >= Trait.PIRATE.getLevels().get(3) && piratesNum < Trait.PIRATE.getLevels().get(4)) {
            setPiratesTreasure(Rarity.EPIC);
        }
        if (piratesNum >= Trait.PIRATE.getLevels().get(4)) {
            setPiratesTreasure(Rarity.LEGENDARY);
        }
    }

    public void processPiratesBuff(PlayerController playerController) {
        Rarity rarity = getPiratesTreasure();
        if (rarity != Rarity.UNDEFINED) {
            Item selectedItem = ItemController.selectItemFromList(
                    ItemController.getPirateItems(rarity, Constants.PIRATES_TREASURES.value),
                    "Pirates discovered " + rarity.getName() + " treasure! Choose item and creature to equip it!");
            Creature selectedCreature = playerController.selectCreature(false);
            selectedItem.equipOn(selectedCreature);
        }
    }

    public List<Creature> getCreaturesByTrait(List<Creature> allCreatures, Trait trait) {
        return allCreatures.stream().filter(creature -> creature.getTraitContainer().hasTag(trait)).collect(Collectors.toList());
    }

    public void addTrait(Trait trait) {
        traitContainer.addTagValue(trait, 1);
    }

    public void removeTrait(Trait trait) {
        traitContainer.setTagValue(trait, Math.max(traitContainer.getTagValue(trait) - 1, 0));
    }

    public int getTraitValue(Trait trait) {
        return traitContainer.getTagValue(trait);
    }

    public Rarity getPiratesTreasure() {
        return piratesTreasure;
    }

    public void setPiratesTreasure(Rarity piratesTreasure) {
        this.piratesTreasure = piratesTreasure;
    }
}

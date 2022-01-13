package core.controllers;

import java.util.List;
import java.util.stream.Collectors;

import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.traits.Trait;
import core.traits.TraitContainer;

public class TraitsController {
    private final TraitContainer traitContainer;

    private TraitsController(TraitContainer traitContainer) {
        this.traitContainer = traitContainer;
    }

    public static TraitsController build() {
        return new TraitsController(new TraitContainer());
    }

    public void updateTraitBuffs(List<Creature> allCreatures) {
        allCreatures.forEach(Creature::clearAllChangesFromTraits);
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
    }

    public void updateDefenderBuff(List<Creature> allCreatures) {
        //Defender: All creatures gains [+2 / +4 / +8] physical and magic armor
        int defendersNum = getTraitValue(Trait.DEFENDER);
        if (defendersNum >= Trait.DEFENDER.getLevels().get(0) && defendersNum < Trait.DEFENDER.getLevels().get(1)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 2));
            allCreatures.forEach(c -> c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.DEFENDER_TRAIT, 2));
        }
        if (defendersNum >= Trait.DEFENDER.getLevels().get(1) && defendersNum < Trait.DEFENDER.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 4));
            allCreatures.forEach(c -> c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.DEFENDER_TRAIT, 4));
        }
        if (defendersNum >= Trait.DEFENDER.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 8));
            allCreatures.forEach(c -> c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.DEFENDER_TRAIT, 8));
        }
    }

    private void updateKnightBuff(List<Creature> allCreatures) {
        //KING_GUARD: <+2 Attack, +3 Attack, +5 Attack> to all Creatures.
        int knight = getTraitValue(Trait.KNIGHT);
        if (knight >= Trait.KNIGHT.getLevels().get(0) && knight < Trait.KNIGHT.getLevels().get(1)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KNIGHT_TRAIT, 2));
        }
        if (knight >= Trait.KNIGHT.getLevels().get(1) && knight < Trait.KNIGHT.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KNIGHT_TRAIT, 3));
        }
        if (knight >= Trait.KNIGHT.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KNIGHT_TRAIT, 5));
        }
    }

    private void updateWarriorBuff(List<Creature> allCreatures) {
        // Warriors gains [+15% / +20% / +30%] attack and [+2 / +3 / +5] physical armor
        int warriorsNum = getTraitValue(Trait.WARRIOR);
        List<Creature> warriors = getCreaturesByTrait(allCreatures, Trait.WARRIOR);
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(0) && warriorsNum < Trait.WARRIOR.getLevels().get(1)) {
            warriors.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.WARRIOR_TRAIT, 15, true));
            warriors.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.WARRIOR_TRAIT, 2));
        }
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(1) && warriorsNum < Trait.WARRIOR.getLevels().get(2)) {
            warriors.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.WARRIOR_TRAIT, 20, true));
            warriors.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.WARRIOR_TRAIT, 3));
        }
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(2)) {
            warriors.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.WARRIOR_TRAIT, 30, true));
            warriors.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.WARRIOR_TRAIT, 4));
        }
    }

    private void updatePoisonousBuff(List<Creature> allCreatures) {
        //POISONOUS: Ядовитые получают [+1 / +2 / +3] яда
        int poisonousNum = getTraitValue(Trait.POISONOUS);
        List<Creature> poisonousCreatures = getCreaturesByTrait(allCreatures, Trait.POISONOUS);
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(0) && poisonousNum < Trait.POISONOUS.getLevels().get(0)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 1));
        }
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(1) && poisonousNum < Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 2));
        }
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 5));
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
        //+7% здоровья, +9% атаки, +1 физической и магической зашиты
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
                    robot.applyCreatureTagChange(CreatureTag.ADD_TEMP_PERCENTAGE_ATTACK_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 9);
                }
                if (stat == Stat.PHYSICAL_ARMOR) {
                    robot.applyCreatureTagChange(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 1);
                    robot.applyCreatureTagChange(CreatureTag.ADD_TEMP_MARM_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 1);
                }
            }
        }
    }

    private void updateMagesBuff(List<Creature> allCreatures) {
        //MAGE: Mages gains [+2 / +4 / +7] and [+20% / +45% / +8%] spell power
        int magesNum = getTraitValue(Trait.MAGE);
        List<Creature> mages = getCreaturesByTrait(allCreatures, Trait.MAGE);
        if (magesNum >= Trait.MAGE.getLevels().get(0) && magesNum < Trait.MAGE.getLevels().get(1)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 2));
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 20, true));
        }
        if (magesNum >= Trait.MAGE.getLevels().get(1) && magesNum < Trait.MAGE.getLevels().get(2)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 4));
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 45, true));
        }
        if (magesNum >= Trait.MAGE.getLevels().get(2)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 7));
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 75, true));
        }
    }

    private void updateFrostBornBuff(List<Creature> allCreatures) {

    }

    private void updateAssassinBuff(List<Creature> allCreatures) {
        // Assassins gains [+20% / +30% / +40%] attack and [+15% / +25% / +50%] speed
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
}

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
        //Defender: All creatures gains [+2 / +4 / +6] physical armor
        int defendersNum = getTraitValue(Trait.DEFENDER);
        allCreatures.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.DEFENDER_TRAIT));
        if (defendersNum >= Trait.DEFENDER.getLevels().get(0) && defendersNum < Trait.DEFENDER.getLevels().get(1)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 2));
        }
        if (defendersNum >= Trait.DEFENDER.getLevels().get(1) && defendersNum < Trait.DEFENDER.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 4));
        }
        if (defendersNum >= Trait.DEFENDER.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.DEFENDER_TRAIT, 6));
        }
    }

    private void updateKnightBuff(List<Creature> allCreatures) {
        //KING_GUARD: <+2 Attack, +3 Attack, +5 Attack> to all Creatures.
        int knight = getTraitValue(Trait.KNIGHT);
        allCreatures.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.KNIGHT_TRAIT));
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
        int warriorsNum = getTraitValue(Trait.WARRIOR);
        List<Creature> warriors = getCreaturesByTrait(allCreatures, Trait.WARRIOR);
        warriors.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.WARRIOR_TRAIT));
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(0) && warriorsNum < Trait.WARRIOR.getLevels().get(1)) {
            warriors.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.WARRIOR_TRAIT, 1));
            warriors.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.WARRIOR_TRAIT, 1));
        }
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(1) && warriorsNum < Trait.WARRIOR.getLevels().get(2)) {
            warriors.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.WARRIOR_TRAIT, 2));
            warriors.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.WARRIOR_TRAIT, 2));
        }
        if (warriorsNum >= Trait.WARRIOR.getLevels().get(2)) {
            warriors.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.WARRIOR_TRAIT, 3));
            warriors.forEach(c -> c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.WARRIOR_TRAIT, 3));
        }
    }

    private void updatePoisonousBuff(List<Creature> allCreatures) {
        //POISONOUS: Ядовитые получают [+1 / +2 / +3] яда при атаке
        int poisonousNum = getTraitValue(Trait.POISONOUS);
        List<Creature> poisonousCreatures = getCreaturesByTrait(allCreatures, Trait.POISONOUS);
        poisonousCreatures.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.POISONOUS_TRAIT));
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(0) && poisonousNum < Trait.POISONOUS.getLevels().get(0)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 1));
        }
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(1) && poisonousNum < Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 2));
        }
        if (poisonousNum >= Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 3));
        }
    }

    private void updateEaterBuff(List<Creature> allCreatures) {
        //EATER: Обжоры навсегда получают [+4 / +6 / +10] здоровья перед боем
        int eatersNum = getTraitValue(Trait.EATER);
        List<Creature> eaters = getCreaturesByTrait(allCreatures, Trait.EATER);
        eaters.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.EATERS_TRAIT));
        if (eatersNum >= Trait.EATER.getLevels().get(0) && eatersNum < Trait.EATER.getLevels().get(1)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 4));
        }
        if (eatersNum >= Trait.EATER.getLevels().get(1) && eatersNum < Trait.EATER.getLevels().get(2)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 6));
        }
        if (eatersNum >= Trait.EATER.getLevels().get(2)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 10));
        }
    }

    private void updateRobotBuffs(List<Creature> allCreatures) {
        //ROBOT: На время боя роботы получают случайные усиления [1 / 3 / 7] раз из следующих:
        //+5 здоровья, +2 атаки, +1 физической зашиты
        int robotsNum = getTraitValue(Trait.ROBOT);
        List<Creature> robots = getCreaturesByTrait(allCreatures, Trait.ROBOT);
        robots.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.ROBOT_TRAIT));
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
        for (int i = 0; i < numberOfRobotBuffs; i++) {
            Stat stat = Stat.getRandomStatFrom(Stat.HP, Stat.ATTACK, Stat.PHYSICAL_ARMOR);
            if (stat == Stat.HP) {
                robots.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_TEMP_HP_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 8));
            }
            if (stat == Stat.ATTACK) {
                robots.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_TEMP_ATTACK_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 2));
            }
            if (stat == Stat.PHYSICAL_ARMOR) {
                robots.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 1));
                robots.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_TEMP_MARM_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 1));
            }
        }
    }

    private void updateMagesBuff(List<Creature> allCreatures) {
        //"Маг", 15, "Маги получают [+4 / +7 / +10] силы умений", 2, 3, 4
        int magesNum = getTraitValue(Trait.MAGE);
        List<Creature> mages = getCreaturesByTrait(allCreatures, Trait.MAGE);
        mages.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.MAGE_TRAIT));
        if (magesNum >= Trait.MAGE.getLevels().get(0) && magesNum < Trait.MAGE.getLevels().get(1)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 4));
        }
        if (magesNum >= Trait.MAGE.getLevels().get(1) && magesNum < Trait.MAGE.getLevels().get(2)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 7));
        }
        if (magesNum >= Trait.MAGE.getLevels().get(2)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 10));
        }
    }

    private void updateFrostBornBuff(List<Creature> allCreatures) {

    }

    private void updateAssassinBuff(List<Creature> allCreatures) {
        //Ассасины получают [+2 / +3 / +4] атаки и [+10 / +20 / +40] скорости
        int assassinsNum = getTraitValue(Trait.ASSASSIN);
        List<Creature> assassins = getCreaturesByTrait(allCreatures, Trait.ASSASSIN);
        assassins.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.ASSASSIN_TRAIT));
        if (assassinsNum >= Trait.ASSASSIN.getLevels().get(0) && assassinsNum < Trait.ASSASSIN.getLevels().get(1)) {
            assassins.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.ASSASSIN_TRAIT, 2));
            assassins.forEach(c -> c.applyBuff(Stat.SPEED, StatChangeSource.ASSASSIN_TRAIT, 10));
        }
        if (assassinsNum >= Trait.ASSASSIN.getLevels().get(1) && assassinsNum < Trait.ASSASSIN.getLevels().get(2)) {
            assassins.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.ASSASSIN_TRAIT, 3));
            assassins.forEach(c -> c.applyBuff(Stat.SPEED, StatChangeSource.ASSASSIN_TRAIT, 20));
        }
        if (assassinsNum >= Trait.ASSASSIN.getLevels().get(2)) {
            assassins.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.ASSASSIN_TRAIT, 4));
            assassins.forEach(c -> c.applyBuff(Stat.SPEED, StatChangeSource.ASSASSIN_TRAIT, 40));
        }
    }

    private void updateStudentsBuff(List<Creature> allCreatures) {
        int studentsNum = getTraitValue(Trait.STUDENT);
        List<Creature> students = getCreaturesByTrait(allCreatures, Trait.STUDENT);
        students.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.STUDENT_TRAIT));
        if (studentsNum >= Trait.STUDENT.getLevels().get(0) && studentsNum < Trait.STUDENT.getLevels().get(1)) {
            students.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL, StatChangeSource.STUDENT_TRAIT, 5));
        }
        if (studentsNum >= Trait.STUDENT.getLevels().get(1) && studentsNum < Trait.STUDENT.getLevels().get(2)) {
            students.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL, StatChangeSource.STUDENT_TRAIT, 11));
        }
        if (studentsNum >= Trait.STUDENT.getLevels().get(2)) {
            students.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL, StatChangeSource.STUDENT_TRAIT, 23));
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

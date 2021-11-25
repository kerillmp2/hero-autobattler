package core.controllers;

import java.util.List;
import java.util.stream.Collectors;

import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.Stat;
import core.creature.StatChangeSource;
import core.traits.Trait;

public class TraitsController {
    private final TraitContainer traitContainer;

    private TraitsController(TraitContainer traitContainer) {
        this.traitContainer = traitContainer;
    }

    public static TraitsController build() {
        return new TraitsController(new TraitContainer());
    }

    public void updateTraitBuffs(List<Creature> allCreatures) {
        updateKingGuardBuff(allCreatures);
        updatePoisonousBuff(allCreatures);
        updateEaterBuff(allCreatures);
        updateRobotBuffs(allCreatures);
        updateMagesBuff(allCreatures);
        updateFrostBornBuff(allCreatures);
    }

    private void updateKingGuardBuff(List<Creature> allCreatures) {
        //KING_GUARD: <+2 Attack, +3 Attack, +5 Attack> to all Creatures.
        int kingGuardNum = getTraitValue(Trait.KING_GUARD);
        allCreatures.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.KING_GUARD_TRAIT));
        if (kingGuardNum == Trait.KING_GUARD.getLevels().get(0)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KING_GUARD_TRAIT, 2));
        }
        if (kingGuardNum == Trait.KING_GUARD.getLevels().get(1)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KING_GUARD_TRAIT, 3));
        }
        if (kingGuardNum >= Trait.KING_GUARD.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KING_GUARD_TRAIT, 5));
        }
    }

    private void updatePoisonousBuff(List<Creature> allCreatures) {
        //POISONOUS: Ядовитые получают [+1 / +2 / +3] яда при атаке
        int poisonousNum = getTraitValue(Trait.POISONOUS);
        List<Creature> poisonousCreatures = getCreaturesByTrait(allCreatures, Trait.POISONOUS);
        poisonousCreatures.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.POISONOUS_TRAIT));
        if (poisonousNum == Trait.POISONOUS.getLevels().get(0)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 1));
        }
        if (poisonousNum == Trait.POISONOUS.getLevels().get(1)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 2));
        }
        if (poisonousNum == Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 3));
        }
    }

    private void updateEaterBuff(List<Creature> allCreatures) {
        //EATER: Обжоры навсегда получают [+4 / +6 / +10] здоровья перед боем
        int eatersNum = getTraitValue(Trait.EATER);
        List<Creature> eaters = getCreaturesByTrait(allCreatures, Trait.EATER);
        eaters.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.EATERS_TRAIT));
        if (eatersNum == Trait.EATER.getLevels().get(0)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 4));
        }
        if (eatersNum == Trait.EATER.getLevels().get(1)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_PERMANENT_HP_BEFORE_BATTLE, StatChangeSource.EATERS_TRAIT, 6));
        }
        if (eatersNum == Trait.EATER.getLevels().get(2)) {
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
        if (robotsNum == Trait.ROBOT.getLevels().get(0)) {
            numberOfRobotBuffs = 1;
        }
        if (robotsNum == Trait.ROBOT.getLevels().get(1)) {
            numberOfRobotBuffs = 3;
        }
        if (robotsNum == Trait.ROBOT.getLevels().get(2)) {
            numberOfRobotBuffs = 7;
        }
        for (int i = 0; i < numberOfRobotBuffs; i++) {
            Stat stat = Stat.getRandomStatFrom(Stat.HP, Stat.ATTACK, Stat.PHYSICAL_ARMOR);
            if (stat == Stat.HP) {
                robots.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_TEMP_HP_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 5));
            }
            if (stat == Stat.ATTACK) {
                robots.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_TEMP_ATTACK_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 2));
            }
            if (stat == Stat.PHYSICAL_ARMOR) {
                robots.forEach(c -> c.applyCreatureTagChange(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE, StatChangeSource.ROBOT_TRAIT, 1));
            }
        }
    }

    private void updateMagesBuff(List<Creature> allCreatures) {
        //"Маг", 15, "Маги получают [+4 / +7 / +10] силы умений", 2, 3, 4
        int magesNum = getTraitValue(Trait.MAGE);
        List<Creature> mages = getCreaturesByTrait(allCreatures, Trait.MAGE);
        mages.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.MAGE_TRAIT));
        if (magesNum == Trait.MAGE.getLevels().get(0)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 4));
        }
        if (magesNum == Trait.MAGE.getLevels().get(1)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 7));
        }
        if (magesNum >= Trait.MAGE.getLevels().get(2)) {
            mages.forEach(c -> c.applyBuff(Stat.SPELL_POWER, StatChangeSource.MAGE_TRAIT, 10));
        }
    }

    private void updateFrostBornBuff(List<Creature> allCreatures) {

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

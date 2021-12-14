package core.creature;

import core.creature.skills.CreatureSkillFactory;
import core.traits.Trait;

public class CreatureFactory {

    /* Инструкция по созданию нового существа:
    1. Создать скилл в CreatureSkillFactory
    2. Создать трейты в Trait
    3. Прописать баффы трейтов в TraitsController (не забыть добавить StatChangeSource для трейта)
    4. Создать метод для создания существа в CreatureFactoty (тут)
    5. Добавить метод создания существа в creatureByName()
    6. Добавить метод создания существа в CreaturePool.init()
     */

    public static Creature dunkan() {
        return Creature.withStats("Dunkan", 92, 13, 4, 0, 0, 100, 100, 1)
                .wrapTrait(Trait.WARRIOR)
                .wrapTrait(Trait.DEFENDER)
                .wrapSkill(CreatureSkillFactory.dunkanSkill());
    }

    public static Creature salvira() {
        Creature salvira = new Creature ("Salvira", 82, 11, 1, 0, 1, 120, 80, 1, 1, CreatureTag.HAVE_BASIC_ATTACK)
                .wrapTrait(Trait.ASSASSIN)
                .wrapTrait(Trait.POISONOUS)
                .wrapSkill(CreatureSkillFactory.salviraSkill());
        salvira.addTagValue(CreatureTag.POISONOUS, 1);
        return salvira;
    }

    public static Creature ignar() {
        return new Creature ("Ignar", 118, 13, 0, 4, 1, 80, 100, 1, 1, CreatureTag.HAVE_BASIC_ATTACK)
                .wrapTrait(Trait.DEMON)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.ignarSkill());
    }

    public static Creature warbot() {
        return new Creature ("Warbot", 90, 15, 4, 0, 1, 90, 90, 1, 1, CreatureTag.HAVE_BASIC_ATTACK)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.warbotSkill());
    }

    public static Creature kodji() {
        return new Creature ("Kodji", 83, 13, 2, 4, 7, 105, 30, 1, 1, CreatureTag.HAVE_BASIC_ATTACK)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.MAGE)
                .wrapSkill(CreatureSkillFactory.kodjiSkill());
    }

    public static Creature mira() {
        return new Creature ("Mira", 86, 14, 1, 3, 5, 108, 70, 1, 1, CreatureTag.HAVE_BASIC_ATTACK)
                .wrapTrait(Trait.ALCHEMIST)
                .wrapTrait(Trait.STUDENT)
                .wrapSkill(CreatureSkillFactory.miraSkill());
    }

    public static Creature dummy() {
        return new Creature ("Dummy", 100, 0, 0, 0, 0, 100, 100, 2, 1, CreatureTag.HAVE_BASIC_ATTACK)
                .wrapTrait(Trait.POISONOUS)
                .wrapTrait(Trait.DEFENDER)
                .wrapSkill(CreatureSkillFactory.miraSkill());
    }

    public static Creature creatureByName(String name) {
        switch (name) {
            case "Dunkan": {
                return dunkan();
            }
            case "Salvira": {
                return salvira();
            }
            case "Ignar": {
                return ignar();
            }
            case "Warbot": {
                return warbot();
            }
            case "Kodji": {
                return kodji();
            }
            case "Mira": {
                return mira();
            }
            case "Dummy": {
                return dummy();
            }
        }
        return Creature.shopDummy();
    }
}

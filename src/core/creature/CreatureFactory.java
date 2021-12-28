package core.creature;

import core.controllers.utils.RandomController;
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
        return Creature.withStats("Dunkan", 81, 14, 4, 0, 0, 100, 100, 1)
                .wrapTrait(Trait.WARRIOR)
                .wrapTrait(Trait.DEFENDER)
                .wrapSkill(CreatureSkillFactory.dunkanSkill());
    }

    public static Creature salvira() {
        Creature salvira = Creature.withStats("Salvira", 78, 12, 0, 1, 1, 120, 80, 1)
                .wrapTrait(Trait.ASSASSIN)
                .wrapTrait(Trait.POISONOUS)
                .wrapSkill(CreatureSkillFactory.salviraSkill());
        salvira.addTagValue(CreatureTag.POISONOUS, 1);
        return salvira;
    }

    public static Creature ignar() {
        return Creature.withStats("Ignar", 114, 14, 1, 4, 1, 80, 100, 1)
                .wrapTrait(Trait.DEMON)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.ignarSkill());
    }

    public static Creature warbot() {
        return Creature.withStats("Warbot", 93, 15, 3, 0, 1, 90, 90, 1)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.warbotSkill());
    }

    public static Creature kodji() {
        return Creature.withStats("Kodji", 83, 14, 2, 4, 7, 105, 30, 1)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.MAGE)
                .wrapSkill(CreatureSkillFactory.kodjiSkill());
    }

    public static Creature mira() {
        return Creature.withStats("Mira", 78, 14, 1, 3, 4, 108, 65, 1)
                .wrapTrait(Trait.ALCHEMIST)
                .wrapTrait(Trait.STUDENT)
                .wrapSkill(CreatureSkillFactory.miraSkill());
    }

    public static Creature dummy() {
        return Creature.withStats("Dummy" + RandomController.randomInt(1, 100), 100, 0, 1, 0, 0, 100, 100, 2)
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

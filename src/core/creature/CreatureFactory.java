package core.creature;

import core.action.ActionFactory;
import core.action.ResolveTime;
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
        return Creature.withStats("Dunkan", 81, 13, 4, 0, 0, 100, 100, 1)
                .wrapTrait(Trait.WARRIOR)
                .wrapTrait(Trait.DEFENDER)
                .wrapSkill(CreatureSkillFactory.dunkanSkill());
    }

    public static Creature salvira() {
        Creature salvira = Creature.withStats("Salvira", 77, 11, 1, 1, 1, 120, 80, 1)
                .wrapTrait(Trait.ASSASSIN)
                .wrapTrait(Trait.POISONOUS)
                .wrapSkill(CreatureSkillFactory.salviraSkill());
        salvira.addTagValue(CreatureTag.POISONOUS, 1);
        return salvira;
    }

    public static Creature ignar() {
        return Creature.withStats("Ignar", 112, 13, 1, 3, 1, 80, 100, 1)
                .wrapTrait(Trait.DEMON)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.ignarSkill());
    }

    public static Creature warbot() {
        return Creature.withStats("Warbot", 89, 13, 3, 0, 1, 90, 100, 1)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.warbotSkill());
    }

    public static Creature kodji() {
        return Creature.withStats("Kodji", 83, 13, 2, 4, 7, 105, 32, 1)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.MAGE)
                .wrapSkill(CreatureSkillFactory.kodjiSkill());
    }

    public static Creature mira() {
        return Creature.withStats("Mira", 79, 13, 1, 3, 4, 108, 65, 1)
                .wrapTrait(Trait.ALCHEMIST)
                .wrapTrait(Trait.STUDENT)
                .wrapSkill(CreatureSkillFactory.miraSkill());
    }

    public static Creature obby() {
        return Creature.withStats("Obby", 82, 13, 3, 1, 2, 110, 100, 1)
                .wrapTrait(Trait.FIREBORN)
                .wrapTrait(Trait.BEAST)
                .wrapSkill(CreatureSkillFactory.obbySkill());
    }

    public static Creature leto() {
        return Creature.withStats("Leto", 72, 16, 0, 2, 2,125, 100, 1)
                .wrapTrait(Trait.SPIRIT)
                .wrapTrait(Trait.ARCHER)
                .wrapSkill(CreatureSkillFactory.letoSkill());
    }

    public static Creature annie() {
        return Creature.withStats("Annie", 79, 15, 2, 0, 1, 115, 100, 1)
                .wrapTrait(Trait.PIRATE)
                .wrapTrait(Trait.DUELIST)
                .wrapSkill(CreatureSkillFactory.annieSkill());
    }

    public static Creature bolver() {
        return Creature.withStats("Bolver", 99, 13, 2, 3, 2, 85, 100, 1)
                .wrapTrait(Trait.CULTIST)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.bolverSkill());
    }

    public static Creature shaya() {
        return Creature.withStats("Shaya", 77, 12, 1, 3, 6, 105, 100, 1)
                .wrapTrait(Trait.STUDENT)
                .wrapTrait(Trait.SUMMONER)
                .wrapSkill(CreatureSkillFactory.shayaSkill());
    }

    public static Creature rover() {
        return Creature.withStats("Rover", 134, 14, 3, 3, 4, 110, 100, 2)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.DEFENDER)
                .wrapSkill(CreatureSkillFactory.roverSkill());
    }

    public static Creature cathyra() {
        return Creature.withStats("Cathyra", 120, 17, 1, 1, 3, 130, 100, 2)
                .wrapTrait(Trait.FIREBORN)
                .wrapTrait(Trait.ASSASSIN)
                .wrapSkill(CreatureSkillFactory.cathyraSkill());
    }

    public static Creature coldy() {
        return Creature.withStats("Coldy", 129, 16, 5, 1, 2, 95, 100, 2)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.UNDEAD)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.coldySkill());
    }

    public static Creature dummy() {
        return Creature.withStats("Dummy", 100, 0, 1, 0, 0, 100, 100, 2)
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
            case "Obby": {
                return obby();
            }
            case "Leto": {
                return leto();
            }
            case "Annie": {
                return annie();
            }
            case "Bolver": {
                return bolver();
            }
            case "Shaya": {
                return shaya();
            }
            case "Rover": {
                return rover();
            }
            case "Cathyra": {
                return cathyra();
            }
            case "Coldy": {
                return coldy();
            }
            case "Dummy": {
                return dummy();
            }
        }
        return Creature.shopDummy();
    }
}

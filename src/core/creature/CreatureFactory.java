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
        return Creature.withStats("Dunkan", 79, 12, 3, 0, 5, 100, 100, 1)
                .wrapTrait(Trait.WARRIOR)
                .wrapTrait(Trait.DEFENDER)
                .wrapSkill(CreatureSkillFactory.dunkanSkill());
    }

    public static Creature salvira() {
        return Creature.withStats("Salvira", 72, 13, 1, 0, 5, 120, 80, 1)
                .wrapTrait(Trait.ASSASSIN)
                .wrapTrait(Trait.POISONOUS)
                .wrapSkill(CreatureSkillFactory.salviraSkill());
    }

    public static Creature ignar() {
        return Creature.withStats("Ignar", 96, 12, 1, 3, 6, 80, 100, 1)
                .wrapTrait(Trait.DEMON)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.ignarSkill());
    }

    public static Creature warbot() {
        return Creature.withStats("Warbot", 81, 12, 2, 0, 2, 90, 100, 1)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.warbotSkill());
    }

    public static Creature kodji() {
        return Creature.withStats("Kodji", 78, 11, 1, 3, 7, 105, 40, 1)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.MAGE)
                .wrapSkill(CreatureSkillFactory.kodjiSkill());
    }

    public static Creature mira() {
        return Creature.withStats("Mira", 79, 12, 1, 4, 6, 108, 90, 1)
                .wrapTrait(Trait.ALCHEMIST)
                .wrapTrait(Trait.STUDENT)
                .wrapTrait(Trait.SUPPORT)
                .wrapSkill(CreatureSkillFactory.miraSkill())
                .wrapBouncingSkill(CreatureSkillFactory.miraBouncingSkill());
    }

    public static Creature obby() {
        return Creature.withStats("Obby", 78, 13, 3, 0, 3, 110, 100, 1)
                .wrapTrait(Trait.FIREBORN)
                .wrapTrait(Trait.BEAST)
                .wrapSkill(CreatureSkillFactory.obbySkill());
    }

    public static Creature leto() {
        return Creature.withStats("Leto", 69, 15, 0, 2, 2,125, 100, 1)
                .wrapTrait(Trait.SPIRIT)
                .wrapTrait(Trait.ARCHER)
                .wrapSkill(CreatureSkillFactory.letoSkill());
    }

    public static Creature annie() {
        return Creature.withStats("Annie", 75, 14, 2, 0, 1, 115, 100, 1)
                .wrapTrait(Trait.PIRATE)
                .wrapTrait(Trait.DUELIST)
                .wrapSkill(CreatureSkillFactory.annieSkill());
    }

    public static Creature bolver() {
        return Creature.withStats("Bolver", 101, 12, 2, 3, 2, 85, 100, 1)
                .wrapTrait(Trait.CULTIST)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.bolverSkill());
    }

    public static Creature shaya() {
        return Creature.withStats("Shaya", 82, 11, 0, 3, 6, 105, 100, 1)
                .wrapTrait(Trait.STUDENT)
                .wrapTrait(Trait.SUMMONER)
                .wrapSkill(CreatureSkillFactory.shayaSkill());
    }

    public static Creature rover() {
        return Creature.withStats("Rover", 134, 14, 4, 4, 5, 100, 100, 2)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.DEFENDER)
                .wrapTrait(Trait.SUPPORT)
                .wrapSkill(CreatureSkillFactory.roverSkill());
    }

    public static Creature cathyra() {
        return Creature.withStats("Cathyra", 104, 15, 2, 1, 3, 130, 100, 2)
                .wrapTrait(Trait.FIREBORN)
                .wrapTrait(Trait.ASSASSIN)
                .wrapSkill(CreatureSkillFactory.cathyraSkill());
    }

    public static Creature coldy() {
        return Creature.withStats("Coldy", 133, 14, 4, 2, 5, 95, 100, 2)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.UNDEAD)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.coldySkill());
    }

    public static Creature jack() {
        return Creature.withStats("Jack", 100, 14, 3, 2, 7, 115, 100, 2)
                .wrapTrait(Trait.PIRATE)
                .wrapTrait(Trait.SUMMONER)
                .wrapSkill(CreatureSkillFactory.jackSkill());
    }

    public static Creature aralis() {
        return Creature.withStats("Aralis", 98, 17, 1, 3, 6, 120, 100, 2)
                .wrapTrait(Trait.DEMON)
                .wrapTrait(Trait.ARCHER)
                .wrapSkill(CreatureSkillFactory.aralisSkill());
    }

    public static Creature weiss() {
        return Creature.withStats("Weiss", 113, 15, 2, 5, 10, 110, 90, 2)
                .wrapTrait(Trait.CULTIST)
                .wrapTrait(Trait.ALCHEMIST)
                .wrapSkill(CreatureSkillFactory.weissSkill())
                .wrapBouncingSkill(CreatureSkillFactory.weissBouncingSkill());
    }

    public static Creature aramis() {
        return Creature.withStats("Aramis", 115, 17, 2, 3, 5, 117, 100, 2)
                .wrapTrait(Trait.KNIGHT)
                .wrapTrait(Trait.DUELIST)
                .wrapSkill(CreatureSkillFactory.aramisSkill());
    }

    public static Creature heshi() {
        return Creature.withStats("Heshi", 120, 14, 4, 1, 6, 95, 120, 2)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.BEAST)
                .wrapSkill(CreatureSkillFactory.heshiSkill());
    }

    public static Creature cook() {
        return Creature.withStats("Cook", 142, 13, 3, 3, 6, 97, 100, 2)
                .wrapTrait(Trait.PIRATE)
                .wrapTrait(Trait.EATER)
                .wrapTrait(Trait.SUPPORT)
                .wrapSkill(CreatureSkillFactory.cookSkill());
    }

    public static Creature dummy() {
        return Creature.withStats("Dummy", 200, 5, 0, 0, 0, 100, 100, 1)
                .wrapSkill(CreatureSkillFactory.dunkanSkill());
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
            case "Jack": {
                return jack();
            }
            case "Aralis": {
                return aralis();
            }
            case "Weiss": {
                return weiss();
            }
            case "Aramis": {
                return aramis();
            }
            case "Heshi": {
                return heshi();
            }
            case "Cook": {
                return cook();
            }
            case "Dummy": {
                return dummy();
            }
        }
        return Creature.shopDummy();
    }
}

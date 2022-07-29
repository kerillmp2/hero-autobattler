package core.creature;

import java.util.List;

import core.controllers.LevelController;
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
        return Creature.withStats("Dunkan", 500, 39, 16, 10, 8, 100, 100, 1)
                .wrapTrait(Trait.WARRIOR)
                .wrapTrait(Trait.DEFENDER)
                .wrapSkill(CreatureSkillFactory.dunkanSkill());
    }

    public static Creature salvira() {
        return Creature.withStats("Salvira", 380, 45, 10, 10, 8, 120, 80, 1)
                .wrapTrait(Trait.ASSASSIN)
                .wrapTrait(Trait.POISONOUS)
                .wrapSkill(CreatureSkillFactory.salviraSkill());
    }

    public static Creature ignar() {
        return Creature.withStats("Ignar", 560, 38, 16, 14, 15, 80, 100, 1)
                .wrapTrait(Trait.DEMON)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.ignarSkill());
    }

    public static Creature warbot() {
        return Creature.withStats("Warbot", 500, 34, 16, 8, 10, 90, 100, 1)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.warbotSkill());
    }

    public static Creature kodji() {
        return Creature.withStats("Kodji", 360, 32, 14, 20, 20, 105, 40, 1)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.MAGE)
                .wrapSkill(CreatureSkillFactory.kodjiSkill());
    }

    public static Creature mira() {
        return Creature.withStats("Mira", 390, 37, 15, 20, 20, 108, 90, 1)
                .wrapTrait(Trait.ALCHEMIST)
                .wrapTrait(Trait.STUDENT)
                .wrapTrait(Trait.SUPPORT)
                .wrapSkill(CreatureSkillFactory.miraSkill())
                .wrapBouncingSkill(CreatureSkillFactory.miraBouncingSkill());
    }

    public static Creature obby() {
        return Creature.withStats("Obby", 440, 36, 18, 15, 16, 110, 100, 1)
                .wrapTrait(Trait.FIREBORN)
                .wrapTrait(Trait.BEAST)
                .wrapSkill(CreatureSkillFactory.obbySkill());
    }

    public static Creature leto() {
        return Creature.withStats("Leto", 360, 52, 15, 20, 14,125, 100, 1)
                .wrapTrait(Trait.SPIRIT)
                .wrapTrait(Trait.ARCHER)
                .wrapSkill(CreatureSkillFactory.letoSkill());
    }

    public static Creature annie() {
        return Creature.withStats("Annie", 390, 42, 15, 12, 10, 115, 100, 1)
                .wrapTrait(Trait.PIRATE)
                .wrapTrait(Trait.DUELIST)
                .wrapSkill(CreatureSkillFactory.annieSkill());
    }

    public static Creature bolver() {
        return Creature.withStats("Bolver", 560, 30, 13, 10, 14, 85, 100, 1)
                .wrapTrait(Trait.CULTIST)
                .wrapTrait(Trait.EATER)
                .wrapSkill(CreatureSkillFactory.bolverSkill());
    }

    public static Creature shaya() {
        return Creature.withStats("Shaya", 330, 30, 14, 18, 20, 105, 100, 1)
                .wrapTrait(Trait.STUDENT)
                .wrapTrait(Trait.SUMMONER)
                .wrapSkill(CreatureSkillFactory.shayaSkill());
    }

    public static Creature rover() {
        return Creature.withStats("Rover", 700, 52, 20, 15, 10, 100, 100, 2)
                .wrapTrait(Trait.ROBOT)
                .wrapTrait(Trait.DEFENDER)
                .wrapTrait(Trait.SUPPORT)
                .wrapSkill(CreatureSkillFactory.roverSkill());
    }

    public static Creature cathyra() {
        return Creature.withStats("Cathyra", 420, 57, 16, 14, 14, 130, 100, 2)
                .wrapTrait(Trait.FIREBORN)
                .wrapTrait(Trait.ASSASSIN)
                .wrapSkill(CreatureSkillFactory.cathyraSkill());
    }

    public static Creature coldy() {
        return Creature.withStats("Coldy", 650, 55, 18, 15, 10, 95, 100, 2)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.UNDEAD)
                .wrapTrait(Trait.WARRIOR)
                .wrapSkill(CreatureSkillFactory.coldySkill());
    }

    public static Creature jack() {
        return Creature.withStats("Jack", 440, 42, 17, 17, 18, 115, 100, 2)
                .wrapTrait(Trait.PIRATE)
                .wrapTrait(Trait.SUMMONER)
                .wrapSkill(CreatureSkillFactory.jackSkill());
    }

    public static Creature aralis() {
        return Creature.withStats("Aralis", 460, 58, 14, 17, 15, 120, 100, 2)
                .wrapTrait(Trait.DEMON)
                .wrapTrait(Trait.ARCHER)
                .wrapSkill(CreatureSkillFactory.aralisSkill());
    }

    public static Creature weiss() {
        return Creature.withStats("Weiss", 520, 45, 14, 22, 24, 110, 90, 2)
                .wrapTrait(Trait.CULTIST)
                .wrapTrait(Trait.ALCHEMIST)
                .wrapSkill(CreatureSkillFactory.weissSkill())
                .wrapBouncingSkill(CreatureSkillFactory.weissBouncingSkill());
    }

    public static Creature aramis() {
        return Creature.withStats("Aramis", 570, 54, 17, 12, 10, 117, 100, 2)
                .wrapTrait(Trait.KNIGHT)
                .wrapTrait(Trait.DUELIST)
                .wrapSkill(CreatureSkillFactory.aramisSkill());
    }

    public static Creature heshi() {
        return Creature.withStats("Heshi", 600, 38, 20, 14, 10, 95, 120, 2)
                .wrapTrait(Trait.FROSTBORN)
                .wrapTrait(Trait.BEAST)
                .wrapSkill(CreatureSkillFactory.heshiSkill());
    }

    public static Creature cook() {
        return Creature.withStats("Cook", 660, 48, 17, 19, 16, 97, 100, 2)
                .wrapTrait(Trait.PIRATE)
                .wrapTrait(Trait.EATER)
                .wrapTrait(Trait.SUPPORT)
                .wrapSkill(CreatureSkillFactory.cookSkill());
    }

    public static Creature dummy() {
        return Creature.withStats("Dummy", 1000, 5, 50, 0, 0, 0, 100, 1)
                .wrapTrait(Trait.SPIRIT)
                .wrapSkill(CreatureSkillFactory.miraSkill());
    }

    // Summoners trait Creature
    public static Creature impy() {
        return Creature.withStats("Impy", 120, 25, 7, 10, 15, 90, 80, 1)
                .wrapSkill(CreatureSkillFactory.impySkill());
    }

    public static Creature creatureByName(String name, int level) {
        Creature initCreature = creatureByName(name);
        while (initCreature.getLevel() < level) {
            int currentLevel = initCreature.getLevel();
            initCreature.setLevel(currentLevel + 1);
            LevelController.levelUpCreature(initCreature, initCreature.getLevel(), true);
        }
        return initCreature;
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

    public static Creature creatureByTrait(Trait trait) {
        List<Creature> creatures = CreaturePool.getPlayerCreaturesWithCost(1);
        creatures.addAll(CreaturePool.getPlayerCreaturesWithCost(2));
        creatures.addAll(CreaturePool.getPlayerCreaturesWithCost(3));
        creatures.addAll(CreaturePool.getPlayerCreaturesWithCost(4));
        creatures.addAll(CreaturePool.getPlayerCreaturesWithCost(5));
        for (Creature creature : creatures) {
            if (creature.hasTrait(trait)) {
                return creature;
            }
        }
        return Creature.shopDummy();
    }
}

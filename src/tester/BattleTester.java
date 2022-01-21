package tester;

import java.util.ArrayList;
import java.util.List;

import core.battle.BattleStatus;
import core.controllers.BattleController;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;

public class BattleTester {

    public static BattleStatus testBattleWithCreatures(List<Creature> firstSide, List<Creature> secondSide) {
        Player p1 = Player.newPlayerWithName("Player 1");
        Player p2 = Player.newPlayerWithName("Player 2");
        for (Creature creature : firstSide) {
            p1.getBoardController().addCreature(creature);
        }
        for (Creature creature : secondSide) {
            p2.getBoardController().addCreature(creature);
        }
        return BattleController.processBattleForPlayers(p1, p2);
    }

    public static BattleStatus testBattleWithCreatures(Creature creature, Creature opponent) {
        List<Creature> creatures = new ArrayList<>();
        creatures.add(creature);
        List<Creature> opponents = new ArrayList<>();
        opponents.add(opponent);
        return testBattleWithCreatures(creatures, opponents);
    }

    public static void testCreatureSolo(Creature creature) {
        int wins = 0;
        int loses = 0;
        int draws = 0;
        int turn_limits = 0;
        List<Creature> opponents = CreaturePool.getPlayerCreaturesWithCost(creature.getCost());
        for (Creature opponent : opponents) {
            BattleStatus battleStatus = testBattleWithCreatures(creature, opponent);
            switch (battleStatus) {
                case DRAW:
                    draws++;
                    break;
                case FIRST_PLAYER_WIN:
                    wins++;
                    break;
                case SECOND_PLAYER_WIN:
                    loses++;
                    break;
                case TURN_LIMIT_REACHED:
                    turn_limits++;
                    break;
            }
        }
        MessageController.forcedPrint("Результаты тестирования " + creature.getName() + ":");
        MessageController.forcedPrint("Победы: " + wins);
        MessageController.forcedPrint("Поражения: " + loses);
        MessageController.forcedPrint("Ничьи: " + draws);
        MessageController.forcedPrint("Лимиты ходов: " + turn_limits + "\n");
    }

    public static void testCreaturesWithCost(int cost) {
        List<Creature> creatures = CreaturePool.getPlayerCreaturesWithCost(cost);
        for (Creature creature : creatures) {
            testCreatureSolo(creature);
        }
    }

    public static void testPair(Creature firstCreature, Creature secondCreature, int times) {
        List<Creature> testSide = new ArrayList<>();
        testSide.add(firstCreature);
        testSide.add(secondCreature);
        List<List<Creature>> compositions = new ArrayList<>();
        List<Creature> firstCostCreatures = CreaturePool.getPlayerCreaturesWithCost(firstCreature.getCost());
        List<Creature> secondCostCreatures = CreaturePool.getPlayerCreaturesWithCost(secondCreature.getCost());

        for (Creature creature_1 : firstCostCreatures) {
            for (Creature creature_2 : secondCostCreatures) {
                if (!creature_1.getName().equals(creature_2.getName())) {
                    List<Creature> composition = new ArrayList<>();
                    composition.add(creature_1);
                    composition.add(creature_2);
                    compositions.add(composition);
                }
            }
        }

        int wins = 0;
        int loses = 0;
        int draws = 0;
        int turn_limits = 0;
        for (int i = 0; i < times; i++) {
            for (List<Creature> opponents : compositions) {
                BattleStatus battleStatus = testBattleWithCreatures(testSide, opponents);
                switch (battleStatus) {
                    case DRAW:
                        draws++;
                        break;
                    case FIRST_PLAYER_WIN:
                        wins++;
                        break;
                    case SECOND_PLAYER_WIN:
                        loses++;
                        break;
                    case TURN_LIMIT_REACHED:
                        turn_limits++;
                        break;
                }
            }
        }
        MessageController.forcedPrint("Результаты тестирования " + testSide + ":");
        MessageController.forcedPrint("Победы: " + wins);
        MessageController.forcedPrint("Поражения: " + loses);
        MessageController.forcedPrint("Ничьи: " + draws);
        MessageController.forcedPrint("Лимиты ходов: " + turn_limits + "\n");
    }

    public static void testPairsWithCost(int firstCost, int secondCost, int times) {
        List<Creature> firstCostCreatures = CreaturePool.getPlayerCreaturesWithCost(firstCost);
        List<Creature> secondCostCreatures = CreaturePool.getPlayerCreaturesWithCost(secondCost);
        for (Creature firstCreature : firstCostCreatures) {
            for (Creature secondCreature : secondCostCreatures) {
                if (!firstCreature.getName().equals(secondCreature.getName())) {
                    testPair(firstCreature, secondCreature, times);
                }
            }
        }
    }

    public static void testCreature(Creature creature) {
        List<Creature> firstCostCreatures = new ArrayList<>();
        testCreatureSolo(creature);
        firstCostCreatures.add(creature);
        List<Creature> secondCostCreatures = CreaturePool.getPlayerCreaturesWithCost(creature.getCost());
        for (Creature firstCreature : firstCostCreatures) {
            for (Creature secondCreature : secondCostCreatures) {
                if (!firstCreature.getName().equals(secondCreature.getName())) {
                    testPair(firstCreature, secondCreature, 2);
                }
            }
        }
    }
}

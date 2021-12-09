package tester;

import java.util.ArrayList;
import java.util.List;

import core.battle.BattleStatus;
import core.battlefield.Position;
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
            p1.getBoard().addCreature(creature, Position.FIRST_LINE);
        }
        for (Creature creature : secondSide) {
            p2.getBoard().addCreature(creature, Position.FIRST_LINE);
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

    public static void testCreature(Creature creature) {
        List<Creature> opponents = CreaturePool.getCreaturesWithCost(creature.getCost());
        int wins = 0;
        int loses = 0;
        int draws = 0;
        int turn_limits = 0;
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
        List<Creature> creatures = CreaturePool.getCreaturesWithCost(cost);
        for (Creature creature : creatures) {
            testCreature(creature);
        }
    }

    public static void testPair(Creature firstCreature, Creature secondCreature, int times) {
        List<Creature> testSide = new ArrayList<>();
        testSide.add(firstCreature);
        testSide.add(secondCreature);
        List<List<Creature>> compositions = new ArrayList<>();
        List<Creature> firstCostCreatures = CreaturePool.getCreaturesWithCost(firstCreature.getCost());
        List<Creature> secondCostCreatures = CreaturePool.getCreaturesWithCost(secondCreature.getCost());

        for (Creature creature_1 : firstCostCreatures) {
            for (Creature creature_2 : secondCostCreatures) {
                List<Creature> composition = new ArrayList<>();
                composition.add(creature_1);
                composition.add(creature_2);
                compositions.add(composition);
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
        List<Creature> firstCostCreatures = CreaturePool.getCreaturesWithCost(firstCost);
        List<Creature> secondCostCreatures = CreaturePool.getCreaturesWithCost(secondCost);
        for (Creature firstCreature : firstCostCreatures) {
            for (Creature secondCreature : secondCostCreatures) {
                testPair(firstCreature, secondCreature, times);
            }
        }
    }
}

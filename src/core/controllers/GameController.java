package core.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import AI.AI;
import AI.AIController;
import core.battle.BattleStatus;
import core.controllers.utils.MessageController;
import core.creature.CreaturePool;
import core.player.Player;
import core.player.PlayerState;
import core.viewers.StatisticViewer;
import statistics.Metric;
import statistics.StatisticCollector;
import utils.Constants;
import utils.Pair;

public class GameController {
    private List<PlayerController> playerControllers;
    private int currentTurn;

    private GameController(List<PlayerController> playerControllers, int currentTurn) {
        this.playerControllers = playerControllers;
        this.currentTurn = currentTurn;
    }

    public static GameController forPlayers(Player... players) {
        CreaturePool defaultCreaturePoolForPlayers = CreaturePool.forPlayer();
        List<PlayerController> playerControllers = new ArrayList<>();
        for (Player player : players) {
            if (player instanceof AI) {
                playerControllers.add(AIController.defaultController(player, defaultCreaturePoolForPlayers));
            } else {
                playerControllers.add(PlayerController.defaultController(player, defaultCreaturePoolForPlayers));
            }
        }
        return new GameController(playerControllers, 1);
    }

    public void startGame() {
        List <Player> alivePlayers = getAlivePlayers();
        while (alivePlayers.size() >= 2) {
            turnsProcessing();
            alivePlayers = getAlivePlayers();
            if (currentTurn >= 30) {
                MessageController.print(
                        "Достигнут лимит ходов!",
                        "Turns limit reached!"
                );
            }
        }
        if (alivePlayers.size() == 0) {
            MessageController.print(
                    "Ничья! Никто не выиграл, никто не проиграл",
                    "Draw!"
            );
            return;
        }
        for (Player player : alivePlayers) {
            MessageController.print(
                    player.getName() + " победил!",
                    player.getName() + " won the game!"
            );
        }
    }

    private void turnsProcessing() {
        MessageController.print("Turn " + currentTurn + " has began!");

        for (PlayerController currentPlayerController : playerControllers) {
            if (currentPlayerController.getPlayer().getState() != PlayerState.DEAD
                    && currentPlayerController.getPlayer().getState() != PlayerState.READY_FOR_BATTLE) {
                MessageController.print("Turn of the player " + currentPlayerController.getPlayer().getName());
                currentPlayerController.processTurnPhase();
            }
        }

        List<Pair<PlayerController, PlayerController>> battlePairs = generateBattlePairs();
        for (Pair<PlayerController, PlayerController> battlePair : battlePairs) {
            if (Constants.EACH_BATTLE_STATISTIC.value > 0) {
                StatisticCollector.init();
            }
            BattleStatus battleStatus = processBattleForPlayers(battlePair.first, battlePair.second);
            Player firstPlayer = battlePair.first.getPlayer();
            Player secondPlayer = battlePair.second.getPlayer();
            switch (battleStatus) {
                case FIRST_PLAYER_WIN:
                    MessageController.print(firstPlayer.getName() + " won versus " + secondPlayer.getName());
                    dealDamageToPlayer(secondPlayer, 2);
                    break;
                case SECOND_PLAYER_WIN:
                    MessageController.print(secondPlayer.getName() + " won versus " + firstPlayer.getName());
                    dealDamageToPlayer(firstPlayer, 2);
                    break;
                case TURN_LIMIT_REACHED:
                    MessageController.print("Turns limit reached!");
                case DRAW:
                    MessageController.print("Draw in battle between " + firstPlayer.getName() + " and " + secondPlayer.getName());
            }
            if (Constants.EACH_BATTLE_STATISTIC.value > 0) {
                MessageController.forcedPrint(StatisticViewer.getStatisticView(Metric.values()));
            }
            addMoneyToPlayer(firstPlayer, 1 + firstPlayer.getMoney() / 10);
            addMoneyToPlayer(secondPlayer, 1 + secondPlayer.getMoney() / 10);
        }

        MessageController.print("Turn " + currentTurn + " is over!");
        currentTurn++;
    }

    private BattleStatus processBattleForPlayers(PlayerController firstPlayerController, PlayerController secondPlayerController) {
        Player firstPlayer = firstPlayerController.getPlayer();
        Player secondPlayer = secondPlayerController.getPlayer();
        MessageController.print("Battle between " + firstPlayer.getName() + " и " + secondPlayer.getName() + " begins!");
        BattleStatus battleStatus = BattleController.processBattleForPlayers(firstPlayerController, secondPlayerController);
        firstPlayer.setState(PlayerState.NOT_READY_FOR_BATTLE);
        secondPlayer.setState(PlayerState.NOT_READY_FOR_BATTLE);
        return battleStatus;
    }

    private List<Pair<PlayerController, PlayerController>> generateBattlePairs() {
        List<PlayerController> readyForBattlePlayers = getPlayersWithState(PlayerState.READY_FOR_BATTLE);
        List<Pair<PlayerController, PlayerController>> battlePairs = new ArrayList<>();

        while (readyForBattlePlayers.size() >= 2) {
            Collections.shuffle(readyForBattlePlayers);
            PlayerController firstPlayer = readyForBattlePlayers.get(0);
            PlayerController secondPlayer = readyForBattlePlayers.get(1);
            battlePairs.add(new Pair<>(firstPlayer, secondPlayer));
            readyForBattlePlayers.remove(firstPlayer);
            readyForBattlePlayers.remove(secondPlayer);
        }

        return battlePairs;
    }

    private List<Player> getAlivePlayers() {
        return playerControllers.stream().map(PlayerController::getPlayer).filter(player -> player.getState() != PlayerState.DEAD).collect(Collectors.toList());
    }

    private List<PlayerController> getPlayersWithState(PlayerState state) {
        return playerControllers.stream().filter(playerController -> playerController.getPlayer().getState() == state).collect(Collectors.toList());
    }

    private void dealDamageToPlayer(Player player, int amount) {
        player.reduceHp(amount);
        MessageController.print(player.getName() + " received " + amount + " damage!");
        if (player.getState() == PlayerState.DEAD) {
            MessageController.print(player.getName() + " dies!\n");
        }
    }

    private void addMoneyToPlayer(Player player, int amount) {
        player.addMoney(amount);
        MessageController.print(player.getName() + " gains " + amount + " coins!");
    }
}

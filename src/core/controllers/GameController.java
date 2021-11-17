package core.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import core.battle.BattleStatus;
import core.controllers.utils.MessageController;
import core.creature.CreaturePool;
import core.player.Player;
import core.player.PlayerState;
import core.utils.Pair;

public class GameController {
    private List<PlayerController> playerControllers;
    private int currentTurn;

    private GameController(List<PlayerController> playerControllers, int currentTurn) {
        this.playerControllers = playerControllers;
        this.currentTurn = currentTurn;
    }

    public static GameController forPlayers(Player... players) {
        CreaturePool.init();
        return new GameController(Arrays.stream(players).map(PlayerController::defaultController).collect(Collectors.toList()), 1);
    }

    public void startGame() {
        List <Player> alivePlayers = getAlivePlayers();
        while (alivePlayers.size() >= 2) {
            turnsProcessing();
            alivePlayers = getAlivePlayers();
            if (currentTurn > 30) {
                MessageController.print("Лимит ходов достигнут!");
            }
        }
        if (alivePlayers.size() == 0) {
            MessageController.print("Ничья! Никто не выиграл, никто не проиграл");
            return;
        }
        for (Player player : alivePlayers) {
            MessageController.print(player.getName() + " победил!");
        }
    }

    private void turnsProcessing() {
        MessageController.print("Ход " + currentTurn + " начался!\n");

        for(int i = 0; i < playerControllers.size(); i++) {
            PlayerController currentPlayerController = playerControllers.get(i);
            if (currentPlayerController.getPlayer().getState() != PlayerState.DEAD
                    && currentPlayerController.getPlayer().getState() != PlayerState.READY_FOR_BATTLE) {
                MessageController.print("Ход игрока " + currentPlayerController.getPlayer().getName());
                currentPlayerController.processTurnPhase();
            }
        }

        List<Pair<Player, Player>> battlePairs = generateBattlePairs();
        for (Pair<Player, Player> battlePair : battlePairs) {
            BattleStatus battleStatus = processBattleForPlayers(battlePair.first, battlePair.second);
            switch (battleStatus) {
                case FIRST_PLAYER_WIN:
                    MessageController.print(battlePair.first.getName() + " победил " + battlePair.second.getName());
                    dealDamageToPlayer(battlePair.second, 2);
                    addMoneyToPlayer(battlePair.first, 1);
                    break;
                case SECOND_PLAYER_WIN:
                    MessageController.print(battlePair.second.getName() + " победил " + battlePair.first.getName());
                    dealDamageToPlayer(battlePair.first, 2);
                    addMoneyToPlayer(battlePair.second, 1);
                    break;
                case DRAW:
                    MessageController.print("Ничья между " + battlePair.second.getName() + " и " + battlePair.first.getName());
            }
        }

        MessageController.print("Ход " + currentTurn + " окончен!\n");
        currentTurn++;
    }

    private BattleStatus processBattleForPlayers(Player firstPlayer, Player secondPlayer) {
        MessageController.print("Начинается битва между " + firstPlayer.getName() + " и " + secondPlayer.getName());
        BattleStatus battleStatus = BattleController.processBattleForPlayers(firstPlayer, secondPlayer);
        firstPlayer.setState(PlayerState.NOT_READY_FOR_BATTLE);
        secondPlayer.setState(PlayerState.NOT_READY_FOR_BATTLE);
        return battleStatus;
    }

    private List<Pair<Player, Player>> generateBattlePairs() {
        List<Player> readyForBattlePlayers = getPlayersWithState(PlayerState.READY_FOR_BATTLE);
        List<Pair<Player, Player>> battlePairs = new ArrayList<>();

        while (readyForBattlePlayers.size() >= 2) {
            Collections.shuffle(readyForBattlePlayers);
            Player firstPlayer = readyForBattlePlayers.get(0);
            Player secondPlayer = readyForBattlePlayers.get(1);
            battlePairs.add(new Pair<>(firstPlayer, secondPlayer));
            readyForBattlePlayers.remove(firstPlayer);
            readyForBattlePlayers.remove(secondPlayer);
        }

        return battlePairs;
    }

    private List<Player> getAlivePlayers() {
        return playerControllers.stream().map(PlayerController::getPlayer).filter(player -> player.getState() != PlayerState.DEAD).collect(Collectors.toList());
    }

    private List<Player> getPlayersWithState(PlayerState state) {
        return playerControllers.stream().map(PlayerController::getPlayer).filter(player -> player.getState() == state).collect(Collectors.toList());
    }

    private void dealDamageToPlayer(Player player, int amount) {
        player.reduceHp(amount);
        MessageController.print(player.getName() + " получает " + amount + " урона!");
        if (player.getState() == PlayerState.DEAD) {
            MessageController.print(player.getName() + " умирает!\n");
        }
    }

    private void addMoneyToPlayer(Player player, int amount) {
        player.addMoney(amount);
        MessageController.print(player.getName() + " получает " + amount + " монет!");
    }
}

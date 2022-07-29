package core.controllers;

import java.util.Comparator;

import AI.AI;
import core.action.ResolveTime;
import core.battlefield.ObjectStatus;
import core.viewers.BattlefieldViewer;
import core.battle.BattleStatus;
import core.battlefield.BattlefieldCreature;
import core.controllers.utils.MessageController;
import core.player.Player;
import core.battlefield.Battlefield;
import utils.Constants;

public class BattleController {
    private final Battlefield battlefield;
    private final TurnController turnController;

    private BattleController(Battlefield battlefield, TurnController turnController) {
        this.battlefield = battlefield;
        this.turnController = turnController;
    }

    private static BattleController forBattlefield(Battlefield battlefield) {
        return new BattleController(battlefield, TurnController.forBattlefield(battlefield));
    }

    public static BattleStatus processBattleForPlayers(PlayerController firstPlayerController, PlayerController secondPlayerController) {
        Player firstPlayer = firstPlayerController.getPlayer();
        Player secondPLayer = secondPlayerController.getPlayer();
        Battlefield battlefield = Battlefield.fromTwoBoards(firstPlayer.getBoard(), secondPLayer.getBoard());
        BattleController battleController = BattleController.forBattlefield(battlefield);
        battlefield.setBattleController(battleController);
        BattleStatus battleStatus = battleController.battle();
        battleController.onBattleEnd(firstPlayerController, secondPlayerController);
        return battleStatus;
    }

    public BattleStatus battle() {
        onBattleStart();
        while (turnController.getTurnCounter() < Constants.BATTLE_TURN_LIMIT.value) {
            MessageController.print(BattlefieldViewer.getBattleFieldView(battlefield));
            boolean firstSideHasAlive = this.battlefield.getFirstSide().hasAliveCreature();
            boolean secondSideHasAlive = this.battlefield.getSecondSide().hasAliveCreature();
            if (firstSideHasAlive && !secondSideHasAlive) {
                return BattleStatus.FIRST_PLAYER_WIN;
            }
            if (!firstSideHasAlive && secondSideHasAlive) {
                return BattleStatus.SECOND_PLAYER_WIN;
            }
            if (!firstSideHasAlive && !secondSideHasAlive) {
                return BattleStatus.DRAW;
            }
            String message = turnController.nextTurn();
            MessageController.print(message);
        }
        return BattleStatus.TURN_LIMIT_REACHED;
    }

    public void onBattleStart() {
        StringBuilder message = new StringBuilder();
        battlefield.getAllCreatures().stream().filter(c -> c.hasStatuses(ObjectStatus.ALIVE))
                .sorted(Comparator.comparingInt(BattlefieldCreature::getCurrentSpeed))
                .forEach(c -> message.append(ActionController.resolve(c, ResolveTime.ON_BATTLE_START)));
        MessageController.print(message.toString());
    }

    private void onBattleEnd(PlayerController firstPlayerController, PlayerController secondPlayerController) {
        firstPlayerController.onBattleEnd();
        secondPlayerController.onBattleEnd();
        battlefield.getAllCreatures().forEach(BattlefieldCreature::onBattleEnd);
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public TurnController getTurnController() {
        return turnController;
    }
}

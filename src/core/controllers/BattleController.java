package core.controllers;

import core.battle.BattleMap;
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

    public static BattleStatus processBattleForPlayers(Player firstPlayer, Player secondPLayer) {
        Battlefield battlefield = Battlefield.fromTwoBoards(firstPlayer.getBoard(), secondPLayer.getBoard());
        BattleController battleController = BattleController.forBattlefield(battlefield);
        battlefield.setBattleController(battleController);
        BattleStatus battleStatus = battleController.battle();
        battleController.onBattleEnd();
        return battleStatus;
    }

    public BattleStatus battle() {
        while (turnController.getTurnCounter() < Constants.BATTLE_TURN_LIMIT.value) {
            MessageController.print(BattleMap.getBattleFieldView(battlefield));
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

    private void onBattleEnd() {
        battlefield.getAllCreatures().forEach(BattlefieldCreature::onBattleEnd);
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public TurnController getTurnController() {
        return turnController;
    }
}

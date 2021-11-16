package core.battle;

import core.player.Player;
import core.utils.MessageController;
import core.battlefield.Battlefield;

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
        return battleController.battle();
    }

    public BattleStatus battle() {
        while (turnController.getTurnCounter() < 1000) {
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

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public TurnController getTurnController() {
        return turnController;
    }
}

package AI;

import core.controllers.BoardController;
import core.player.Player;
import core.player.PlayerState;
import utils.Constants;

public class AI extends Player {

    protected AI(String name, PlayerState state, int money, int hp, BoardController boardController) {
        super(name, state, money, hp, boardController);
    }

    public static AI newAIWithName(String name) {
        return new AI(name, PlayerState.NOT_READY_FOR_BATTLE, Constants.PLAYER_MONEY.value, Constants.PLAYER_HP.value, AIBoardController.empty(1, Constants.BENCH_SIZE.value));
    }
}

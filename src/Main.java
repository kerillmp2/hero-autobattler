import core.controllers.GameController;
import core.player.Player;
import tester.BattleTester;
import utils.Constants;

public class Main {
    public static void main(String[] args) {

        /*Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 0;
        BattleTester.testCreaturesWithCost(1);
        BattleTester.testPairsWithCost(1, 1, 5);*/

        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 1;
        Player kirill = Player.newPlayerWithName("Player 1");
        Player valera = Player.newPlayerWithName("Player 2");

        GameController gameController = GameController.forPlayers(kirill, valera);
        gameController.startGame();
    }
}

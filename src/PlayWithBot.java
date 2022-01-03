import AI.AI;
import core.controllers.GameController;
import core.player.Player;
import utils.Constants;

public class PlayWithBot {
    public static void main(String[] args) {
        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 1;
        Player kirill = Player.newPlayerWithName("Player 1");
        Player bot = AI.newAIWithName("Bot");

        GameController gameController = GameController.forPlayers(kirill, bot);
        gameController.startGame();
    }
}

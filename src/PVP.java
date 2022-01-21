import core.controllers.GameController;
import core.player.Player;
import statistics.StatisticCollector;
import utils.Constants;

public class PVP {
    public static void main(String[] args) {
        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 1;
        Constants.PRINT_CREATURE_STATS_IN_BATTLE.value = 1;
        Player kirill = Player.newPlayerWithName("Player 1");
        Player valera = Player.newPlayerWithName("Valera");

        StatisticCollector.init();
        StatisticCollector.resetEachBattle();
        GameController gameController = GameController.forPlayers(kirill, valera);
        gameController.startGame();
    }
}

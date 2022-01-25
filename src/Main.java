import AI.AI;
import core.controllers.GameController;
import core.controllers.utils.MessageController;
import core.player.Player;
import core.viewers.StatisticViewer;
import statistics.Metric;
import statistics.StatisticCollector;
import utils.Constants;

public class Main {
    public static void main(String[] args) {
        Player kirill = Player.newPlayerWithName("Player 1");
        Player valera = Player.newPlayerWithName("Player 2");

        Player bot = AI.newAIWithName("Bot");
        Player bot2 = AI.newAIWithName("Bot 2");

        StatisticCollector.init();
        StatisticCollector.resetEachBattle();
        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 0;
        GameController gameController = GameController.forPlayers(bot, bot2);
        gameController.startGame();
        MessageController.forcedPrint(StatisticViewer.getStatisticView(Metric.PHYSICAL_DAMAGE_DEALT, Metric.MAGIC_DAMAGE_DEALT, Metric.TOTAL_DAMAGE_DEALT));
    }
}

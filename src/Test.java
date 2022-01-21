import core.controllers.utils.MessageController;
import core.creature.CreatureFactory;
import core.viewers.StatisticViewer;
import statistics.Metric;
import statistics.StatisticCollector;
import tester.BattleTester;
import utils.Constants;

public class Test {
    public static void main(String[] args) {

        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 0;
        StatisticCollector.init();
        BattleTester.testCreaturesWithCost(2);
        //BattleTester.testPairsWithCost(1, 2, 2);
        MessageController.forcedPrint(StatisticViewer.getStatisticView(Metric.PHYSICAL_DAMAGE_DEALT, Metric.MAGIC_DAMAGE_DEALT, Metric.TOTAL_DAMAGE_DEALT, Metric.DAMAGE_FROM_SKILL));

        //BattleTester.testCreature(CreatureFactory.mira());
        //Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 1;
        //BattleTester.testBattleWithCreatures(CreatureFactory.rover(), CreatureFactory.cathyra());
    }
}

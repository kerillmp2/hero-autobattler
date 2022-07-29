import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureFactory;
import core.item.ItemFactory;
import core.traits.Trait;
import core.viewers.StatisticViewer;
import statistics.Metric;
import statistics.StatisticCollector;
import tester.BattleTester;
import utils.Constants;

public class Test {
    public static void main(String[] args) {

        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 1;
        StatisticCollector.init();
        Creature creature1 = CreatureFactory.creatureByTrait(Trait.SPIRIT);
        ItemFactory.redSoul().equipOn(creature1);
        BattleTester.testBattleWithCreatures(creature1, CreatureFactory.dummy());
        //BattleTester.testCreaturesWithCost(1, 1);
        //BattleTester.testPairsWithCost(2, 2, 2);
        //BattleTester.testPair(CreatureFactory.heshi(), 2, 2);

        //BattleTester.testCreature(CreatureFactory.mira());
        MessageController.forcedPrint(StatisticViewer.getStatisticView(Metric.values()));

    }
}

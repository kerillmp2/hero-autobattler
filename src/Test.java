import tester.BattleTester;
import utils.Constants;

public class Test {
    public static void main(String[] args) {

        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 0;
        BattleTester.testCreaturesWithCost(1);
        BattleTester.testPairsWithCost(1, 1, 4);
    }
}

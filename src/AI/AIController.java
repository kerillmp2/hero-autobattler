package AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import core.controllers.PlayerController;
import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;
import core.shop.CreatureShop;
import utils.Constants;

public class AIController extends PlayerController {

    public AIController(Player player, AICreatureShopController aiCreatureShopController) {
        super(player, aiCreatureShopController);
    }

    public static AIController defaultController(Player player, CreaturePool creaturePool) {
        return new AIController(player, new AICreatureShopController(CreatureShop.defaultCreatureShop(player, creaturePool), player, creaturePool));
    }

    @Override
    public void processTurnPhase() {
        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 0;
        creatureShopController.regenerateShop();
        creatureShopController.refreshShop();
        processShoppingPhase();
        processMoveToBoard();
        creatureShopController.reduceLevelUpCost(2);
        endTurn();
        Constants.PRINT_MESSAGES_IN_CONTROLLER.value = 1;
    }

    @Override
    protected void processShoppingPhase() {
        creatureShopController.shopProcessing();
    }

    @Override
    protected Creature selectCreature(List<Creature> creatures) {
        if (creatures.size() == 0) {
            return null;
        }
        List<Creature> sorted = new ArrayList<>(creatures);
        sorted.sort(Comparator.comparingInt(Creature::getSellingCost));
        return sorted.get(sorted.size() - 1);
    }
}

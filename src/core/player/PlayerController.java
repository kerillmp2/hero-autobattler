package core.player;

import java.util.List;

import core.creature.Creature;
import core.shop.CreatureShop;
import core.shop.ShopController;
import core.utils.Option;
import core.utils.Selector;

public class PlayerController {
    private Player player;
    private ShopController<Creature> creatureShopController;

    private PlayerController(Player player, ShopController<Creature> creatureShopController) {
        this.player = player;
        this.creatureShopController = creatureShopController;
    }

    public static PlayerController defaultController(Player player) {
        return new PlayerController(player, new ShopController<>(CreatureShop.defaultCreatureShop(player), player));
    }

    public void processTurnPhase() {
        int currentOptionNum = -1;
        TurnOption currentOption = TurnOption.DEFAULT;
        creatureShopController.regenerateShop();
        creatureShopController.refreshShop();
        while (currentOption != TurnOption.END_TURN) {
            List<Option> turnOptions = player.getTurnOptions();
            currentOptionNum = Selector.select(turnOptions);
            currentOption = turnOptions.get(currentOptionNum).getTag();
            resolveTurnOption(currentOption);
        }
    }

    private void resolveTurnOption(TurnOption turnOption) {
        switch (turnOption) {
            case OPEN_SHOP:
                player.setState(PlayerState.IN_SHOP);
                processShoppingPhase();
                player.setState(PlayerState.NOT_READY_FOR_BATTLE);
                break;
            case END_TURN:
                endTurn();
                break;
        }
    }

    private void endTurn() {
        player.setState(PlayerState.READY_FOR_BATTLE);
    }

    private void processShoppingPhase() {
        creatureShopController.shopProcessing();
    }

    public Player getPlayer() {
        return player;
    }
}

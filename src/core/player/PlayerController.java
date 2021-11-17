package core.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.creature.Creature;
import core.shop.CreatureShop;
import core.shop.ShopController;
import core.utils.Constants;
import core.utils.HasNameImpl;
import core.utils.MessageController;
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
            List<Option<TurnOption>> turnOptions = player.getTurnOptions();
            MessageController.print("-".repeat(Constants.SHOP_VIEW_SIZE.value));
            currentOptionNum = Selector.select(turnOptions);
            MessageController.print("-".repeat(Constants.SHOP_VIEW_SIZE.value));
            currentOption = TurnOption.byName(turnOptions.get(currentOptionNum).getTag().getName());
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
            case VIEW_BOARD:
                player.setState(PlayerState.VIEW_BOARD);
                processViewBoard();
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

    private void processViewBoard() {
        int selectedNumber = -1;
        while (selectedNumber != 0) {
            BoardViewer.showBoardView(player.getBoard());
            selectedNumber = Selector.select(new Option<>(TurnOption.DEFAULT, "Назад"), new Option<>(TurnOption.DEFAULT, "Продать существо"));
            if (selectedNumber == 1) {
                processSelling();
            }
        }
    }

    private void processSelling() {
        int selectedNumber = -1;
        while (selectedNumber != 0) {
            BoardViewer.showBoardView(player.getBoard());
            HasNameImpl backOption = new HasNameImpl("Назад");
            List<HasNameImpl> options = new ArrayList<>();
            List<Creature> allCreatures = player.getBoard().getAllCreatures();
            options.add(backOption);
            options.addAll(
                    allCreatures.stream().map(creature -> new HasNameImpl(creature.getShopView())).collect(Collectors.toList())
            );
            selectedNumber = Selector.select(options);
            if (selectedNumber != 0) {
                creatureShopController.sellItem(allCreatures.get(selectedNumber - 1));
                return;
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}

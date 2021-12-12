package core.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.viewers.BoardViewer;
import core.player.Player;
import core.player.PlayerState;
import core.player.TurnOption;
import core.shop.CreatureShop;
import core.viewers.ListViewer;
import core.viewers.TurnOptionsViewer;
import utils.HasNameImpl;
import utils.Option;
import utils.Selector;

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
            MessageController.print(TurnOptionsViewer.getOptionsView(turnOptions));
            currentOptionNum = Selector.select(turnOptions);
            currentOption = TurnOption.byName(turnOptions.get(currentOptionNum).getTag().getName());
            resolveTurnOption(currentOption);
        }
        creatureShopController.reduceLevelUpCost(2);
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
            case SELL_CREATURE:
                processSelling();
                break;
            case MOVE_TO_BOARD:
                processMoveToBoard();
                break;
            case MOVE_FROM_BOARD:
                processMoveFromBoard();
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
        while (true) {
            BoardViewer.showBoardView(player.getBoard(), player.getBench(), player.getBoard().getMaxSize());
            List<Option<TurnOption>> options = new ArrayList<>();
            options.add(new Option<>(TurnOption.DEFAULT, "Back"));
            options.add(new Option<>(TurnOption.MOVE_FROM_BOARD, "Remove creature from board"));
            options.add(new Option<>(TurnOption.MOVE_TO_BOARD, "Put creature on board"));
            options.add(new Option<>(TurnOption.SELL_CREATURE, "Sell creature"));
            MessageController.print(TurnOptionsViewer.getOptionsView(options));
            selectedNumber = Selector.select(options);
            if (selectedNumber == 0) {
                break;
            }
            TurnOption currentOption = TurnOption.byName(options.get(selectedNumber).getTag().getName());
            resolveTurnOption(currentOption);
        }
    }

    private Creature selectCreature() {
        int selectedNumber = -1;
        Creature selectedCreature = null;
        while (selectedNumber != 0) {
            BoardViewer.showBoardView(player.getBoard(), player.getBench(), player.getBoard().getMaxSize());
            List<Creature> allBoardCreatures = player.getBoard().getAllCreatures();
            List<Creature> allBenchCreatures = player.getBench().getCreaturesWithDummys();
            List<HasNameImpl> boardCreatures = allBoardCreatures.stream().map(creature -> new HasNameImpl(creature.getShopView())).collect(Collectors.toList());
            List<HasNameImpl> benchCreatures = allBenchCreatures.stream().map(creature -> new HasNameImpl(creature.getShopView())).collect(Collectors.toList());
            MessageController.print(BoardViewer.benchBoardSimpleView(allBoardCreatures, allBenchCreatures));
            selectedNumber = Selector.creatureSellingSelect(boardCreatures, benchCreatures);
            if (selectedNumber != 0) {
                selectedNumber--;
                if (selectedNumber < allBoardCreatures.size()) {
                    selectedCreature = allBoardCreatures.get(selectedNumber);
                    break;
                } else {
                    selectedNumber -= allBoardCreatures.size();
                    if (selectedNumber < allBenchCreatures.size()) {
                        selectedCreature = allBenchCreatures.get(selectedNumber);
                        break;
                    }
                }
            }
        }
        return selectedCreature;
    }

    private void processMoveToBoard() {
        Creature creature = selectCreature(player.getBench().getCreaturesWithDummys());
        if (creature == null) {
            return;
        }
        if (creature.getName().equals("Пусто") || creature.getName().equals("Empty")) {
            return;
        }
        if (!player.getBoardController().canAddCreatureToBoard()) {
            MessageController.print(
                    "Нет места на доске!",
                    "Your board is full!"
            );
            return;
        }
        player.getBoardController().removeCreatureFromBench(creature);
        player.getBoardController().addCreatureToBoard(creature);
        MessageController.print(
                creature.getName() + " выставлен на доску\n",
                creature.getName() + " put on the board\n"
        );
    }

    private void processMoveFromBoard() {
        Creature creature = selectCreature(player.getBoard().getAllCreatures());
        if (creature == null) {
            return;
        }
        if (player.getBoardController().canAddCreatureToBench()) {
            player.getBoardController().addCreatureToBench(creature);
            player.getBoardController().removeCreatureFromBoard(creature);
            MessageController.print(
                    creature.getName() + " возвращается на скамейку\n",
                    creature.getName() + " returned to the bench\n"
            );
        } else {
            MessageController.print(
                    "Нет места на скамейке!\n",
                    "Your bench is full!\n"
            );
        }
    }

    private Creature selectCreature(List<Creature> creatures) {
        int selectedNumber = -1;
        Creature selectedCreature = null;
        while (selectedNumber != 0) {
            List<HasNameImpl> boardCreatures = creatures.stream().map(creature -> new HasNameImpl(creature.getShopView())).collect(Collectors.toList());
            MessageController.print(ListViewer.viewList(creatures.stream().map(Creature::getShopView).collect(Collectors.toList()), true));
            selectedNumber = Selector.creatureSellingSelect(boardCreatures);
            if (selectedNumber != 0) {
                selectedNumber--;
                if (selectedNumber < creatures.size()) {
                    selectedCreature = creatures.get(selectedNumber);
                    break;
                }
            }
        }
        return selectedCreature;
    }

    private void processSelling() {
        Creature creature = selectCreature();
        if (creature != null) {
            if (creatureShopController.sellCreature(creature)) {
                MessageController.print(creature.getNameLevel() + " sold for " + creature.getSellingCost() + " gold");
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}
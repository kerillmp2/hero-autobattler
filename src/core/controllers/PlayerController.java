package core.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.battlefield.Position;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.player.Bench;
import core.player.Board;
import core.player.BoardViewer;
import core.player.Player;
import core.player.PlayerState;
import core.player.TurnOption;
import core.shop.CreatureShop;
import utils.Constants;
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
            MessageController.print("-".repeat(Constants.SHOP_VIEW_SIZE.value));
            currentOptionNum = Selector.select(turnOptions);
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
            BoardViewer.showBoardView(player.getBoard(), player.getBench(), player.getCreatureShopLevel());
            List<Option<TurnOption>> options = new ArrayList<>();
            options.add(new Option<>(TurnOption.DEFAULT, "Назад"));
            options.add(new Option<>(TurnOption.MOVE_FROM_BOARD, "Убрать с доски"));
            options.add(new Option<>(TurnOption.MOVE_TO_BOARD, "Выставить на доску"));
            options.add(new Option<>(TurnOption.SELL_CREATURE, "Продать существо"));
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
            BoardViewer.showBoardView(player.getBoard(), player.getBench(), player.getCreatureShopLevel());
            List<Creature> allBoardCreatures = player.getBoard().getAllCreatures();
            List<Creature> allBenchCreatures = player.getBench().getCreaturesWithDummys();
            List<HasNameImpl> boardCreatures = allBoardCreatures.stream().map(creature -> new HasNameImpl(creature.getShopView())).collect(Collectors.toList());
            List<HasNameImpl> benchCreatures = allBenchCreatures.stream().map(creature -> new HasNameImpl(creature.getShopView())).collect(Collectors.toList());
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
        if (creature.getName().equals("Пусто")) {
            return;
        }
        if (player.getBoard().getAllCreatures().size() >= player.getCreatureShopLevel()) {
            MessageController.print("Нет места на доске!");
            return;
        }
        player.getBench().removeCreature(creature);
        player.getBoard().addCreature(creature, Position.FIRST_LINE);
        MessageController.print(creature.getName() + " выставлен на доску\n");
    }

    private void processMoveFromBoard() {
        Creature creature = selectCreature(player.getBoard().getAllCreatures());
        if (creature == null) {
            return;
        }
        if (player.getBench().addCreature(creature)) {
            player.getBoard().removeCreature(creature);
            MessageController.print(creature.getName() + " возвращается на скамейку\n");
        } else {
            MessageController.print("Нет места на скамейке!\n");
        }
    }

    private Creature selectCreature(List<Creature> creatures) {
        int selectedNumber = -1;
        Creature selectedCreature = null;
        while (selectedNumber != 0) {
            List<HasNameImpl> boardCreatures = creatures.stream().map(creature -> new HasNameImpl(creature.getShopView())).collect(Collectors.toList());
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

    private void processSwapping() {
        Creature firstCreature = selectCreature();
        Creature secondCreature = selectCreature();
        Board board = player.getBoard();
        Bench bench = player.getBench();
        if (firstCreature != null && secondCreature != null) {
            if (board.hasCreature(firstCreature)) {
                if (board.hasCreature(secondCreature)) {
                    return;
                }
                if (bench.hasCreature(secondCreature)) {
                    int position = bench.positionOf(secondCreature);
                    bench.removeCreature(secondCreature);
                    board.removeCreature(firstCreature);
                    bench.addCreature(firstCreature, position);
                    board.addCreature(secondCreature, Position.FIRST_LINE);
                    return;
                }
            }
            if (bench.hasCreature(firstCreature)) {
                if (board.hasCreature(secondCreature)) {
                    int position = bench.positionOf(firstCreature);
                    bench.removeCreature(firstCreature);
                    board.removeCreature(secondCreature);
                    bench.addCreature(secondCreature, position);
                    board.addCreature(firstCreature, Position.FIRST_LINE);
                    return;
                }
                if (bench.hasCreature(secondCreature)) {
                    int f_position = bench.positionOf(firstCreature);
                    int s_position = bench.positionOf(secondCreature);
                    bench.removeCreature(firstCreature);
                    bench.removeCreature(secondCreature);
                    bench.addCreature(firstCreature, s_position);
                    bench.addCreature(secondCreature, f_position);
                }
            }
        }
    }

    private void processSelling() {
        Creature creature = selectCreature();
        if (creature != null) {
            creatureShopController.sellCreature(creature);
        }
    }

    public Player getPlayer() {
        return player;
    }
}
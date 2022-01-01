package core.controllers;

import java.util.List;
import java.util.stream.Collectors;

import core.creature.Creature;
import core.creature.CreatureFactory;
import core.creature.CreaturePool;
import core.player.Player;
import core.controllers.utils.MessageController;
import core.shop.HasShopView;
import core.shop.Shop;
import core.shop.ShopItem;
import core.viewers.ShopViewer;
import utils.Pair;
import utils.Selector;

public class ShopController<T extends HasShopView> {
    protected Shop<T> shop;
    protected Player player;
    protected int refreshCost = 2;
    protected int levelUpCost;
    protected CreaturePool creaturePool;

    public ShopController(Shop<T> shop, Player player, CreaturePool creaturePool) {
        this.shop = shop;
        this.player = player;
        this.levelUpCost = generateLevelUpCost(player.getBoard().getMaxSize());
        this.creaturePool = creaturePool;
    }

    public void shopProcessing() {
        List<? extends HasShopView> items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
        int selectedNumber = -1;
        while (selectedNumber != 0) {
            Pair<String, Integer> refreshOption = new Pair<>("Refresh", refreshCost);
            Pair<String, Integer> levelUpOption = new Pair<>("Level up", levelUpCost);
            MessageController.print(ShopViewer.getShopView(player.getBoard().getMaxSize(), player.getMoney(), items, refreshOption, levelUpOption));
            selectedNumber = Selector.shopSelect(items, refreshOption, levelUpOption);
            if (selectedNumber != 0) {
                if (selectedNumber - 1 == items.size()) {
                    if (player.getMoney() >= refreshCost) {
                        player.reduceMoney(refreshCost);
                        regenerateShop();
                        refreshShop();
                        items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
                    } else {
                        MessageController.print(
                                "Недостаточно монет!\n",
                                "Not enough coins!\n"
                        );
                    }
                    continue;
                }
                if (selectedNumber - 2 == items.size()) {
                    if (player.getMoney() >= levelUpCost) {
                        player.reduceMoney(levelUpCost);
                        regenerateShop();
                        levelUp();
                    } else {
                        MessageController.print(
                                "Недостаточно монет!\n",
                                "Not enough coins!\n"
                        );
                    }
                    continue;
                }
                boolean isOperationSuccess = tryToBuyItemFromLine(selectedNumber - 1);
                if (isOperationSuccess) {
                    items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
                }
            }
        }
    }

    public void regenerateShop() {
        shop.regenerate();
    }

    public void refreshShop() {
        shop.refreshLine();
    }

    protected boolean tryToBuyItemFromLine(int position) {
        int lineLength = shop.getCurrentLine().items.size();
        if (position >= lineLength) {
            return false;
        } else {
            return tryToBuyItem(shop.getCurrentLine().items.get(position));
        }
    }

    protected boolean tryToBuyItem(ShopItem<T> shopItem) {
        int cost = shopItem.getCost();
        if (player.hasMoney(cost)) {
            if (!shopItem.getItem().getName().equals("Продано") && !shopItem.getItem().getName().equals("Sold")) {
                player.reduceMoney(cost);
                boolean isOperationSuccess = buyItem(shopItem);
                if (isOperationSuccess) {
                    MessageController.print(
                            player.getName() + " купил " + shopItem.getItem().getName(),
                            player.getName() + " bought " + shopItem.getItem().getName()
                    );
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            MessageController.print(
                    "Недостаточно золота для покупки " + shopItem.getItem().getName() + "\n",
                    "Not enough gold for " + shopItem.getItem().getName() + "\n"
            );
        }
        return false;
    }

    protected boolean buyItem(ShopItem<T> shopItem) {
        if (shopItem.getItem() instanceof Creature) {
            return buyCreature((ShopItem<Creature>) shopItem);
        }
        return false;
    }

    protected boolean buyCreature(ShopItem<Creature> shopItem) {
        if (player.getBoardController().addCreature(shopItem.getItem())) {
            creaturePool.removeCreature(shopItem.getItem());
            int index = shop.currentLine.items.indexOf(shopItem);
            shop.changeItemToDummy(index);
            return true;
        } else {
            MessageController.print(
                    "Ваше поле и скамейка переполнены!\n",
                    "Your board and bench is full!\n"
            );
            return false;
        }
    }

    public boolean sellCreature(Creature creature) {
        boolean sold = false;
        if (player.getBoard().hasCreature(creature)) {
            player.getBoardController().removeCreatureFromBoard(creature);
            sold = true;
        }
        if (player.getBench().hasCreature(creature)) {
            player.getBoardController().removeCreatureFromBench(creature);
            sold = true;
        }
        if (sold) {
            player.getBoardController().getCreatureCounter().clear(creature.getName());
            creature.clearAllChangesFromAllSources();
            for (int i = 0; i < creature.getLevel(); i++) {
                creaturePool.addCreature(CreatureFactory.creatureByName(creature.getName()));
            }
            player.addMoney(creature.getSellingCost());
        }
        return sold;
    }

    public void reduceLevelUpCost(int amount) {
        this.levelUpCost = Math.max(0, levelUpCost - amount);
    }

    protected void levelUp() {
        shop.incrementShopLevel();
        player.incrementShopLevel();
        this.levelUpCost = generateLevelUpCost(player.getShopLevel());
        MessageController.print(player.getName() + " leveled up shop to level " + player.getShopLevel());
    }

    protected int generateLevelUpCost(int level) {
        return 2 + 3 * level + Math.max(0, 3 - level * 2);
    }
}

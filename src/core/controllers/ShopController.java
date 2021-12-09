package core.controllers;

import java.util.List;
import java.util.stream.Collectors;

import core.battlefield.Position;
import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;
import core.controllers.utils.MessageController;
import core.shop.HasShopView;
import core.shop.Shop;
import core.shop.ShopItem;
import core.shop.ShopView;
import utils.Pair;
import utils.Selector;

public class ShopController<T extends HasShopView> {
    private Shop<T> shop;
    private Player player;
    private int refreshCost = 1;
    private int levelUpCost;

    public ShopController(Shop<T> shop, Player player) {
        this.shop = shop;
        this.player = player;
        this.levelUpCost = generateLevelUpCost(player.getCreatureShopLevel());
    }

    public void shopProcessing() {
        List<? extends HasShopView> items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
        int selectedNumber = -1;
        while (selectedNumber != 0) {
            MessageController.print(ShopView.getHeader(player.getCreatureShopLevel(), player.getMoney()));
            Pair<String, Integer> refreshOption = new Pair<>("Обновить", refreshCost);
            Pair<String, Integer> levelUpOption = new Pair<>("Повысить уровень", levelUpCost);
            selectedNumber = Selector.shopSelect(items, refreshOption, levelUpOption);
            if (selectedNumber != 0) {
                if (selectedNumber - 1 == items.size()) {
                    if (player.getMoney() >= refreshCost) {
                        player.reduceMoney(refreshCost);
                        refreshShop();
                        items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
                    } else {
                        MessageController.print("Недостаточно монет!\n");
                    }
                    continue;
                }
                if (selectedNumber - 2 == items.size()) {
                    if (player.getMoney() >= levelUpCost) {
                        player.reduceMoney(levelUpCost);
                        levelUp();
                    } else {
                        MessageController.print("Недостаточно монет!\n");
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

    private boolean tryToBuyItemFromLine(int position) {
        int lineLength = shop.getCurrentLine().items.size();
        if (position >= lineLength) {
            return false;
        } else {
            return tryToBuyItem(shop.getCurrentLine().items.get(position));
        }
    }

    private boolean tryToBuyItem(ShopItem<T> shopItem) {
        int cost = shopItem.getCost();
        if (player.hasMoney(cost)) {
            if (!shopItem.getItem().getName().equals("Продано")) {
                player.reduceMoney(cost);
                boolean isOperationSuccess = buyItem(shopItem);
                if (isOperationSuccess) {
                    MessageController.print(player.getName() + " купил " + shopItem.getItem().getName());
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            MessageController.print("Недостаточно золота для покупки " + shopItem.getItem().getName() + "\n");
        }
        return false;
    }

    private boolean buyItem(ShopItem<T> shopItem) {
        if (shopItem.getItem() instanceof Creature) {
            return buyCreature((ShopItem<Creature>) shopItem);
        }
        return false;
    }

    private boolean buyCreature(ShopItem<Creature> shopItem) {
        if (player.getBoard().getAllCreatures().size() >= player.getCreatureShopLevel()) {
            if (player.getBench().getFreeSpace() <= 0) {
                MessageController.print("Ваше поле переполнено!\n");
                return false;
            } else {
                player.getBench().addCreature(shopItem.getItem());
                CreaturePool.removeCreature(shopItem.getItem());
                int index = shop.currentLine.items.indexOf(shopItem);
                shop.changeItemToDummy(index);
                return true;
            }
        } else {
            player.getBoard().addCreature(shopItem.getItem(), Position.FIRST_LINE);
            CreaturePool.removeCreature(shopItem.getItem());
            int index = shop.currentLine.items.indexOf(shopItem);
            shop.changeItemToDummy(index);
            return true;
        }
    }

    public boolean sellCreature(Creature creature) {
        if (player.getBoard().hasCreature(creature)) {
            return sellCreatureFromBoard(creature);
        }
        if (player.getBench().hasCreature(creature)) {
            return sellCreatureFromBench(creature);
        }
        return false;
    }

    public boolean sellCreatureFromBoard(Creature creature) {
        if (!player.getBoard().hasCreature(creature)) {
            return false;
        }
        player.getBoard().removeCreature(creature);
        creature.clearAllChangesFromAllSources();
        CreaturePool.addCreature(creature);
        player.addMoney(creature.getCost());
        return true;
    }

    public boolean sellCreatureFromBench(Creature creature) {
        if (!player.getBench().removeCreature(creature)) {
            return false;
        }
        creature.clearAllChangesFromAllSources();
        CreaturePool.addCreature(creature);
        player.addMoney(creature.getCost());
        return true;
    }

    private void levelUp() {
        shop.incrementShopLevel();
        player.incrementShopLevel();
        this.levelUpCost = generateLevelUpCost(player.getCreatureShopLevel());
    }

    private int generateLevelUpCost(int level) {
        return 2 + 2 * level;
    }
}

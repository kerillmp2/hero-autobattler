package core.shop;

import java.util.List;
import java.util.stream.Collectors;

import core.battlefield.Position;
import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;
import core.utils.MessageController;
import core.utils.Pair;
import core.utils.Selector;

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
                boolean isOperationSuccess = TryToBuyItemFromLine(selectedNumber - 1);
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

    private boolean TryToBuyItemFromLine(int position) {
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
                buyItem(shopItem);
                MessageController.print(player.getName() + " купил " + shopItem.getItem().getName());
                return true;
            }
        } else {
            MessageController.print("Недостаточно золота для покупки " + shopItem.getItem().getName() + "\n");
        }
        return false;
    }

    private void buyItem(ShopItem<T> shopItem) {
        if (shopItem.getItem() instanceof Creature) {
            buyCreature((ShopItem<Creature>) shopItem);
        }
    }

    private void buyCreature(ShopItem<Creature> shopItem) {
        player.getBoard().addCreature(shopItem.getItem(), Position.FIRST_LINE);
        CreaturePool.removeCreature(shopItem.getItem());
        int index = shop.currentLine.items.indexOf(shopItem);
        shop.changeItemToDummy(index);
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

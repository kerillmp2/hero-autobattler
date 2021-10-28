package core.shop;

import java.util.List;
import java.util.stream.Collectors;

import core.battlefield.Position;
import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;
import core.utils.MessageController;
import core.utils.Selector;

public class ShopController<T extends HasShopView> {
    private Shop<T> shop;
    private Player player;
    private int refreshCost = 1;

    public ShopController(Shop<T> shop, Player player) {
        this.shop = shop;
        this.player = player;
    }

    public void shopProcessing() {
        List<? extends HasShopView> items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
        int selectedNumber = -1;
        while (selectedNumber != 0) {
            MessageController.print("У вас " + player.getMoney() + " монет");
            selectedNumber = Selector.shopSelect(items, true, refreshCost);
            if (selectedNumber != 0) {
                if (items.size() == selectedNumber - 1) {
                    player.reduceMoney(refreshCost);
                    refreshShop();
                    items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
                } else {
                    boolean isOperationSuccess = TryToBuyItemFromLine(selectedNumber - 1);
                    if (isOperationSuccess) {
                        items = shop.getCurrentLine().items.stream().map(ShopItem::getItem).collect(Collectors.toList());
                    }
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
            player.reduceMoney(cost);
            buyItem(shopItem);
            MessageController.print(player.getName() + " купил " + shopItem.getItem().getName());
            return true;
        } else {
            MessageController.print("Недостаточно золота для покупки " + shopItem.getItem().getName() + "\n");
            return false;
        }
    }

    private void buyItem(ShopItem<T> shopItem) {
        if (shopItem.getItem() instanceof Creature) {
            Creature creature = (Creature) shopItem.getItem();
            buyCreature(creature);
            CreaturePool.removeCreature(creature);
            shop.currentLine.items.remove(shopItem);
        }
    }

    private void buyCreature(Creature creature) {
        player.getBoard().addCreature(creature, Position.FIRST_LINE);
    }
}

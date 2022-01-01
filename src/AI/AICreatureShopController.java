package AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import core.controllers.ShopController;
import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;
import core.shop.Shop;
import core.shop.ShopItem;

public class AICreatureShopController extends ShopController<Creature> {

    public AICreatureShopController(Shop<Creature> shop, Player player, CreaturePool creaturePool) {
        super(shop, player, creaturePool);
    }

    @Override
    public void shopProcessing() {
        int money = player.getMoney();
        while (money >= levelUpCost) {
            player.reduceMoney(levelUpCost);
            money = player.getMoney();
            regenerateShop();
            levelUp();
        }
        boolean endShopping = false;
        while (!endShopping) {
            if (shop.lineIsEmpty()) {
                if (money > refreshCost) {
                    player.reduceMoney(refreshCost);
                    money = player.getMoney();
                    regenerateShop();
                    refreshShop();
                } else {
                    if(sellProcessing()) {
                        continue;
                    } else {
                        endShopping = true;
                    }
                }
            } else {
                boolean isOperationSuccess = findAndBuyBestCreature(shop.getCurrentLine().items);
                if (isOperationSuccess) {
                    continue;
                } else {
                    endShopping = true;
                }
            }
        }
    }

    public boolean findAndBuyBestCreature(List<ShopItem<Creature>> itemsInShop) {
        for (ShopItem<Creature> item : itemsInShop) {
            Creature creature = item.getItem();
            int currentLevel = player.getBoardController().getCreatureCounter().count(creature);
            if (currentLevel == 2 || currentLevel == 5 || currentLevel == 8) {
                return tryToBuyItem(item);
            }
        }
        for (int lvl = 7; lvl >= 0; lvl--) {
            for (ShopItem<Creature> item : itemsInShop) {
                Creature creature = item.getItem();
                int currentLevel = player.getBoardController().getCreatureCounter().count(creature);
                if (currentLevel == lvl) {
                    return tryToBuyItem(item);
                }
            }
        }
        return true;
    }

    public Creature chooseCreatureToSell() {
        List<Creature> allCreatures = new ArrayList<>();
        allCreatures.addAll(this.player.getBench().getCreatures());
        allCreatures.addAll(this.player.getBoard().getCreatures());
        int totalCreatures = allCreatures.size();
        if (totalCreatures <= player.getShopLevel()) {
            return null;
        }
        allCreatures.sort(Comparator.comparingInt(Creature::getSellingCost));
        for (Creature creature : allCreatures) {
            if (creature.getLevel() >= 6) {
                continue;
            }
            if (creature.getCost() >= 4) {
                continue;
            }
            return creature;
        }
        return null;
    }

    public boolean sellProcessing() {
        Creature creatureForSelling = chooseCreatureToSell();
        if (creatureForSelling == null) {
            return false;
        }
        this.sellCreature(creatureForSelling);
        return true;
    }
}

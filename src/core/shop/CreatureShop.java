package core.shop;

import java.util.List;
import java.util.stream.Collectors;

import core.creature.Creature;
import core.creature.CreaturePool;
import core.player.Player;

public class CreatureShop extends Shop<Creature> {
    private CreatureShop(List<ShopItem<Creature>> itemPool, int shopLineLength) {
        super(itemPool, shopLineLength);
    }

    public static CreatureShop defaultCreatureShop(Player player) {
        return new CreatureShop(
                CreaturePool.toShopItems(),
                5
        );
    }

    @Override
    public void regenerate() {
        this.itemPool = CreaturePool.toShopItems();
    }
}

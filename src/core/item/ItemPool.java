package core.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPool {
    Map<Rarity, List<Item>> pool;

    public ItemPool(Map<Rarity, List<Item>> pool) {
        this.pool = pool;
    }

    public static ItemPool empty() {
        Map<Rarity, List<Item>> pool = new HashMap<>();
        for (Rarity rarity : Rarity.values()) {
            if (rarity != Rarity.UNDEFINED) {
                pool.put(rarity, new ArrayList<>());
            }
        }
        return new ItemPool(pool);
    }

    public void add(Item item) {
        pool.get(item.rarity).add(item);
    }

    public void remove(Item item) {
        pool.get(item.rarity).remove(item);
    }

    public Item pullItemForRarity(Rarity rarity) {
        List<Item> items = pool.get(rarity);
        Collections.shuffle(items);
        Item item = items.size() > 0 ? items.get(0) : null;
        if (item != null) {
            items.remove(item);
        }
        return item;
    }
}

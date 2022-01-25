package core.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.controllers.utils.RandomController;
import core.creature.Creature;
import core.item.Item;
import core.item.ItemPool;
import core.item.Rarity;
import core.traits.Trait;

import static core.item.ItemFactory.*;

public class ItemController {

    private static Map<Rarity, Integer> chances = new HashMap<>();

    public static List<Item> getItemsFor(Creature creature, int amount) {
        ItemPool itemPool = ItemPool.empty();
        List<Item> items = new ArrayList<>();
        int level = creature.getLevel();
        chances = getChances(level);
        addBasicItems(itemPool);
        for (Trait trait : creature.getTraitContainer().getTags()) {
            addItemsForTrait(itemPool, trait);
        }
        for (int i = 0; i < amount; i++) {
            Rarity rarity = generateRarity();
            Item item = itemPool.pullItemForRarity(rarity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public static void addBasicItems(ItemPool pool) {
        pool.add(snowball());
        pool.add(leatherCloak());
        pool.add(leatherBoots());
        pool.add(ironKnife());
        pool.add(ironSword());
        pool.add(ironShield());
        pool.add(ironArmor());
        pool.add(ironLance());
        pool.add(ironStaff());
        pool.add(oldBook());
        pool.add(rubyAmulet());
        pool.add(topazAmulet());
        pool.add(sapphireAmulet());
        pool.add(emeraldAmulet());

        pool.add(backpack());

        pool.add(dragonRay());
        pool.add(bottomlessBag());
    }

    public static void addItemsForTrait(ItemPool pool, Trait trait) {
        switch (trait) {

            //CLASS
            case WARRIOR: {
                break;
            }
            case KNIGHT: {
                break;
            }
            case ASSASSIN: {
                pool.add(throwingDagger());
                break;
            }

            case MAGE: {
                break;
            }
            case EATER: {
                break;
            }
            case ALCHEMIST: {
                pool.add(potionKit());
                break;
            }

            //TYPE
            case DEFENDER: {
                pool.add(ironHeart());
                break;
            }
            case POISONOUS: {
                pool.add(poisonBlade());
                break;
            }
            case DEMON: {
                break;
            }
            case ROBOT: {
                pool.add(repairKit());
                pool.add(rageModule());
                pool.add(mindModule());
                pool.add(heatModule());
                break;
            }
            case FROSTBORN: {
                break;
            }
            case FIREBORN: {
                pool.add(fireShard());
                break;
            }
            case STUDENT: {
                break;
            }
        }
    }

    private static Rarity generateRarity() {
        int randomInt = RandomController.randomInt(1, 100, true);
        int counter = chances.get(Rarity.COMMON);
        if (randomInt <= counter) {
            return Rarity.COMMON;
        }
        counter += chances.get(Rarity.UNCOMMON);
        if (randomInt <= counter) {
            return Rarity.UNCOMMON;
        }
        counter += chances.get(Rarity.RARE);
        if (randomInt <= counter) {
            return Rarity.RARE;
        }
        counter += chances.get(Rarity.EPIC);
        if (randomInt <= counter) {
            return Rarity.EPIC;
        }
        counter += chances.get(Rarity.LEGENDARY);
        if (randomInt <= counter) {
            return Rarity.LEGENDARY;
        }
        return Rarity.UNDEFINED;
    }

    private static Map<Rarity, Integer> getChances(int level) {
        Map<Rarity, Integer> chances = new TreeMap<>();
        for (Rarity rarity : Rarity.values()) {
            if (rarity != Rarity.UNDEFINED) {
                chances.put(rarity, 0);
            }
        }

        switch (level) {
            case 3:
                chances.put(Rarity.COMMON, 60);
                chances.put(Rarity.UNCOMMON, 40);
                break;
            case 6:
                chances.put(Rarity.UNCOMMON, 10);
                chances.put(Rarity.RARE, 70);
                chances.put(Rarity.EPIC, 20);
                break;
            case 9:
                chances.put(Rarity.RARE, 15);
                chances.put(Rarity.EPIC, 60);
                chances.put(Rarity.LEGENDARY, 25);
                break;
            default:
                chances.put(Rarity.COMMON, 100);
        }

        return chances;
    }
}

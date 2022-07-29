package core.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import core.controllers.utils.MessageController;
import core.controllers.utils.RandomController;
import core.creature.Creature;
import core.item.Item;
import core.item.ItemPool;
import core.item.Rarity;
import core.traits.Trait;
import core.viewers.ItemChoiceViewer;
import utils.Selector;

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

    public static List<Item> getPirateItems(Rarity rarity, int amount) {
        ItemPool itemPool = ItemPool.empty();
        addPirateItems(itemPool);
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Item item = itemPool.pullItemForRarity(rarity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    private static void addPirateItems(ItemPool pool) {
        pool.add(seaweed());
        pool.add(turtleShell());
        pool.add(emptyBottle());
        pool.add(bluntKnife());
        pool.add(saltyWater());
        pool.add(rustyCoin());
        pool.add(rubyAmulet());
        pool.add(rubyAmulet());
        pool.add(backpack());
        pool.add(backpack());
        pool.add(rageModule());
        pool.add(rageModule());
        pool.add(dragonRay());
        pool.add(dragonRay());
    }

    private static void addBasicItems(ItemPool pool) {
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
                pool.add(throwingDaggers());
                break;
            }

            case MAGE: {
                break;
            }

            case EATER: {
                pool.add(chickenLeg());
                pool.add(bronzeFork());
                pool.add(silverFork());
                pool.add(mythrilFork());
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
                pool.add(demonClaw());
                break;
            }
            case ROBOT: {
                pool.add(repairModule());
                pool.add(rageModule());
                pool.add(mindModule());
                pool.add(heatModule());
                break;
            }
            case FROSTBORN: {
                pool.add(frostShard());
                pool.add(iceShield());
                break;
            }
            case FIREBORN: {
                pool.add(fireShard());
                break;
            }
            case STUDENT: {
                break;
            }
            case ARCHER: {
                pool.add(poisonArrows());
                pool.add(fireArrows());
                break;
            }
            case CULTIST: {
                break;
            }
            case UNDEAD: {
                break;
            }
            case BEAST: {
                break;
            }
            case PIRATE: {
                break;
            }
            case SPIRIT: {
                pool.add(lostSoul());
                pool.add(redSoul());
                pool.add(greenSoul());
                pool.add(yellowSoul());
                pool.add(blueSoul());
                break;
            }
            case DUELIST: {
                break;
            }
            case SUPPORT: {
                break;
            }
            case SUMMONER: {
                break;
            }
        }
    }

    public static Item selectItemFromList(List<Item> items, String header) {
        int selectedNumber = -1;
        while (selectedNumber == -1) {
            MessageController.print(ItemChoiceViewer.getItemChoiceView(items, header));
            selectedNumber = Selector.select(items, 1);
        }
        return items.get(selectedNumber - 1);
    }

    public static void selectItem(Creature creature) {
        int selectedNumber = -1;
        List<Item> items = ItemController.getItemsFor(creature, 5);
        while (selectedNumber == -1) {
            MessageController.print(ItemChoiceViewer.getItemChoiceView(items, creature));
            selectedNumber = Selector.select(items, 1);
        }
        Item selectedItem = items.get(selectedNumber - 1);
        selectedItem.equipOn(creature);
    }

    public static void selectItemForAI(Creature creature) {
        List<Item> items = ItemController.getItemsFor(creature, 5).stream().filter(Item::inAIPool).sorted(Comparator.comparingInt(Item::getValue)).collect(Collectors.toList());
        if (items.size() > 0) {
            Item selectedItem = items.get(items.size() - 1);
            selectedItem.equipOn(creature);
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

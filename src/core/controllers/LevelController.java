package core.controllers;

import java.util.Comparator;
import java.util.List;

import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureFactory;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.item.Item;
import core.viewers.ItemChoiceViewer;
import utils.Selector;

public class LevelController {

    public static void levelUpCreature(Creature creature, int level, boolean forAI) {
        if (level == 3) {
            Creature initCreature = CreatureFactory.creatureByName(creature.getName());
            for (Stat stat : Stat.values()) {
                if (stat != Stat.MANA && stat != Stat.SPEED) {
                    int amount = initCreature.getStat(stat);
                    creature.applyBuff(stat, StatChangeSource.PERMANENT, amount / 3);
                }
            }
            if (forAI) {
                selectItemForAI(creature);
            } else {
                selectItem(creature);
            }
        }
        if (level == 6) {
            Creature initCreature = CreatureFactory.creatureByName(creature.getName());
            for (Stat stat : Stat.values()) {
                if (stat != Stat.MANA && stat != Stat.SPEED) {
                    int amount = initCreature.getStat(stat);
                    creature.applyBuff(stat, StatChangeSource.PERMANENT, amount / 2);
                }
            }
            if (forAI) {
                selectItemForAI(creature);
            } else {
                selectItem(creature);
            }
        }
        if (level == 9) {
            Creature initCreature = CreatureFactory.creatureByName(creature.getName());
            for (Stat stat : Stat.values()) {
                if (stat != Stat.MANA && stat != Stat.SPEED) {
                    int amount = initCreature.getStat(stat);
                    creature.applyBuff(stat, StatChangeSource.PERMANENT, (amount));
                }
            }
            if (forAI) {
                selectItemForAI(creature);
            } else {
                selectItem(creature);
            }
        }
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
        List<Item> items = ItemController.getItemsFor(creature, 5);
        items.sort(Comparator.comparingInt(Item::getValue));
        if (items.size() > 0) {
            Item selectedItem = items.get(items.size() - 1);
            selectedItem.equipOn(creature);
        }
    }
}

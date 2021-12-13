package core.controllers;

import java.util.ArrayList;
import java.util.List;

import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureFactory;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.item.Item;
import core.item.ItemFactory;
import core.viewers.ItemChoiceViewer;
import utils.Selector;

public class LevelController {

    public static void levelUpCreature(Creature creature, int level) {
        if (level == 3) {
            Creature initCreature = CreatureFactory.creatureByName(creature.getName());
            for (Stat stat : Stat.values()) {
                if (stat != Stat.MANA && stat != Stat.SPEED) {
                    int amount = initCreature.getStat(stat);
                    creature.applyBuff(stat, StatChangeSource.PERMANENT, amount / 2);
                }
            }
            int selectedNumber = -1;
            List<Item> items = ItemController.getItemsFor(creature, level, 5);
            while (selectedNumber == -1) {
                MessageController.print(ItemChoiceViewer.getItemChoiceView(items, creature));
                selectedNumber = Selector.select(items);
            }
            Item selectedItem = items.get(selectedNumber - 1);
            selectedItem.equipOn(creature);
        }
        if (level == 6) {
            Creature initCreature = CreatureFactory.creatureByName(creature.getName());
            for (Stat stat : Stat.values()) {
                if (stat != Stat.MANA && stat != Stat.SPEED) {
                    int amount = initCreature.getStat(stat);
                    creature.applyBuff(stat, StatChangeSource.PERMANENT, amount);
                }
            }
        }
        if (level == 9) {
            Creature initCreature = CreatureFactory.creatureByName(creature.getName());
            for (Stat stat : Stat.values()) {
                if (stat != Stat.MANA && stat != Stat.SPEED) {
                    int amount = initCreature.getStat(stat);
                    creature.applyBuff(stat, StatChangeSource.PERMANENT, (int) (amount * 1.5));
                }
            }
        }
    }
}

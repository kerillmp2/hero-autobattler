package core.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.creature.Creature;
import core.item.Item;
import core.traits.Trait;

import static core.item.ItemFactory.*;

public class ItemController {

    public static List<Item> getItemsFor(Creature creature, int amount) {
        List<Item> pool = new ArrayList<>(getItemsForLevel(creature.getLevel()));
        for (Trait trait : creature.getTraitContainer().getTags()) {
            pool.addAll(getItemsForTrait(trait, creature.getLevel()));
        }
        Collections.shuffle(pool);
        amount = Math.min(pool.size(), amount);
        return pool.subList(0, amount);
    }

    public static List<Item> getItemsForLevel(int level) {
        List<Item> pool = new ArrayList<>();
        if (level == 3) {
            pool.add(snowball());
            pool.add(leatherCloak());
            pool.add(leatherBoots());
            pool.add(ironKnife());
            pool.add(ironSword());
            pool.add(ironShield());
            pool.add(ironArmor());
            pool.add(ironLance());
            pool.add(oldBook());
            pool.add(ironStaff());
            pool.add(rubyAmulet());
            pool.add(topazAmulet());
            pool.add(sapphireAmulet());
            pool.add(emeraldAmulet());
        }
        if (level == 6) {
            pool.add(backpack());
        }
        if (level == 9) {
            pool.add(dragonRay());
        }
        return pool;
    }

    public static List<Item> getItemsForTrait(Trait trait, int level) {
        List<Item> pool = new ArrayList<>();
        switch (trait) {

            //CLASS
            case WARRIOR: {
                if (level == 3) {
                }
                break;
            }
            case KNIGHT: {
                if (level == 3) {
                }
                break;
            }
            case ASSASSIN: {
                if (level == 3) {
                }
                if (level == 6) {
                    pool.add(throwingDagger());
                }
                break;
            }

            case MAGE: {
                if (level == 3) {
                }
                break;
            }
            case EATER: {
                if (level == 3) {
                }
                break;
            }
            case ALCHEMIST: {
                if (level == 3) {
                }
                break;
            }

            //TYPE
            case DEFENDER: {
                if (level == 3) {
                }
                if (level == 6) {
                    pool.add(ironHeart());
                }
                break;
            }
            case POISONOUS: {
                if (level == 6) {
                    pool.add(poisonBlade());
                }
                break;
            }
            case DEMON: {

            }
            case ROBOT: {
                if (level == 3) {
                    pool.add(repairKit());
                }
                if (level == 6) {
                    pool.add(rageModule());
                    pool.add(mindModule());
                    pool.add(heatModule());
                }
            }
            case FROSTBORN: {

            }
            case STUDENT: {

            }
        }
        return pool;
    }
}

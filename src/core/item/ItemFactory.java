package core.item;

import java.util.ArrayList;
import java.util.List;

import core.controllers.utils.RandomController;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.creature.stat.StatsContainer;

import static core.controllers.utils.RandomController.*;
import static core.creature.stat.Stat.*;

public class ItemFactory {

    //All creatures
    public static Item ironKnife() {
        return ItemTemplate.empty().withName("Iron Knife").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, randomInt(5, 10, true), true)
                .withStatChange(SPEED, randomInt(5, 10, true), false)
                .build();
    }

    public static Item ironSword() {
        return ItemTemplate.empty().withName("Iron sword").withRarity(Rarity.COMMON)
                .withStatChange(ATTACK, randomInt(1, 3, true), false)
                .withStatChange(ATTACK, randomInt(10, 15, true), true)
                .build();
    }

    public static Item ironStaff() {
        int manaGain = RandomController.randomInt(1, 2, true);
        return ItemTemplate.empty().withName("Iron staff")
                .withStatChange(SPELL_POWER, randomInt(2, 4, true), false)
                .withStatChange(SPELL_POWER, randomInt(18, 25, true), true)
                .addDescription("$ +%d Mana on attack").addValue(manaGain)
                .buildWithAction(c -> {
                    c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ATTACK, StatChangeSource.PERMANENT, manaGain);
                });
    }

    public static Item oldBook() {
        int manaGain = RandomController.randomInt(2, 5, true);
        return ItemTemplate.empty().withName("Old book").withRarity(Rarity.COMMON)
                .withStatChange(SPELL_POWER, randomInt(2, 3, true), false)
                .withStatChange(SPELL_POWER, randomInt(17, 22, true), true)
                .addDescription("$ +%d Mana on attack").addValue(manaGain)
                .buildWithAction(c -> {
                    c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ATTACK, StatChangeSource.PERMANENT, manaGain);
                });
    }

    public static Item ironArmor() {
        return ItemTemplate.empty().withName("Iron armor").withRarity(Rarity.COMMON)
                .withStatChange(PHYSICAL_ARMOR, randomInt(1, 3, true), false)
                .withStatChange(HP, randomInt(9, 13, true), true)
                .build();
    }

    public static Item ironShield() {
        return ItemTemplate.empty().withName("Iron shield").withRarity(Rarity.COMMON)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(2, 3, true), false)
                .withStatChange(Stat.MAGIC_ARMOR, RandomController.randomInt(1, 2, true), false)
                .withStatChange(Stat.HP, RandomController.randomInt(12, 18, true), false)
                .build();
    }

    public static Item leatherBoots() {
        return ItemTemplate.empty().withName("Leather Boots").withRarity(Rarity.COMMON)
                .withStatChange(Stat.SPEED, RandomController.randomInt(7, 12, true), true)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(1, 2, true), false)
                .withStatChange(Stat.MAGIC_ARMOR, RandomController.randomInt(1, 2, true), false)
                .build();
    }

    public static Item leatherCloak() {
        return ItemTemplate.empty().withName("Leather cloak").withRarity(Rarity.COMMON)
                .withStatChange(Stat.MAGIC_ARMOR, RandomController.randomInt(2, 4, true), false)
                .withStatChange(Stat.HP, RandomController.randomInt(8, 13, true), true)
                .build();
    }

    public static Item ironLance() {
        return ItemTemplate.empty().withName("Iron lance").withRarity(Rarity.Rare)
                .withStatChange(Stat.ATTACK, RandomController.randomInt(2, 3, true), false)
                .withStatChange(Stat.ATTACK, RandomController.randomInt(8, 15, true), true)
                .withStatChange(Stat.SPEED, RandomController.randomInt(5, 10, true), false)
                .build();
    }

    public static Item emeraldAmulet() {
        return ItemTemplate.empty().withName("Emerald amulet").withRarity(Rarity.Rare)
                .withStatChange(Stat.HP, RandomController.randomInt(10, 14, true), true)
                .withStatChange(Stat.HP, RandomController.randomInt(11, 15, true), false)
                .withStatChange(Stat.getRandomStatFrom(Stat.PHYSICAL_ARMOR, Stat.MAGIC_ARMOR, Stat.ATTACK, Stat.SPEED), RandomController.randomInt(1, 2), false)
                .build();
    }

    public static Item topazAmulet() {
        return ItemTemplate.empty().withName("Topaz amulet").withRarity(Rarity.Rare)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(2, 4, true), false)
                .withStatChange(Stat.MAGIC_ARMOR, RandomController.randomInt(2, 4, true), false)
                .withStatChange(Stat.getRandomStatFrom(Stat.PHYSICAL_ARMOR, Stat.MAGIC_ARMOR, Stat.ATTACK, Stat.SPEED), RandomController.randomInt(1, 2), false)
                .build();
    }

    public static Item sapphireAmulet() {
        int manaGain = RandomController.randomInt(2, 4, true);
        return ItemTemplate.empty().withName("Sapphire amulet").withRarity(Rarity.Rare)
                .withStatChange(Stat.MANA, (-1) * RandomController.randomInt(10, 15, true), true)
                .withStatChange(Stat.getRandomStatFrom(Stat.PHYSICAL_ARMOR, Stat.MAGIC_ARMOR, Stat.ATTACK, Stat.SPEED), RandomController.randomInt(1, 2), false)
                .addDescription("$ +%d Mana on attack").addValue(manaGain)
                .buildWithAction(c -> {
                    c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ATTACK, StatChangeSource.PERMANENT, manaGain);
                });
    }

    public static Item rubyAmulet() {
        return ItemTemplate.empty().withName("Ruby amulet").withRarity(Rarity.Rare)
                .withStatChange(Stat.ATTACK, RandomController.randomInt(2, 4, true), false)
                .withStatChange(Stat.ATTACK, RandomController.randomInt(10, 20, true), true)
                .withStatChange(Stat.HP, RandomController.randomInt(10, 15, true), false)
                .withStatChange(Stat.getRandomStatFrom(Stat.PHYSICAL_ARMOR, Stat.MAGIC_ARMOR, Stat.ATTACK, Stat.SPEED), RandomController.randomInt(1, 2), false)
                .build();
    }

    public static Item ironHeart() {
        int armorGain = randomInt(1, 2, true);
        return ItemTemplate.empty().withName("Iron heart").withRarity(Rarity.EPIC)
                .withStatChange(Stat.PHYSICAL_ARMOR, RandomController.randomInt(1, 4, true), false)
                .withStatChange(Stat.MAGIC_ARMOR, RandomController.randomInt(1, 4, true), false)
                .addDescription("$ Gain %d physical armor after taking physical damage").addValue(armorGain)
                .buildWithAction(c -> {
                    c.applyCreatureTagChange(CreatureTag.ADD_ARMOR_AFTER_TAKING_PHYSICAL_DAMAGE , StatChangeSource.PERMANENT, armorGain);
                });

    }

    public static Item dragonRay() {
        int damage = RandomController.randomInt(30, 50, true);
        return ItemTemplate.empty().withName("Dragon ray").withRarity(Rarity.LEGENDARY)
                .addDescription("Deals %d magic damage to each enemy after attack").addValue(damage)
                .buildWithAction(c -> {
                   c.applyCreatureTagChange(CreatureTag.DAMAGE_ALL_ENEMIES_AFTER_ATTACK, StatChangeSource.PERMANENT, damage);
                });
    }


    private static class ItemTemplate {
        private String name;
        private Rarity rarity;
        private String descriptionTemplate;
        private List<Integer> values;
        private List<StatsContainer.StatChange> statChanges;

        private ItemTemplate(String name, Rarity rarity, String descriptionTemplate, List<Integer> values, List<StatsContainer.StatChange> statChanges) {
            this.name = name;
            this.rarity = rarity;
            this.descriptionTemplate = descriptionTemplate;
            this.values = values;
            this.statChanges = statChanges;
        }

        public static ItemTemplate empty() {
            return new ItemTemplate("", Rarity.UNDEFINED, "", new ArrayList<>(), new ArrayList<>());
        }

        public ItemTemplate withName(String name) {
            this.name = name;
            return this;
        }

        public ItemTemplate withDescription(String description) {
            this.descriptionTemplate = description;
            return this;
        }

        public ItemTemplate addValue(int value) {
            this.values.add(value);
            return this;
        }

        public ItemTemplate withRarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public ItemTemplate withStatChange(Stat stat, int amount, boolean isPercentage) {
            statChanges.add(new StatsContainer.StatChange(stat, StatChangeSource.PERMANENT, amount, isPercentage));
            String symbol = amount > 0 ? "+" : "";
            String percent = isPercentage ? "%%" : "";
            if (statChanges.size() > 1) {
                descriptionTemplate += "$ ";
            }
            descriptionTemplate += symbol + "%d" + percent + " " + stat.getName();
            values.add(amount);
            return this;
        }

        public ItemTemplate addDescription(String additionalDescription) {
            descriptionTemplate += additionalDescription;
            return this;
        }

        public Item build() {
            return new Item(this.name, String.format(this.descriptionTemplate, values.toArray()), rarity,
                    creature -> {
                        for (StatsContainer.StatChange statChange : statChanges) {
                            creature.apply(statChange);
                        }
                    });
        }

        public Item buildWithAction(EquipAction equipAction) {
            return new Item(this.name, String.format(this.descriptionTemplate, values.toArray()), rarity,
                    creature -> {
                        equipAction.onEquip(creature);
                        for (StatsContainer.StatChange statChange : statChanges) {
                            creature.apply(statChange);
                        }
                    });
        }
    }
}

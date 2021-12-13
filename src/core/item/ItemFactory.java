package core.item;

import java.util.ArrayList;
import java.util.List;

import core.controllers.utils.RandomController;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.traits.Trait;

public class ItemFactory {

    public static List<Item> getItemsForLevel(int level) {
        List<Item> pool = new ArrayList<>();
        if (level == 3) {
            pool.add(ironKnife());
            pool.add(ironSword());
            pool.add(ironStaff());
            pool.add(cloak());
            pool.add(ironArmor());
            pool.add(oldBook());
        }
        return pool;
    }

    public static List<Item> getItemsForTrait(Trait trait, int level) {
        List<Item> pool = new ArrayList<>();
        switch (trait) {
            case KNIGHT: {
                if (level == 3) {
                    pool.add(ironLance());
                    pool.add(ironShield());
                    pool.add(ironArmor());
                }
            }
            case WARRIOR: {
                if (level == 3) {
                    pool.add(ironSword());
                    pool.add(ironShield());
                    pool.add(ironArmor());
                }
            }
        }
        return pool;
    }

    public static Item ironKnife() {
        int speedBuff = RandomController.randomInt(5, 10, true);
        int attackBuff = RandomController.randomInt(5, 10, true);
        return ItemTemplate.empty().withName("Iron Knife").withDescription("+%d Speed$ +%d%% Attack").addValue(speedBuff).addValue(attackBuff).withRarity(Rarity.COMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.ATTACK, StatChangeSource.PERMANENT, attackBuff, true);
                    c.applyBuff(Stat.SPEED, StatChangeSource.PERMANENT, speedBuff, false);
                });
    }

    public static Item ironSword() {
        int floatAttackBuff = RandomController.randomInt(1, 3, true);
        int percentageAttackBuff = RandomController.randomInt(10, 15, true);
        return ItemTemplate.empty().withName("Iron sword").withDescription("+%d Attack$ +%d%% Attack")
                .addValue(floatAttackBuff).addValue(percentageAttackBuff).withRarity(Rarity.COMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.ATTACK, StatChangeSource.PERMANENT, floatAttackBuff, false);
                    c.applyBuff(Stat.ATTACK, StatChangeSource.PERMANENT, percentageAttackBuff, true);
                });
    }

    public static Item ironStaff() {
        int floatSPBuff = RandomController.randomInt(2, 4, true);
        int percentageSPBuff = RandomController.randomInt(18, 25, true);
        int manaGain = RandomController.randomInt(1, 2, true);
        return ItemTemplate.empty().withName("Iron staff").withDescription("+%d Spell power$ +%d%% Spell power$ +%d Mana on attack")
                .addValue(floatSPBuff).addValue(percentageSPBuff).addValue(manaGain).withRarity(Rarity.COMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.SPELL_POWER, StatChangeSource.PERMANENT, floatSPBuff, false);
                    c.applyBuff(Stat.SPELL_POWER, StatChangeSource.PERMANENT, percentageSPBuff, true);
                    c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ATTACK, StatChangeSource.PERMANENT, manaGain);
                });
    }

    public static Item oldBook() {
        int floatSPBuff = RandomController.randomInt(2, 3, true);
        int percentageSPBuff = RandomController.randomInt(17, 22, true);
        int manaGain = RandomController.randomInt(2, 5, true);
        return ItemTemplate.empty().withName("Old book").withDescription("+%d Spell power$ +%d%% Spell power$ +%d Mana on attack")
                .addValue(floatSPBuff).addValue(percentageSPBuff).addValue(manaGain).withRarity(Rarity.COMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.SPELL_POWER, StatChangeSource.PERMANENT, floatSPBuff, false);
                    c.applyBuff(Stat.SPELL_POWER, StatChangeSource.PERMANENT, percentageSPBuff, true);
                    c.applyCreatureTagChange(CreatureTag.ADD_MANA_AFTER_ATTACK, StatChangeSource.PERMANENT, manaGain);
                });
    }

    public static Item ironArmor() {
        int floatArmor = RandomController.randomInt(1, 3, true);
        int percentageHPBuff = RandomController.randomInt(10, 15, true);
        return ItemTemplate.empty().withName("Iron armor").withDescription("+%d Physical Armor$ +%d%% HP")
                .addValue(floatArmor).addValue(percentageHPBuff).withRarity(Rarity.COMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.PERMANENT, floatArmor, false);
                    c.applyBuff(Stat.HP, StatChangeSource.PERMANENT, percentageHPBuff, true);
                });
    }

    public static Item cloak() {
        int magicArmor = RandomController.randomInt(2, 4, true);
        int percentageHPBuff = RandomController.randomInt(8, 13, true);
        return ItemTemplate.empty().withName("Cloak").withDescription("+%d Magic Armor$ +%d%% HP")
                .addValue(magicArmor).addValue(percentageHPBuff).withRarity(Rarity.COMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.PERMANENT, magicArmor, false);
                    c.applyBuff(Stat.HP, StatChangeSource.PERMANENT, percentageHPBuff, true);
                });
    }

    public static Item ironLance() {
        int attack = RandomController.randomInt(2, 3, true);
        int percentageAttack = RandomController.randomInt(11, 16, true);
        int speed = RandomController.randomInt(5, 10, true);
        return ItemTemplate.empty().withName("Iron lance").withDescription("+%d Attack$ +%d%% Attack$ +%d Speed")
                .addValue(attack).addValue(percentageAttack).addValue(speed).withRarity(Rarity.UNCOMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.ATTACK, StatChangeSource.PERMANENT, attack, false);
                    c.applyBuff(Stat.ATTACK, StatChangeSource.PERMANENT, percentageAttack, true);
                    c.applyBuff(Stat.ATTACK, StatChangeSource.PERMANENT, speed, false);
                });
    }

    public static Item ironShield() {
        int physArmor = RandomController.randomInt(2, 3, true);
        int magArmor = RandomController.randomInt(1, 2, true);
        int hp = RandomController.randomInt(20, 25, true);
        return ItemTemplate.empty().withName("Iron shield").withDescription("+%d Physical Armor$ +%d Magic Armor$ +%d HP")
                .addValue(physArmor).addValue(magArmor).addValue(hp).withRarity(Rarity.UNCOMMON)
                .buildWithAction(c -> {
                    c.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.PERMANENT, physArmor, false);
                    c.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.PERMANENT, magArmor, false);
                    c.applyBuff(Stat.HP, StatChangeSource.PERMANENT, hp, false);
                });
    }

    private static class ItemTemplate {
        private String name;
        private Rarity rarity;
        private String descriptionTemplate;
        private List<Integer> values;

        private ItemTemplate(String name, Rarity rarity, String descriptionTemplate, List<Integer> values) {
            this.name = name;
            this.rarity = rarity;
            this.descriptionTemplate = descriptionTemplate;
            this.values = values;
        }

        public static ItemTemplate empty() {
            return new ItemTemplate("", Rarity.UNDEFINED, "", new ArrayList<>());
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

        public ItemTemplate withValues(List<Integer> values) {
            this.values = values;
            return this;
        }

        public ItemTemplate withRarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Item buildWithAction(EquipAction equipAction) {
            return new Item(this.name, String.format(this.descriptionTemplate, values.toArray()), rarity, equipAction);
        }
    }
}

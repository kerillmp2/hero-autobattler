package core.creature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.battle.HasBattleView;
import core.shop.HasShopView;
import core.traits.Trait;
import core.traits.TraitContainer;
import core.utils.Constants;
import core.utils.TagContainer;

public class Creature extends TagContainer<CreatureTag> implements HasShopView, HasBattleView {
    private String name;
    private int cost;
    private TraitContainer traitContainer = new TraitContainer();
    private StatsContainer statsContainer = new StatsContainer();

    public Creature(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int cost, Map<CreatureTag, Integer> tagValues) {
        super(tagValues);
        this.name = name;
        statsContainer.addTagValue(Stat.HP, hp);
        statsContainer.addTagValue(Stat.ATTACK, attack);
        statsContainer.addTagValue(Stat.PHYSICAL_ARMOR, physicalArmor);
        statsContainer.addTagValue(Stat.MAGIC_ARMOR, magicArmor);
        statsContainer.addTagValue(Stat.SPELL_POWER, spellPower);
        statsContainer.addTagValue(Stat.SPEED, speed);
        this.cost = cost;
    }

    public Creature(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int cost, CreatureTag... tags) {
        super(Arrays.asList(tags));
        this.name = name;
        statsContainer.addTagValue(Stat.HP, hp);
        statsContainer.addTagValue(Stat.ATTACK, attack);
        statsContainer.addTagValue(Stat.PHYSICAL_ARMOR, physicalArmor);
        statsContainer.addTagValue(Stat.MAGIC_ARMOR, magicArmor);
        statsContainer.addTagValue(Stat.SPELL_POWER, spellPower);
        statsContainer.addTagValue(Stat.SPEED, speed);
        this.cost = cost;
    }

    public static Creature shopDummy() {
        return new Creature("Продано", 0, 0, 0, 0, 0,0, 0);
    }

    public static Creature withStats(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int cost) {
        return new Creature(name, hp, attack, physicalArmor, magicArmor, spellPower, speed, cost, CreatureTag.HAVE_BASIC_ATTACK);
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return statsContainer.getTagValue(Stat.HP);
    }

    public int getAttack() {
        return statsContainer.getTagValue(Stat.ATTACK);
    }

    public int getPhysicalArmor() {
        return statsContainer.getTagValue(Stat.PHYSICAL_ARMOR);
    }

    public int getMagicArmor() {
        return statsContainer.getTagValue(Stat.MAGIC_ARMOR);
    }

    public int getSpellPower() {
        return statsContainer.getTagValue(Stat.SPELL_POWER);
    }

    public int getSpeed() {
        return statsContainer.getTagValue(Stat.SPEED);
    }

    public Creature wrapTrait(Trait trait) {
        traitContainer.addTagValue(trait, 1);
        return this;
    }

    public Creature wrapTraits(Trait... traits) {
        for(Trait trait : traits) {
            wrapTrait(trait);
        }
        return this;
    }

    public void applyBuff(Stat stat, StatChangeSource source, int amount) {
        statsContainer.addBuff(stat, source, amount);
    }

    public void applyDebuff(Stat stat, StatChangeSource source, int amount) {
        statsContainer.addDebuff(stat, source, amount);
    }

    public void clearBuffsFromSource(Stat stat, StatChangeSource source) {
        statsContainer.clearBuffsFromSource(stat, source);
    }

    public void clearBuffsFromSource(StatChangeSource source) {
        statsContainer.clearBuffsFromSource(source);
    }

    public void clearDebuffsFromSource(Stat stat, StatChangeSource source) {
        statsContainer.clearDebuffsFromSource(stat, source);
    }

    public void clearDebuffsFromSource(StatChangeSource source) {
        statsContainer.clearDebuffsFromSource(source);
    }

    public TraitContainer getTraitContainer() {
        return traitContainer;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getShopView() {
        StringBuilder view = new StringBuilder(name
                + " (" + cost + ") "
                + "[AD: " + getAttack() + ", "
                + "HP: " + getHp() + "] "
                + "<PArm: " + getPhysicalArmor() + ", "
                + "MArm: " + getMagicArmor() + ", "
                + "Speed: " + getSpeed() + ">");
        List<String> tagsView = new ArrayList<>();
        if (this.hasTag(CreatureTag.POISONOUS)) {
            tagsView.add("Яд " + this.getTagValue(CreatureTag.POISONOUS));
        }
        if (tagsView.size() > 0) {
            view.append(" {");
            view.append(tagsView.get(0));
            for (int i = 1; i < tagsView.size(); i++) {
                view.append(", ").append(tagsView.get(i));
            }
            view.append("}");
        }
        return view.toString();
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public List<String> getBattleView() {
        int rowSize = Constants.BATTLE_VIEW_LENGTH.value;
        int height = Constants.BATTLE_VIEW_HEIGHT.value;
        int offset = 1;
        int curHeight = 0;
        int curLength = 0;
        StringBuilder view = new StringBuilder();
        String curHp = getHp() + " ".repeat(offset);
        String curAttack = " ".repeat(offset) + getAttack();
        String[] nameParts = getName().split(" ");

        view.append("-").append("-".repeat(rowSize)).append("-\n");
        curHeight += 1;

        for (String namePart : nameParts) {
            if (curHeight >= height - 3) {
                break;
            }
            if (namePart.length() > rowSize) {
                continue;
            }
            if (curLength > 0) {
                if (namePart.length() + curLength < rowSize) {
                    view.append(" ").append(namePart);
                    curLength += namePart.length() + 1;
                } else {
                    view.append(" ".repeat(rowSize - curLength)).append("|\n");
                    view.append("|").append(" ".repeat(offset)).append(namePart);
                    curLength = namePart.length() + offset;
                    curHeight += 1;
                    continue;
                }
            } else {
                view.append("|");
                if (namePart.length() + curLength <= rowSize) {
                    view.append(" ".repeat(offset)).append(namePart);
                    curLength += namePart.length() + offset;
                } else {
                    view.append(" ".repeat(rowSize - curLength)).append("|\n");
                    view.append("|").append(" ".repeat(offset)).append(namePart);
                    curLength = namePart.length() + offset;
                    curHeight += 1;
                    continue;
                }
            }
        }
        view.append(" ".repeat(rowSize - curLength)).append("|\n");

        view.append(("|" + " ".repeat(rowSize) + "|\n").repeat(height - 3 - curHeight));


        view.append("|").append(curAttack).append(" ".repeat(rowSize - curHp.length() - curAttack.length())).append(curHp).append("|\n");
        view.append("-").append("-".repeat(rowSize)).append("-\n");

        return Arrays.stream(view.toString().split("\n")).collect(Collectors.toList());
    }
}
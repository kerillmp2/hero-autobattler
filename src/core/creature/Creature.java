package core.creature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.action.Action;
import core.action.ActionFactory;
import core.battle.HasBattleView;
import core.controllers.LevelController;
import core.creature.skills.CreatureSkill;
import core.creature.skills.CreatureSkillFactory;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.creature.stat.StatsContainer;
import core.item.Item;
import core.shop.HasShopView;
import core.traits.Trait;
import core.traits.TraitContainer;
import core.viewers.CreatureBattleViewer;
import core.viewers.CreatureShopViewer;
import utils.TagContainer;

public class Creature extends TagContainer<CreatureTag> implements HasShopView, HasBattleView {
    private String name;
    private int cost;
    private int level;
    private CreatureSkill skill = CreatureSkillFactory.emptySkill();
    private TraitContainer traitContainer;
    private StatsContainer statsContainer;
    private List<Action> actions;
    private List<Item> items;

    private Creature(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int maxMana, int cost, int level, List<Item> items, List<Action> actions, CreatureTag... tags) {
        super(Arrays.asList(tags));
        this.name = name;
        traitContainer = new TraitContainer();
        statsContainer = new StatsContainer();
        statsContainer.addTagValue(Stat.HP, hp);
        statsContainer.addTagValue(Stat.ATTACK, attack);
        statsContainer.addTagValue(Stat.PHYSICAL_ARMOR, physicalArmor);
        statsContainer.addTagValue(Stat.MAGIC_ARMOR, magicArmor);
        statsContainer.addTagValue(Stat.SPELL_POWER, spellPower);
        statsContainer.addTagValue(Stat.SPEED, speed);
        statsContainer.addTagValue(Stat.MANA, maxMana);
        this.cost = cost;
        this.level = level;
        this.items = items;
        this.actions = actions;
    }

    public static Creature shopDummy() {
        return new Creature("Sold", 0, 0, 0, 0, 0,0, 0, 0, 1, new ArrayList<>(), new ArrayList<>());
    }

    public static Creature benchDummy() {
        return new Creature("Empty", 0, 0, 0, 0, 0, 0, 0, 0, 1, new ArrayList<>(), new ArrayList<>());
    }

    public static Creature withStats(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int maxMana, int cost) {
        return new Creature(name, hp, attack, physicalArmor, magicArmor, spellPower, speed, maxMana, cost,
                1, new ArrayList<>(), ActionFactory.defaultCreatureActions(), CreatureTag.HAVE_BASIC_ATTACK);
    }

    @Override
    public int getTagValue(CreatureTag tag) {
        int defaultValue = super.getTagValue(tag);
        int change = statsContainer.getTagValue(tag);
        return Math.max(defaultValue + change, 0);
    }

    public String getName() {
        return name;
    }

    public String getNameLevel() {
        return name + " <" + level + ">";
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

    public int getMaxMana() {
        return statsContainer.getTagValue(Stat.MANA);
    }

    public int getStat(Stat stat) {
        return statsContainer.getTagValue(stat);
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

    public void apply(StatsContainer.StatChange statChange) {
        apply(statChange.getStat(), statChange.getSource(), statChange.getAmount(), statChange.isPercentage());
    }

    public void apply(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        if (amount > 0) {
            applyBuff(stat, source, amount, isPercentage);
        }
        if (amount < 0) {
            applyDebuff(stat, source, -amount, isPercentage);
        }
    }

    public void applyBuff(Stat stat, StatChangeSource source, int amount) {
        statsContainer.addBuff(stat, source, amount, false);
    }

    public void applyBuff(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        statsContainer.addBuff(stat, source, amount, isPercentage);
    }

    public void applyDebuff(Stat stat, StatChangeSource source, int amount) {
        statsContainer.addDebuff(stat, source, amount, false);
    }

    public void applyDebuff(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        statsContainer.addDebuff(stat, source, amount, isPercentage);
    }

    public void applyCreatureTagChange(CreatureTag creatureTag, StatChangeSource source, int amount) {
        statsContainer.addTagChange(creatureTag, source, amount);
    }

    public void clearAllChangesFromSource(StatChangeSource source) {
        statsContainer.clearAllChangesFromSource(source);
    }

    public void clearAllChangesFromAllSources() {
        for (StatChangeSource source : StatChangeSource.values()) {
            clearAllChangesFromSource(source);
        }
    }

    public TraitContainer getTraitContainer() {
        return traitContainer;
    }

    public Creature wrapSkill(CreatureSkill skill) {
        this.skill = skill;
        return this;
    }

    public CreatureSkill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public List<Item> getItems() {
        return this.items;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getShopView() {
        return getShopView(false, true, true, true);
    }

    public String getShopView(boolean showLevel, boolean showCost, boolean showTraits, boolean showStats) {
        return CreatureShopViewer.getShopViewFor(this, showLevel, showCost, showTraits, showStats);
    }

    @Override
    public int getSellingCost() {
        return cost * level;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public List<String> getBattleView() {
        return CreatureBattleViewer.getCreatureView(getNameLevel(), getAttack(), getHp(), getItems(), this);
    }
}
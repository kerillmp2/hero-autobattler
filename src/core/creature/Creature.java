package core.creature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import core.battle.HasBattleView;
import core.creature.skills.CreatureSkill;
import core.creature.skills.CreatureSkillFactory;
import core.shop.HasShopView;
import core.traits.Trait;
import core.controllers.TraitContainer;
import core.viewers.CreatureBattleViewer;
import utils.Constants;
import utils.TagContainer;

public class Creature extends TagContainer<CreatureTag> implements HasShopView, HasBattleView {
    private String name;
    private int cost;
    private int level;
    private CreatureSkill skill = CreatureSkillFactory.emptySkill();
    private TraitContainer traitContainer;
    private StatsContainer statsContainer;

    public Creature(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int maxMana, int cost, int level, CreatureTag... tags) {
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
    }

    public static Creature shopDummy() {
        return new Creature("Sold", 0, 0, 0, 0, 0,0, 0, 0, 1);
    }

    public static Creature benchDummy() {
        return new Creature("Empty", 0, 0, 0, 0, 0, 0, 0, 0, 1);
    }

    public static Creature withStats(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int maxMana, int cost) {
        return new Creature(name, hp, attack, physicalArmor, magicArmor, spellPower, speed, maxMana, cost, 1, CreatureTag.HAVE_BASIC_ATTACK);
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

    public void incrementLevel() {
        this.level++;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getShopView() {
        if (name.equals("Продано") || name.equals("Пусто") || name.equals("Sold") || name.equals("Empty")) {
            return name;
        }
        StringBuilder view = new StringBuilder();
        view.append(name).append(" <").append(level).append(">").append(" ".repeat(Constants.MAX_NAME_LEN.value - String.valueOf(level).length() - name.length() - 1));
        int traitsLen = 0;
        view.append("[");
        List<Trait> traits = new ArrayList<>(traitContainer.getTags());
        for (int i = 0; i < traits.size(); i++) {
            view.append(traits.get(i));
            traitsLen += traits.get(i).getName().length();
            if (i < traits.size() - 1) {
                view.append(", ");
                traitsLen += 2;
            }
        }
        view.append("]");
        view.append(" ".repeat(Constants.MAX_TRAIT_NAME_LEN.value * 3 - traitsLen + 1));

        StringBuilder statsView = new StringBuilder();

        statsView.append("(").append(getSellingCost()).append(") ")
                .append("[AD: ").append(getAttack()).append(", ").append("HP: ").append(getHp()).append("]");
        statsView.append(" ".repeat(Constants.AD_HP_LEN.value - statsView.length()));
        statsView.append("<PhysArm: ").append(getPhysicalArmor()).append(", ").append("MagArm: ").append(getMagicArmor()).append(", ").append("Speed: ").append(getSpeed()).append(">");
        view.append(statsView);
        List<String> tagsView = new ArrayList<>();
        if (this.hasTag(CreatureTag.POISONOUS)) {
            tagsView.add("Poison " + this.getTagValue(CreatureTag.POISONOUS));
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
    public int getSellingCost() {
        return cost * level;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public List<String> getBattleView() {
        return CreatureBattleViewer.getCreatureView(getNameLevel(), getAttack(), getHp(), traitContainer.getTags(), this);
    }
}
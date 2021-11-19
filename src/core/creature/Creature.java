package core.creature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.battle.HasBattleView;
import core.shop.HasShopView;
import core.traits.Trait;
import core.controllers.TraitContainer;
import core.utils.Constants;
import core.utils.TagContainer;

public class Creature extends TagContainer<CreatureTag> implements HasShopView, HasBattleView {
    private String name;
    private int cost;
    private TraitContainer traitContainer;
    private StatsContainer statsContainer;

    public Creature(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int cost, CreatureTag... tags) {
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
        this.cost = cost;
    }

    public static Creature shopDummy() {
        return new Creature("Продано", 0, 0, 0, 0, 0,0, 0);
    }

    public static Creature withStats(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int cost) {
        return new Creature(name, hp, attack, physicalArmor, magicArmor, spellPower, speed, cost, CreatureTag.HAVE_BASIC_ATTACK);
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

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getShopView() {
        if (name.equals("Продано")) {
            return name;
        }
        StringBuilder view = new StringBuilder();
        view.append(name).append(" ".repeat(Constants.MAX_NAME_LEN.value - name.length() + 1));
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

        statsView.append("(").append(cost).append(") ")
                .append("[AD: ").append(getAttack()).append(", ").append("HP: ").append(getHp()).append("]");
        statsView.append(" ".repeat(Constants.AD_HP_LEN.value - statsView.length()));
        statsView.append("<PArm: ").append(getPhysicalArmor()).append(", ").append("MArm: ").append(getMagicArmor()).append(", ").append("Speed: ").append(getSpeed()).append(">");
        view.append(statsView);
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
        return CreatureViewer.getCreatureView(name, getAttack(), getHp(), traitContainer.getTags(), this);
    }
}
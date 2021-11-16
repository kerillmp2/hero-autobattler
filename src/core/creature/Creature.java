package core.creature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import core.shop.HasShopView;
import core.utils.TagContainer;

public class Creature extends TagContainer<CreatureTag> implements HasShopView {
    private String name;
    private int hp;
    private int attack;
    private int physicalArmor;
    private int magicArmor;
    private int spellPower;
    private int speed;
    private int cost;

    public Creature(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int cost, Map<CreatureTag, Integer> tagValues) {
        super(tagValues);
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.physicalArmor = physicalArmor;
        this.magicArmor = magicArmor;
        this.spellPower = spellPower;
        this.speed = speed;
        this.cost = cost;
    }

    public Creature(String name, int hp, int attack, int physicalArmor, int magicArmor, int spellPower, int speed, int cost, CreatureTag... tags) {
        super(Arrays.asList(tags));
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.physicalArmor = physicalArmor;
        this.magicArmor = magicArmor;
        this.spellPower = spellPower;
        this.speed = speed;
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
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getPhysicalArmor() {
        return physicalArmor;
    }

    public int getMagicArmor() {
        return magicArmor;
    }

    public int getSpellPower() {
        return spellPower;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getShopView() {
        StringBuilder view = new StringBuilder(name
                + " (" + cost + ") "
                + "[AD: " + attack + ", "
                + "HP: " + hp + "] "
                + "<PArm: " + physicalArmor + ", "
                + "MArm: " + magicArmor + ", "
                + "Speed: " + speed + ">");
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
}
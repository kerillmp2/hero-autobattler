package core.battlefield;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import core.action.ActionFactory;
import core.action.ResolveTime;
import core.battle.HasBattleView;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.Stat;
import core.creature.StatChangeSource;
import core.creature.WithStats;
import core.viewers.CreatureBattleViewer;
import utils.Constants;

public class BattlefieldCreature extends BattlefieldObject implements WithStats, HasBattleView {
    private Creature creature;
    private int currentHp;
    private int currentAttack;
    private int currentPhysicalArmor;
    private int currentMagicArmor;
    private int currentSpellPower;
    private int currentSpeed;
    private int currentMana;
    private final String battleName;
    private Battlefield battlefield;
    private BattlefieldSide battlefieldSide;

    public BattlefieldCreature(Creature creature, Position position, String battleName, Set<ObjectStatus> statusSet) {
        super(statusSet, position);
        this.creature = creature;
        beforeBattleStart();
        this.currentHp = creature.getHp();
        this.currentAttack = creature.getAttack();
        this.currentPhysicalArmor = creature.getPhysicalArmor();
        this.currentMagicArmor = creature.getMagicArmor();
        this.currentSpellPower = creature.getSpellPower();
        this.currentSpeed = creature.getSpeed();
        this.battleName = battleName;
        this.battlefield = null;
    }

    public BattlefieldCreature(Creature creature, Position position, ObjectStatus... statuses) {
        this(creature, position, Arrays.stream(statuses).collect(Collectors.toSet()));
    }

    public BattlefieldCreature(Creature creature, Position position, Set<ObjectStatus> statusSet) {
        this(creature, position, creature.getName(), statusSet);
    }

    private void beforeBattleStart() {
        addStatus(ObjectStatus.CREATURE);

        this.addAction(ActionFactory.chooseMainActionAction(this));
        this.addAction(ActionFactory.addManaAction(this, Constants.MANA_AFTER_TAKING_DAMAGE.value, ResolveTime.AFTER_TAKING_DAMAGE));
        this.addAction(ActionFactory.addManaAction(this, Constants.MANA_AFTER_DEALING_DAMAGE.value, ResolveTime.AFTER_DEALING_DAMAGE));

        //EATER TRAIT
        int additionalHP = creature.getTagValue(CreatureTag.ADD_PERMANENT_HP_BEFORE_BATTLE);
        if (additionalHP > 0) {
            creature.applyBuff(Stat.HP, StatChangeSource.PERMANENT, additionalHP);
            MessageController.print(
                    creature.getName() + " навсегда получает " + additionalHP + " HP",
                    creature.getName() + " gains " + additionalHP + " HP"
            );
        }

        //ROBOT TRAIT
        if (creature.getTagValue(CreatureTag.ADD_TEMP_HP_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.HP;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_HP_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(
                    String.format("%s получает %d %s до конца боя", creature.getName(), amount, stat.getName()),
                    String.format("%s gains %d %s until battle end", creature.getName(), amount, stat.getName())
            );
        }
        if (creature.getTagValue(CreatureTag.ADD_TEMP_ATTACK_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.ATTACK;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_ATTACK_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(
                    String.format("%s получает %d %s до конца боя", creature.getName(), amount, stat.getName()),
                    String.format("%s gains %d %s until battle end", creature.getName(), amount, stat.getName())
            );
        }
        if (creature.getTagValue(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.PHYSICAL_ARMOR;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(
                    String.format("%s получает %d %s до конца боя", creature.getName(), amount, stat.getName()),
                    String.format("%s gains %d %s until battle end", creature.getName(), amount, stat.getName())
            );
        }

        //STUDENT TRAIT
        int manaAmount = creature.getTagValue(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL);
        if (manaAmount > 0) {
            this.addAction(ActionFactory.addManaAction(this, manaAmount, ResolveTime.AFTER_ALLY_USING_SKILL));
        }
    }

    public void onBattleEnd() {
        creature.clearAllChangesFromSource(StatChangeSource.UNTIL_BATTLE_END);
    }

    public String useSkill() {
        this.setCurrentMana(0);
        return this.creature.getSkill().cast(battlefield.getBattleController(), this);
    }

    public boolean hasEnoughManaForSkill() {
        return currentMana >= creature.getMaxMana();
    }

    public int getMaxHp() {
        return creature.getHp();
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.min(creature.getHp(), currentHp);
        if (this.currentHp <= 0) {
            this.removeStatus(ObjectStatus.ALIVE);
            this.addStatus(ObjectStatus.DEAD);
        }
    }

    @Override
    public int getCurrentAttack() {
        return currentAttack;
    }

    @Override
    public void setCurrentAttack(int currentAttack) {
        this.currentAttack = currentAttack;
    }

    @Override
    public int getCurrentPhysicalArmor() {
        return currentPhysicalArmor;
    }

    @Override
    public void setCurrentPhysicalArmor(int currentPhysicalArmor) {
        this.currentPhysicalArmor = currentPhysicalArmor;
    }

    @Override
    public int getCurrentMagicArmor() {
        return currentMagicArmor;
    }

    @Override
    public void setCurrentMagicArmor(int currentMagicArmor) {
        this.currentMagicArmor = currentMagicArmor;
    }

    @Override
    public int getCurrentSpellPower() {
        return currentSpellPower;
    }

    @Override
    public void setCurrentSpellPower(int currentSpellPower) {
        this.currentSpellPower = currentSpellPower;
    }

    public String getBattleName() {
        return battleName + " [HP: " + currentHp + "]";
    }

    @Override
    public int getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    @Override
    public int getCurrentMana() {
        return currentMana;
    }

    @Override
    public void setCurrentMana(int currentMana) {
        this.currentMana = Math.min(creature.getMaxMana(), currentMana);
    }

    public int getMaxMana() {
        return creature.getMaxMana();
    }

    public void addMana(int amount) {
        this.currentMana = Math.min(creature.getMaxMana(), currentMana + amount);
    }

    public void setBattlefield(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public Creature getCreature() {
        return creature;
    }

    public void setCreature(Creature creature) {
        this.creature = creature;
    }

    public BattlefieldSide getBattlefieldSide() {
        return battlefieldSide;
    }

    public void setBattlefieldSide(BattlefieldSide battlefieldSide) {
        this.battlefieldSide = battlefieldSide;
    }

    public int getTagValue(CreatureTag tag) {
        return creature.getTagValue(tag);
    }

    public boolean hasTag(CreatureTag tag) {
        return creature.hasTag(tag);
    }

    public int get(Stat stat) {
        switch (stat) {
            case HP:
                return getCurrentHp();
            case MANA:
                return getCurrentMana();
            case ATTACK:
                return getCurrentAttack();
            case SPEED:
                return getCurrentSpeed();
            case SPELL_POWER:
                return getCurrentSpellPower();
            case PHYSICAL_ARMOR:
                return getCurrentPhysicalArmor();
            case MAGIC_ARMOR:
                return getCurrentMagicArmor();
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return this.battleName;
    }

    @Override
    public List<String> getBattleView() {
        return CreatureBattleViewer.getCreatureView(creature.getNameLevel(), getCurrentAttack(), getCurrentHp(), creature.getTraitContainer().getTags(), creature);
    }
}

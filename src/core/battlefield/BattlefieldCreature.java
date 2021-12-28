package core.battlefield;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ResolveTime;
import core.battle.HasBattleView;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.creature.stat.StatsContainer;
import core.creature.stat.WithStats;
import core.viewers.CreatureBattleViewer;
import utils.Constants;

public class BattlefieldCreature extends BattlefieldObject implements WithStats, HasBattleView {
    private Creature creature;
    private int currentMana;
    private final StatsContainer statsContainer;
    private final String battleName;
    private Battlefield battlefield;
    private BattlefieldSide battlefieldSide;

    public BattlefieldCreature(Creature creature, String battleName, StatsContainer statsContainer, Set<ObjectStatus> statusSet) {
        super(statusSet);
        this.creature = creature;
        this.statsContainer = statsContainer;
        beforeBattleStart();
        statsContainer.setTagValue(Stat.HP, creature.getHp());
        statsContainer.setTagValue(Stat.ATTACK, creature.getAttack());
        statsContainer.setTagValue(Stat.PHYSICAL_ARMOR, creature.getPhysicalArmor());
        statsContainer.setTagValue(Stat.MAGIC_ARMOR, creature.getMagicArmor());
        statsContainer.setTagValue(Stat.SPELL_POWER, creature.getSpellPower());
        statsContainer.setTagValue(Stat.SPEED, creature.getSpeed());
        this.battleName = battleName;
        this.battlefield = null;
    }

    public BattlefieldCreature(Creature creature, ObjectStatus... statuses) {
        this(creature, Arrays.stream(statuses).collect(Collectors.toSet()));
    }

    public BattlefieldCreature(Creature creature, Set<ObjectStatus> statusSet) {
        this(creature, creature.getName(), new StatsContainer(), statusSet);
    }

    private void beforeBattleStart() {
        addStatus(ObjectStatus.CREATURE);

        List<Action> creatureActions = creature.getActions();
        for (Action action : creatureActions) {
            this.addAction(action.withPerformer(this));
        }

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
        if (creature.getTagValue(CreatureTag.ADD_TEMP_MARM_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.MAGIC_ARMOR;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_MARM_BEFORE_BATTLE);
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

    public int getStat(Stat stat) {
        return statsContainer.getTagValue(stat);
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

    @Override
    public int getCurrentHp() {
        return statsContainer.getTagValue(Stat.HP);
    }

    @Override
    public void setCurrentHp(int currentHp) {
        statsContainer.setTagValue(Stat.HP, Math.max(Math.min(creature.getHp(), currentHp), 0));
        if (statsContainer.getTagValue(Stat.HP) <= 0) {
            this.removeStatus(ObjectStatus.ALIVE);
            this.addStatus(ObjectStatus.DEAD);
        }
    }

    @Override
    public int getCurrentAttack() {
        return statsContainer.getTagValue(Stat.ATTACK);
    }

    @Override
    public void setCurrentAttack(int currentAttack) {
        statsContainer.setTagValue(Stat.ATTACK, Math.max(currentAttack, 0));
    }

    @Override
    public int getCurrentPhysicalArmor() {
        return statsContainer.getTagValue(Stat.PHYSICAL_ARMOR);
    }

    @Override
    public void setCurrentPhysicalArmor(int currentPhysicalArmor) {
        statsContainer.setTagValue(Stat.PHYSICAL_ARMOR, currentPhysicalArmor);
    }

    @Override
    public int getCurrentMagicArmor() {
        return statsContainer.getTagValue(Stat.MAGIC_ARMOR);
    }

    @Override
    public void setCurrentMagicArmor(int currentMagicArmor) {
        statsContainer.setTagValue(Stat.MAGIC_ARMOR, currentMagicArmor);
    }

    @Override
    public int getCurrentSpellPower() {
        return statsContainer.getTagValue(Stat.SPELL_POWER);
    }

    @Override
    public void setCurrentSpellPower(int currentSpellPower) {
        statsContainer.setTagValue(Stat.SPELL_POWER, Math.max(currentSpellPower, 0));
    }

    public String getBattleName() {
        return battleName + " [HP: " + getCurrentHp() + "]";
    }

    @Override
    public int getCurrentSpeed() {
        return statsContainer.getTagValue(Stat.SPEED);
    }

    @Override
    public void setCurrentSpeed(int currentSpeed) {
        statsContainer.setTagValue(Stat.SPEED, currentSpeed);
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

    @Override
    public String toString() {
        return this.battleName;
    }

    @Override
    public List<String> getBattleView() {
        return CreatureBattleViewer.getCreatureView(creature.getNameLevel(), getCurrentAttack(), getCurrentHp(), creature.getTraitContainer().getTags(), creature);
    }
}

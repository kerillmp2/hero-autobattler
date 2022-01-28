package core.battlefield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;
import core.battle.HasBattleView;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.skills.CreatureSkill;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import core.creature.stat.StatsContainer;
import core.creature.stat.WithStats;
import core.viewers.CreatureBattleViewer;
import core.viewers.CreatureShopViewer;
import statistics.Metric;
import statistics.StatisticCollector;
import utils.Calculator;
import utils.Constants;
import utils.Pair;

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

    public static BattlefieldCreature fromCreature(Creature creature) {
        return new BattlefieldCreature(creature, ObjectStatus.ALIVE);
    }

    private void beforeBattleStart() {
        addStatus(ObjectStatus.CREATURE);

        List<Action> creatureActions = creature.getActions();
        for (Action action : creatureActions) {
            this.addAction(action.withPerformer(this));
        }

        //EATER TRAIT
        int floatAdditionalHP = creature.getTagValue(CreatureTag.ADD_PERMANENT_FLOAT_HP_BEFORE_BATTLE);
        if (floatAdditionalHP > 0) {
            creature.applyBuff(Stat.HP, StatChangeSource.PERMANENT, floatAdditionalHP);
            MessageController.print(creature.getName() + " gains " + floatAdditionalHP + " HP");
        }
        int percentageOfAdditionalHP = creature.getTagValue(CreatureTag.ADD_PERMANENT_PERCENTAGE_HP_BEFORE_BATTLE);
        if (percentageOfAdditionalHP > 0) {
            int additionalHP = (int) (creature.getHp() * percentageOfAdditionalHP / 100.0);
            creature.applyBuff(Stat.HP, StatChangeSource.PERMANENT, additionalHP);
            MessageController.print(creature.getName() + " gains " + additionalHP + " HP");
        }

        //ROBOT TRAIT
        if (creature.getTagValue(CreatureTag.ADD_TEMP_PERCENTAGE_HP_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.HP;
            int percentage = creature.getTagValue(CreatureTag.ADD_TEMP_PERCENTAGE_HP_BEFORE_BATTLE);
            int amount = (int) (creature.getHp() * percentage / 100.0);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(String.format("%s gains %d %s until battle end", creature.getName(), amount, stat.getName()));
        }
        if (creature.getTagValue(CreatureTag.ADD_TEMP_PERCENTAGE_ATTACK_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.ATTACK;
            int percentage = creature.getTagValue(CreatureTag.ADD_TEMP_PERCENTAGE_ATTACK_BEFORE_BATTLE);
            int amount = (int) (creature.getAttack() * percentage / 100.0);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(String.format("%s gains %d %s until battle end", creature.getName(), amount, stat.getName()));
        }
        if (creature.getTagValue(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.PHYSICAL_ARMOR;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(String.format("%s gains %d %s until battle end", creature.getName(), amount, stat.getName()));
        }
        if (creature.getTagValue(CreatureTag.ADD_TEMP_MARM_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.MAGIC_ARMOR;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_MARM_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(String.format("%s gains %d %s until battle end", creature.getName(), amount, stat.getName()));
        }

        // Demon Trait
        int burnPercentageMana = creature.getTagValue(CreatureTag.BURN_PERCENTAGE_MANA_ON_ATTACK);
        if (burnPercentageMana > 0) {
            this.addAction(ActionFactory.ActionBuilder.empty().from(this).withTime(ResolveTime.AFTER_ATTACK).wrapTag(ActionTag.BASIC_ATTACK_BUFF).wrapTag(ActionTag.REDUCE_PERCENTAGE_STAT).wrapTag(ActionTag.MANA, burnPercentageMana).build());
        }
        int burnFloatMana = creature.getTagValue(CreatureTag.BURN_FLOAT_MANA_ON_ATTACK);
        if (burnFloatMana > 0) {
            this.addAction(ActionFactory.ActionBuilder.empty().from(this).withTime(ResolveTime.AFTER_ATTACK).wrapTag(ActionTag.BASIC_ATTACK_BUFF).wrapTag(ActionTag.REDUCE_FLOAT_STAT).wrapTag(ActionTag.MANA, burnFloatMana).build());
        }

        //STUDENT TRAIT
        int manaAmount = creature.getTagValue(CreatureTag.ADD_MANA_AFTER_ALLY_USED_SKILL);
        if (manaAmount > 0) {
            this.addAction(ActionFactory.addManaAction(this, manaAmount, ResolveTime.AFTER_ALLY_USING_SKILL));
        }

        // Duelist Trait
        int counterAttackChance = creature.getTagValue(CreatureTag.COUNTERATTACK_CHANCE);
        int counterAttackDamage = creature.getTagValue(CreatureTag.COUNTERATTACK_DAMAGE);
        if (counterAttackChance > 0 && counterAttackDamage > 0) {
            this.addAction(ActionFactory.ActionBuilder.empty().from(this).withTime(ResolveTime.AFTER_ATTACKED)
                    .wrapTag(ActionTag.BASIC_ATTACK_RESPONSE)
                    .wrapTag(ActionTag.CHANCE, counterAttackChance)
                    .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE)
                    .wrapTag(ActionTag.BASED_ON_STAT, counterAttackDamage)
                    .wrapTag(ActionTag.ATTACK)
                    .withPrefix("%s counterattacks %s!").build());
        }

        // Frostborn Trait
        int additionalMagicDamageOnAttack = creature.getTagValue(CreatureTag.ADDITIONAL_MAGIC_DAMAGE_ON_ATTACK);
        int slowOnAttack = creature.getTagValue(CreatureTag.PERCENTAGE_SLOW_ON_ATTACK);
        if (additionalMagicDamageOnAttack > 0 && slowOnAttack > 0) {
            this.addAction(ActionFactory.ActionBuilder.empty().from(this).withTime(ResolveTime.AFTER_ATTACK)
                    .wrapTag(ActionTag.BASIC_ATTACK_BUFF)
                    .wrapTag(ActionTag.DEAL_MAGIC_DAMAGE)
                    .wrapTag(ActionTag.BASED_ON_STAT, additionalMagicDamageOnAttack)
                    .wrapTag(ActionTag.SPELL_POWER)
                    .build());
            this.addAction(ActionFactory.ActionBuilder.empty().from(this).withTime(ResolveTime.AFTER_ATTACK)
                    .wrapTag(ActionTag.BASIC_ATTACK_BUFF)
                    .wrapTag(ActionTag.SPEED, slowOnAttack)
                    .wrapTag(ActionTag.REDUCE_PERCENTAGE_STAT)
                    .build());
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
        return useSkill(this.creature.getSkill());
    }

    public String useBouncingSkill() {
        return useSkill(this.creature.getBouncingSkill());
    }

    public String useSkill(CreatureSkill skill) {
        return skill.cast(battlefield.getBattleController(), this);
    }

    public boolean hasEnoughManaForSkill() {
        return currentMana >= creature.getMaxMana();
    }

    public int getMaxHp() {
        return creature.getHp();
    }

    private void apply(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        int oldValue = this.getStat(stat);
        if (amount > 0) {
            statsContainer.addBuff(stat, source, amount, isPercentage);
        }
        if (amount < 0) {
            statsContainer.addDebuff(stat, source, (-1) * amount, isPercentage);
        }
        int newValue = this.getStat(stat);
        if (Constants.COLLECT_STATISTIC.value != 0) {
            StatisticCollector.updateCreatureStatistic(this.getCreature().getName(), stat, oldValue, newValue);
        }
    }

    public void applyBuff(Stat stat, StatChangeSource source, int amount) {
        apply(stat, source, amount, false);
    }

    public void applyBuff(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        apply(stat, source, amount, isPercentage);
    }

    public void applyDebuff(Stat stat, StatChangeSource source, int amount) {
        apply(stat, source, (-1) * amount, false);
    }

    public void applyDebuff(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        apply(stat, source, (-1) * amount, isPercentage);
    }

    public void applyCreatureTagChange(CreatureTag creatureTag, StatChangeSource source, int amount) {
        this.getCreature().applyCreatureTagChange(creatureTag, source, amount);
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

    public String takeMagicDamage(int amount, String source) {
        String message = "";
        int currentHp = getCurrentHp();
        int damage = Calculator.calculateMagicDamage(amount, this, true);
        int damageAfterMagicBarrier = reduceMagicBarrier(damage);
        if (damage > damageAfterMagicBarrier) {
            message += this.getBattleName() + " absorbed " + (damage - damageAfterMagicBarrier) + " with magic barrier! [Magic barrier left: " + this.getTagValue(CreatureTag.MAGIC_BARRIER) + "]\n";
        }
        int damageAfterBarrier = reduceBarrier(damageAfterMagicBarrier);
        if (damageAfterMagicBarrier > damageAfterBarrier) {
            message += this.getBattleName() + " absorbed " + (damageAfterMagicBarrier - damageAfterBarrier) + " with barrier! [Barrier left: " + this.getTagValue(CreatureTag.BARRIER) + "]\n";
        }
        setCurrentHp(currentHp - damageAfterBarrier);
        if (Constants.COLLECT_STATISTIC.value > 0) {
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.MAGIC_DAMAGE_TAKEN, damage);
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.TOTAL_DAMAGE_TAKEN, damage);
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.MAGIC_BARRIER_ABSORBED, Math.max(damage - damageAfterMagicBarrier, 0));
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.BARRIER_ABSORBED, Math.max(damageAfterMagicBarrier - damageAfterBarrier, 0));
            StatisticCollector.updateCreatureMetric(source, Metric.MAGIC_DAMAGE_DEALT, damage);
            StatisticCollector.updateCreatureMetric(source, Metric.TOTAL_DAMAGE_DEALT, damage);
        }
        message += this.getBattleName() + " takes " + damageAfterBarrier + " magic damage from " + source + "!\n";
        return message;
    }

    public String takePhysicalDamage(int amount, String source) {
        String message = "";
        int currentHp = getCurrentHp();
        int damage = Calculator.calculatePhysicalDamage(amount, this, true);
        int damageAfterPhysicalBarrier = reducePhysicalBarrier(damage);
        if (damage > damageAfterPhysicalBarrier) {
            message += this.getBattleName() + " absorbed " + (damage - damageAfterPhysicalBarrier) + " with physical barrier! [Physical barrier left: " + this.getTagValue(CreatureTag.PHYSICAL_BARRIER) + "]\n";
        }
        int damageAfterBarrier = reduceBarrier(damageAfterPhysicalBarrier);
        if (damageAfterPhysicalBarrier > damageAfterBarrier) {
            message += this.getBattleName() + " absorbed " + (damageAfterPhysicalBarrier - damageAfterBarrier) + " with barrier! [Barrier left: " + this.getTagValue(CreatureTag.BARRIER) + "]\n";
        }
        setCurrentHp(currentHp - damageAfterBarrier);
        if (Constants.COLLECT_STATISTIC.value > 0) {
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.PHYSICAL_DAMAGE_TAKEN, damage);
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.TOTAL_DAMAGE_TAKEN, damage);
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.PHYSICAL_BARRIER_ABSORBED, Math.max(damage - damageAfterPhysicalBarrier, 0));
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.BARRIER_ABSORBED, Math.max(damageAfterPhysicalBarrier - damageAfterBarrier, 0));
            StatisticCollector.updateCreatureMetric(source, Metric.PHYSICAL_DAMAGE_DEALT, damage);
            StatisticCollector.updateCreatureMetric(source, Metric.TOTAL_DAMAGE_DEALT, damage);
        }
        message += this.getBattleName() + " takes " + damageAfterBarrier + " physical damage from " + source + "!\n";
        return message;
    }

    public String takeTrueDamage(int amount, String source) {
        String message = "";
        int currentHp = getCurrentHp();
        int damageAfterBarrier = reduceBarrier(amount);
        if (amount - damageAfterBarrier > 0) {
            message += this.getBattleName() + " absorbed " + (amount - damageAfterBarrier) + " with barrier! [Barrier left: " + this.getTagValue(CreatureTag.BARRIER) + "]\n";
        }
        setCurrentHp(currentHp - damageAfterBarrier);
        if (Constants.COLLECT_STATISTIC.value > 0) {
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.TRUE_DAMAGE_DEALT, amount);
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.TOTAL_DAMAGE_TAKEN, amount);
            StatisticCollector.updateCreatureMetric(this.getCreature().getName(), Metric.BARRIER_ABSORBED, Math.max(amount - damageAfterBarrier, 0));
            StatisticCollector.updateCreatureMetric(source, Metric.TRUE_DAMAGE_DEALT, amount);
            StatisticCollector.updateCreatureMetric(source, Metric.TOTAL_DAMAGE_DEALT, amount);
        }
        message += this.getBattleName() + " takes " + amount + " true damage from " + source + "!\n";
        return message;
    }

    public int reduceMagicBarrier(int damage) {
        int barrier = this.getTagValue(CreatureTag.MAGIC_BARRIER);
        if (barrier <= 0) {
            return damage;
        }
        int damageAfterBarrier = Math.max(damage - barrier, 0);
        this.creature.applyCreatureTagChange(CreatureTag.MAGIC_BARRIER, StatChangeSource.UNTIL_BATTLE_END, (-1) * Math.min(damage, barrier));
        return damageAfterBarrier;
    }

    public int reducePhysicalBarrier(int damage) {
        int barrier = this.getTagValue(CreatureTag.PHYSICAL_BARRIER);
        if (barrier <= 0) {
            return damage;
        }
        int damageAfterBarrier = Math.max(damage - barrier, 0);
        this.creature.applyCreatureTagChange(CreatureTag.PHYSICAL_BARRIER, StatChangeSource.UNTIL_BATTLE_END, (-1) * Math.min(damage, barrier));
        return damageAfterBarrier;
    }

    public int reduceBarrier(int damage) {
        int barrier = this.getTagValue(CreatureTag.BARRIER);
        if (barrier <= 0) {
            return damage;
        }
        int damageAfterBarrier = Math.max(damage - barrier, 0);
        this.creature.applyCreatureTagChange(CreatureTag.BARRIER, StatChangeSource.UNTIL_BATTLE_END, (-1) * Math.min(damage, barrier));
        return damageAfterBarrier;
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

    public int getLevel() {
        return this.creature.getLevel();
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
        return CreatureBattleViewer.getCreatureView(creature.getNameLevel(), getCurrentAttack(), getCurrentHp(),
                creature.getItems(), creature, getAdditionalViewInfo());
    }

    public String getShopView() {
        return getShopView(false, true, true, true);
    }

    public String getShopView(boolean showLevel, boolean showCost, boolean showTraits, boolean showStats) {
        return CreatureShopViewer.getShopViewFor(creature, statsContainer, showLevel, showCost, showTraits, showStats);
    }

    private List<Pair<String, String>> getAdditionalViewInfo() {
        List<Pair<String, String>> additionalInfo = new ArrayList<>();
        Action burnAction = this.getActionByTags(ActionTag.APPLY_BURN_DAMAGE);
        if (!burnAction.getActionInfo().hasTag(ActionTag.UNDEFINED)) {
            int burnAmount = burnAction.getActionInfo().getTagValue(ActionTag.APPLY_BURN_DAMAGE);
            additionalInfo.add(new Pair<>("Burn", String.valueOf(burnAmount)));
        }
        Action poisonedAction = this.getActionByTags(ActionTag.APPLY_POISON_DAMAGE);
        if (!poisonedAction.getActionInfo().hasTag(ActionTag.UNDEFINED)) {
            int poisonedAmount = poisonedAction.getActionInfo().getTagValue(ActionTag.APPLY_POISON_DAMAGE);
            additionalInfo.add(new Pair<>("Poisoned", String.valueOf(poisonedAmount)));
        }
        return additionalInfo;
    }
}

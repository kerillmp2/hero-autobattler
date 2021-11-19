package core.battlefield;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import core.battle.HasBattleView;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.Stat;
import core.creature.StatChangeSource;
import core.creature.WithStats;
import core.action.ActionFactory;
import core.creature.CreatureViewer;

public class BattlefieldCreature extends BattlefieldObject implements WithStats, HasBattleView {
    private Creature creature;
    private int currentHp;
    private int currentAttack;
    private int currentPhysicalArmor;
    private int currentMagicArmor;
    private int currentSpellPower;
    private int currentSpeed;
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
        for (CreatureTag creatureTag : creature.getTags()) {
            this.addTagValue(BattlefieldObjectTag.byName(creatureTag.getName()), creature.getTagValue(creatureTag));
        }
        if (hasTag(BattlefieldObjectTag.HAVE_BASIC_ATTACK)) {
            addAction(ActionFactory.generateAttackAction(this));
        }
        //EATER TRAIT
        int additionalHP = creature.getTagValue(CreatureTag.ADD_PERMANENT_HP_BEFORE_BATTLE);
        if (additionalHP > 0) {
            creature.applyBuff(Stat.HP, StatChangeSource.PERMANENT, additionalHP);
            MessageController.print(creature.getName() + " навсегда получает " + additionalHP + " HP");
        }

        //ROBOT TRAIT
        if (creature.getTagValue(CreatureTag.ADD_TEMP_HP_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.HP;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_HP_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(String.format("%s получает %d %s до конца боя", creature.getName(), amount, stat.getName()));
        }
        if (creature.getTagValue(CreatureTag.ADD_TEMP_ATTACK_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.ATTACK;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_ATTACK_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(String.format("%s получает %d %s до конца боя", creature.getName(), amount, stat.getName()));
        }
        if (creature.getTagValue(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE) > 0) {
            Stat stat = Stat.PHYSICAL_ARMOR;
            int amount = creature.getTagValue(CreatureTag.ADD_TEMP_PARM_BEFORE_BATTLE);
            creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
            MessageController.print(String.format("%s получает %d %s до конца боя", creature.getName(), amount, stat.getName()));
        }
    }

    public void onBattleEnd() {
        creature.clearAllChangesFromSource(StatChangeSource.UNTIL_BATTLE_END);
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
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

    @Override
    public String toString() {
        return this.battleName;
    }

    @Override
    public List<String> getBattleView() {
        return CreatureViewer.getCreatureView(creature.getName(), getCurrentAttack(), getCurrentHp(), creature.getTraitContainer().getTags(), creature);
    }
}

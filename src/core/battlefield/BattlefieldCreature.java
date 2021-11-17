package core.battlefield;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import core.battle.HasBattleView;
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
        addStatus(ObjectStatus.CREATURE);
        if (creature.getTagValue(CreatureTag.EATER) > 0) {
            creature.applyBuff(Stat.HP, StatChangeSource.PERMANENT, creature.getTagValue(CreatureTag.EATER));
        }
        this.creature = creature;
        this.currentHp = creature.getHp();
        this.currentAttack = creature.getAttack();
        this.currentPhysicalArmor = creature.getPhysicalArmor();
        this.currentMagicArmor = creature.getMagicArmor();
        this.currentSpellPower = creature.getSpellPower();
        this.currentSpeed = creature.getSpeed();
        this.battleName = battleName;
        this.battlefield = null;
        for (CreatureTag creatureTag : creature.getTags()) {
            this.addTagValue(BattlefieldObjectTag.byName(creatureTag.getName()), creature.getTagValue(creatureTag));
        }
        if (hasTag(BattlefieldObjectTag.HAVE_BASIC_ATTACK)) {
            addAction(ActionFactory.generateAttackAction(this));
        }
    }

    public BattlefieldCreature(Creature creature, Position position, ObjectStatus... statuses) {
        this(creature, position, Arrays.stream(statuses).collect(Collectors.toSet()));
    }

    public BattlefieldCreature(Creature creature, Position position, Set<ObjectStatus> statusSet) {
        this(creature, position, creature.getName(), statusSet);
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

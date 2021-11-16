package core.battlefield;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import core.battle.HasBattleView;
import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.WithStats;
import core.action.ActionFactory;
import core.utils.Constants;

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
        this.creature = creature;
        this.currentHp = creature.getHp();
        this.currentAttack = creature.getAttack();
        this.currentPhysicalArmor = creature.getPhysicalArmor();
        this.currentMagicArmor = creature.getMagicArmor();
        this.currentSpellPower = creature.getSpellPower();
        this.currentSpeed = creature.getSpeed();
        this.battleName = battleName;
        this.battlefield = null;
        for (Map.Entry<CreatureTag, Integer> creatureTag : creature.getTagValues().entrySet()) {
            this.addTagValue(BattlefieldObjectTag.byName(creatureTag.getKey().getName()), creatureTag.getValue());
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
        return battleName + "[" + currentHp + "]";
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
        int rowSize = Constants.BATTLE_VIEW_LENGTH.value;
        int height = Constants.BATTLE_VIEW_HEIGHT.value;
        int curHeight = 0;
        int curLength = 0;
        StringBuilder view = new StringBuilder();
        String curHp = String.valueOf(getCurrentHp());
        String curAttack = String.valueOf(getCurrentAttack());
        String[] nameParts = creature.getName().split(" ");

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
                    view.append("|").append(namePart);
                    curLength = namePart.length();
                    curHeight += 1;
                    continue;
                }
            } else {
                view.append("|");
                if (namePart.length() + curLength <= rowSize) {
                    view.append(namePart);
                    curLength += namePart.length();
                } else {
                    view.append(" ".repeat(rowSize - curLength)).append("|\n");
                    view.append("|").append(namePart);
                    curLength = namePart.length();
                    curHeight += 1;
                    continue;
                }
            }
        }
        view.append(" ".repeat(rowSize - curLength)).append("|\n");

        view.append(("|" + " ".repeat(rowSize) + "|\n").repeat(height - 2 - curHeight));


        view.append("|").append(curAttack).append(" ".repeat(rowSize - curHp.length() - curAttack.length())).append(curHp).append("|\n");
        view.append("-").append("-".repeat(rowSize)).append("-\n");

        return Arrays.stream(view.toString().split("\n")).collect(Collectors.toList());
    }
}

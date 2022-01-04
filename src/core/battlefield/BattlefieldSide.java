package core.battlefield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.controllers.utils.RandomController;
import core.creature.stat.Stat;
import core.player.Board;
import core.creature.Creature;

public class BattlefieldSide {

    private final List<BattlefieldCreature> creatures;
    private BattlefieldSide oppositeSide;

    private BattlefieldSide() {
        this.creatures = new ArrayList<>();
    }

    private BattlefieldSide(List<BattlefieldCreature> creatures) {
        this();
        this.creatures.addAll(creatures);
        getCreatures().forEach(creature -> creature.setBattlefieldSide(this));
    }

    public static BattlefieldSide fromBoard(Board board) {
        List<BattlefieldCreature> creatures = new ArrayList<>();
        board.updateTraitBuffs();
        List<Creature> creaturesOnBoard = board.getCreatures();
        for (Creature creature : creaturesOnBoard) {
            BattlefieldCreature battlefieldCreature = new BattlefieldCreature(creature, ObjectStatus.ALIVE);
            creatures.add(battlefieldCreature);
        }
        BattlefieldSide battlefieldSide = new BattlefieldSide(creatures);
        for (BattlefieldCreature creature : battlefieldSide.getCreatures()) {
            creature.setBattlefieldSide(battlefieldSide);
        }
        return battlefieldSide;
    }

    public boolean hasAliveCreature() {
        return this.getCreatures().stream().anyMatch(creature -> creature.hasStatus(ObjectStatus.ALIVE));
    }

    public void addCreature(BattlefieldCreature creature) {
        this.creatures.add(creature);
        creature.setBattlefieldSide(this);
    }

    public List<BattlefieldCreature> getCreatures() {
        return creatures;
    }

    public BattlefieldCreature getRandomSideCreature() {
        return getRandomSideCreature(this);
    }

    public BattlefieldCreature getRandomOppositeSideCreature() {
        return getRandomSideCreature(this.oppositeSide);
    }

    public BattlefieldCreature getRandomOppositeSideAliveCreature() {
        return getRandomOppositeSideCreature(ObjectStatus.ALIVE);
    }

    public BattlefieldCreature getRandomOppositeSideCreature(ObjectStatus... filters) {
        return getRandomSideCreature(this.oppositeSide, filters);
    }

    private BattlefieldCreature getRandomSideCreature(BattlefieldSide side, ObjectStatus... filters) {
        List<BattlefieldCreature> allSideCreatures = side.getCreatures()
                .stream().filter(creature -> creature.hasStatuses(filters)).collect(Collectors.toList());
        if (allSideCreatures.size() == 0) {
            return null;
        }
        return allSideCreatures.get(RandomController.randomInt(allSideCreatures.size()));
    }

    public List<BattlefieldCreature> getCreatures(ObjectStatus... filters) {
        return getCreatures().stream().filter(c -> c.hasStatuses(filters)).collect(Collectors.toList());
    }

    public BattlefieldCreature getCreatureWithHighest(Stat stat) {
        List<BattlefieldCreature> allCreatures = getCreatures();
        if (allCreatures.isEmpty()) {
            return null;
        }
        return allCreatures.stream().max(Comparator.comparingInt(o -> o.getStat(stat))).get();
    }

    public BattlefieldCreature getCreatureWithLowest(Stat stat) {
        List<BattlefieldCreature> allCreatures = getCreatures();
        if (allCreatures.isEmpty()) {
            return null;
        }
        return allCreatures.stream().min(Comparator.comparingInt(o -> o.getStat(stat))).get();
    }

    public void setOppositeSide(BattlefieldSide oppositeSide) {
        this.oppositeSide = oppositeSide;
    }

    public BattlefieldSide getOppositeSide() {
        return oppositeSide;
    }
}

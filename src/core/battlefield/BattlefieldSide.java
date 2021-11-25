package core.battlefield;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.controllers.utils.RandomController;
import core.player.Board;
import core.creature.Creature;

public class BattlefieldSide {

    private final Map<Position, List<BattlefieldCreature>> creatures;
    private BattlefieldSide oppositeSide;

    private BattlefieldSide() {
        this.creatures = new HashMap<>();
        for(Position position : Position.values()) {
            this.creatures.put(position, new ArrayList<>());
        }
    }

    private BattlefieldSide(Map<Position, List<BattlefieldCreature>> creatures) {
        this();
        for (Position position : creatures.keySet()) {
            this.creatures.put(position, creatures.get(position));
        }
        getAllCreatures().forEach(creature -> creature.setBattlefieldSide(this));
    }

    public static BattlefieldSide fromBoard(Board board) {
        Map<Position, List<BattlefieldCreature>> creatures = new HashMap<>();
        board.updateTraitBuffs();
        for (Position position : Position.values()) {
            List<Creature> creaturesOnPosition = board.getCreaturesOnPosition(position);
            List<BattlefieldCreature> line = new ArrayList<>();
            for (Creature creature : creaturesOnPosition) {
                BattlefieldCreature battlefieldCreature = new BattlefieldCreature(creature, position, ObjectStatus.ALIVE, ObjectStatus.byPosition(position));
                line.add(battlefieldCreature);
            }
            creatures.put(position, line);
        }
        BattlefieldSide battlefieldSide = new BattlefieldSide(creatures);
        for (BattlefieldCreature creature : battlefieldSide.getAllCreatures()) {
            creature.setBattlefieldSide(battlefieldSide);
        }
        return battlefieldSide;
    }

    public boolean hasAliveCreature() {
        return this.getAllCreatures().stream().anyMatch(creature -> creature.hasStatus(ObjectStatus.ALIVE));
    }

    public void addCreature(BattlefieldCreature creature, Position position) {
        this.creatures.get(position).add(creature);
    }

    public void moveCreature(BattlefieldCreature creature, Position position) {
        Position creaturesPosition = findCreaturePosition(creature);
        if(creaturesPosition != Position.UNDEFINED) {
            this.creatures.get(creaturesPosition).remove(creature);
            this.creatures.get(position).add(creature);
        }
    }

    public Position findCreaturePosition(BattlefieldCreature creature) {
        for (Position position : this.creatures.keySet()) {
            if (this.creatures.get(position).contains(creature)) {
                return position;
            }
        }
        return Position.UNDEFINED;
    }

    public List<BattlefieldCreature> getAllCreatures() {
        List<BattlefieldCreature> allCreatures = new ArrayList<>();
        this.creatures.values().forEach(allCreatures::addAll);
        return allCreatures;
    }

    public Map<Position, List<BattlefieldCreature>> getCreatures() {
        return creatures;
    }

    public List<BattlefieldCreature> getCreaturesOnPosition(Position position) {
        return creatures.get(position);
    }

    public List<BattlefieldCreature> getCreaturesOnPositions(Position... positions) {
        return getCreaturesOnPositions(Arrays.asList(positions));
    }

    public List<BattlefieldCreature> getCreaturesOnPositions(List<Position> positions) {
        List<BattlefieldCreature> creatures = new ArrayList<>();
        for (Position position : positions) {
            creatures.addAll(getCreaturesOnPosition(position));
        }
        return creatures;
    }

    public BattlefieldCreature getRandomSideCreature() {
        return getRandomSideCreature(this);
    }

    public BattlefieldCreature getRandomSideCreature(Position... positions) {
        return getRandomSideCreature(this, Arrays.asList(positions));
    }

    public BattlefieldCreature getRandomSideCreature(List<Position> positions) {
        return getRandomSideCreature(this, positions);
    }

    public BattlefieldCreature getRandomOppositeSideCreature() {
        return getRandomSideCreature(this.oppositeSide);
    }

    public BattlefieldCreature getRandomOppositeSideCreature(Position... positions) {
        return getRandomSideCreature(this.oppositeSide, Arrays.asList(positions));
    }

    public BattlefieldCreature getRandomOppositeSideAliveCreature() {
        return getRandomOppositeSideAliveCreature(Position.values());
    }

    public BattlefieldCreature getRandomOppositeSideAliveCreature(Position... positions) {
        return getRandomOppositeSideCreature(Arrays.asList(positions), ObjectStatus.ALIVE);
    }

    public BattlefieldCreature getRandomOppositeSideCreature(List<Position> positions, ObjectStatus... filters) {
        return getRandomSideCreature(this.oppositeSide, positions, filters);
    }

    private BattlefieldCreature getRandomSideCreature(BattlefieldSide side, ObjectStatus... filters) {
        return getRandomSideCreature(side, Arrays.asList(Position.values()), filters);
    }

    private BattlefieldCreature getRandomSideCreature(BattlefieldSide side, List<Position> positions, ObjectStatus... filters) {
        List<BattlefieldCreature> allSideCreatures = side.getCreaturesOnPositions(positions)
                .stream().filter(creature -> creature.hasStatuses(filters)).collect(Collectors.toList());
        if (allSideCreatures.size() == 0) {
            return null;
        }
        return allSideCreatures.get(RandomController.randomInt(allSideCreatures.size()));
    }

    public void setOppositeSide(BattlefieldSide oppositeSide) {
        this.oppositeSide = oppositeSide;
    }

    public BattlefieldSide getOppositeSide() {
        return oppositeSide;
    }
}

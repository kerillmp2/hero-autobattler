package core.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.battlefield.Position;
import core.creature.Creature;

public class Board {

    private final Map<Position, List<Creature>> creatures;

    public Board() {
        this.creatures = new HashMap<>();
        for (Position position : Position.values()) {
            this.creatures.put(position, new ArrayList<>());
        }
    }

    public Board(Map<Position, List<Creature>> creatures) {
        this();
        for (Position position : creatures.keySet()) {
           this.creatures.put(position, creatures.get(position));
        }
    }

    public void addCreature(Creature creature, Position position) {
        this.creatures.get(position).add(creature);
    }

    public void moveCreature(Creature creature, Position position) {
        Position creaturesPosition = findCreature(creature);
        if(creaturesPosition != Position.UNDEFINED) {
            this.creatures.get(creaturesPosition).remove(creature);
            this.creatures.get(position).add(creature);
        }
    }

    public Position findCreature(Creature creature) {
        for (Position position : this.creatures.keySet()) {
            if (this.creatures.get(position).contains(creature)) {
                return position;
            }
        }
        return Position.UNDEFINED;
    }

    public List<Creature> getAllCreatures() {
        List<Creature> allCreatures = new ArrayList<>();
        this.creatures.values().forEach(allCreatures::addAll);
        return allCreatures;
    }

    public Map<Position, List<Creature>> getCreatures() {
        return creatures;
    }

    public List<Creature> getCreaturesOnPosition(Position position) {
        return creatures.get(position);
    }
}

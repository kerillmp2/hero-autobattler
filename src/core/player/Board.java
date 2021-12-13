package core.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import core.battlefield.Position;
import core.controllers.TraitsController;
import core.creature.Creature;
import core.traits.Trait;
import core.traits.TraitContainer;

public class Board {

    private final Map<Position, List<Creature>> creatures;
    private final TraitsController traitsController = TraitsController.build();
    private int maxSize;

    public Board(int maxSize) {
        this.creatures = new HashMap<>();
        for (Position position : Position.values()) {
            this.creatures.put(position, new ArrayList<>());
        }
        this.maxSize = maxSize;
    }

    public void addCreature(Creature creature, Position position) {
        if (isCreatureUnique(creature)) {
            TraitContainer creatureTraits = creature.getTraitContainer();
            for (Trait trait : creatureTraits.getTags()) {
                traitsController.addTrait(trait);
            }
        }
        this.creatures.get(position).add(creature);
        updateTraitBuffs();
    }

    public void removeCreature(Creature creature) {
        removeCreature(creature, getCreaturePosition(creature));
    }

    public void removeCreature(Creature creature, Position position) {
        this.creatures.get(position).remove(creature);
        if (isCreatureUnique(creature)) {
            TraitContainer creatureTraits = creature.getTraitContainer();
            for(Trait trait : creatureTraits.getTags()) {
                traitsController.removeTrait(trait);
            }
        }
        updateTraitBuffs();
    }

    public Position getCreaturePosition(Creature creature) {
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

    public boolean isCreatureUnique(Creature creature) {
        return getAllCreatures().stream().noneMatch(c -> c.getName().equals(creature.getName()));
    }

    public Map<Position, List<Creature>> getCreatures() {
        return creatures;
    }

    public List<Creature> getCreaturesByTrait(Trait trait) {
        return getAllCreatures().stream().filter(creature -> creature.getTraitContainer().hasTag(trait)).collect(Collectors.toList());
    }

    public boolean hasCreature(Creature creature) {
        return getAllCreatures().contains(creature);
    }

    public List<Creature> getCreaturesOnPosition(Position position) {
        return creatures.get(position);
    }

    public void updateTraitBuffs() {
        traitsController.updateTraitBuffs(getAllCreatures());
    }

    public Map<Trait, Integer> getTraits() {
        Map<Trait, Integer> traits = new TreeMap<>();
        for(Trait trait : Trait.values()) {
            int traitNum = traitsController.getTraitValue(trait);
            if (traitNum > 0) {
                traits.put(trait, traitNum);
            }
        }
        return traits;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}

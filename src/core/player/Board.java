package core.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import core.controllers.TraitsController;
import core.creature.Creature;
import core.traits.Trait;
import core.traits.TraitContainer;

public class Board {

    private final List<Creature> creatures;
    private final TraitsController traitsController = TraitsController.build();
    private int maxSize;

    public Board(int maxSize) {
        this.creatures = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public void addCreature(Creature creature) {
        if (isCreatureUnique(creature)) {
            TraitContainer creatureTraits = creature.getTraitContainer();
            for (Trait trait : creatureTraits.getTags()) {
                traitsController.addTrait(trait);
            }
        }
        this.creatures.add(creature);
        updateTraitBuffs();
    }

    public void removeCreature(Creature creature) {
        this.creatures.remove(creature);
        if (isCreatureUnique(creature)) {
            TraitContainer creatureTraits = creature.getTraitContainer();
            for(Trait trait : creatureTraits.getTags()) {
                traitsController.removeTrait(trait);
            }
        }
        updateTraitBuffs();
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public boolean isCreatureUnique(Creature creature) {
        return getCreatures().stream().noneMatch(c -> c.getName().equals(creature.getName()));
    }

    public List<Creature> getCreaturesByTrait(Trait trait) {
        return getCreatures().stream().filter(creature -> creature.getTraitContainer().hasTag(trait)).collect(Collectors.toList());
    }

    public boolean hasCreature(Creature creature) {
        return getCreatures().contains(creature);
    }

    public void updateTraitBuffs() {
        traitsController.updateTraitBuffs(getCreatures());
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

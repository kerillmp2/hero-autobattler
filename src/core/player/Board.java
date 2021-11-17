package core.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import core.battlefield.Position;
import core.creature.Creature;
import core.creature.Stat;
import core.creature.StatChangeSource;
import core.traits.Trait;
import core.traits.TraitContainer;

public class Board {

    private final Map<Position, List<Creature>> creatures;
    private final TraitsController traitsController = TraitsController.build();

    public Board() {
        this.creatures = new HashMap<>();
        for (Position position : Position.values()) {
            this.creatures.put(position, new ArrayList<>());
        }
    }

    public void addCreature(Creature creature, Position position) {
        this.creatures.get(position).add(creature);
        TraitContainer creatureTraits = creature.getTraitContainer();
        for(Trait trait : creatureTraits.getTags()) {
            traitsController.addTrait(trait);
        }
        updateTraitBuffs();
    }

    public void removeCreature(Creature creature, Position position) {
        this.creatures.get(position).remove(creature);
        TraitContainer creatureTraits = creature.getTraitContainer();
        for(Trait trait : creatureTraits.getTags()) {
            traitsController.removeTrait(trait);
        }
        updateTraitBuffs();
    }

    public void moveCreature(Creature creature, Position position) {
        Position creaturesPosition = getCreaturePosition(creature);
        if(creaturesPosition != Position.UNDEFINED) {
            this.creatures.get(creaturesPosition).remove(creature);
            this.creatures.get(position).add(creature);
        }
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

    public Map<Position, List<Creature>> getCreatures() {
        return creatures;
    }

    public List<Creature> getCreaturesByTrait(Trait trait) {
        return getAllCreatures().stream().filter(creature -> creature.getTraitContainer().hasTag(trait)).collect(Collectors.toList());
    }

    public List<Creature> getCreaturesOnPosition(Position position) {
        return creatures.get(position);
    }

    public void updateTraitBuffs() {
        //Humans: (2, 3, 4). <+2 Attack, +3 Attack, +5 Attack> to all Humans.
        int humansNum = traitsController.getTraitValue(Trait.HUMAN);
        List<Creature> humans = getCreaturesByTrait(Trait.HUMAN);
        humans.forEach(human -> human.clearBuffsFromSource(StatChangeSource.HUMAN_TRAIT));
        if (humansNum == 2) {
            humans.forEach(human -> human.applyBuff(Stat.ATTACK, StatChangeSource.HUMAN_TRAIT, 2));
        }
        if (humansNum == 3) {
            humans.forEach(human -> human.applyBuff(Stat.ATTACK, StatChangeSource.HUMAN_TRAIT, 3));
        }
        if (humansNum >= 4) {
            humans.forEach(human -> human.applyBuff(Stat.ATTACK, StatChangeSource.HUMAN_TRAIT, 5));
        }
    }
}

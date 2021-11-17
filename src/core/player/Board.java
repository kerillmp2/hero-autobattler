package core.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import core.battlefield.Position;
import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.Stat;
import core.creature.StatChangeSource;
import core.traits.Trait;
import core.controllers.TraitContainer;

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
        if (isCreatureUnique(creature)) {
            TraitContainer creatureTraits = creature.getTraitContainer();
            for(Trait trait : creatureTraits.getTags()) {
                traitsController.addTrait(trait);
            }
        }
        this.creatures.get(position).add(creature);
        updateTraitBuffs();
    }

    public void removeCreature(Creature creature, Position position) {
        if (isCreatureUnique(creature)) {
            TraitContainer creatureTraits = creature.getTraitContainer();
            for(Trait trait : creatureTraits.getTags()) {
                traitsController.removeTrait(trait);
            }
        }
        this.creatures.get(position).remove(creature);
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

    public boolean isCreatureUnique(Creature creature) {
        return getAllCreatures().stream().noneMatch(c -> c.getName().equals(creature.getName()));
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
        List<Creature> allCreatures = getAllCreatures();

        //KING_GUARD: <+2 Attack, +3 Attack, +5 Attack> to all Creatures.
        int kingGuardNum = traitsController.getTraitValue(Trait.KING_GUARD);
        allCreatures.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.KING_GUARD_TRAIT));
        if (kingGuardNum == Trait.KING_GUARD.getLevels().get(0)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KING_GUARD_TRAIT, 2));
        }
        if (kingGuardNum == Trait.KING_GUARD.getLevels().get(1)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KING_GUARD_TRAIT, 3));
        }
        if (kingGuardNum >= Trait.KING_GUARD.getLevels().get(2)) {
            allCreatures.forEach(c -> c.applyBuff(Stat.ATTACK, StatChangeSource.KING_GUARD_TRAIT, 5));
        }

        //POISONOUS: Ядовитые получают [+1 / +2 / +3] яда при атаке
        int poisonousNum = traitsController.getTraitValue(Trait.POISONOUS);
        List<Creature> poisonousCreatures = getCreaturesByTrait(Trait.POISONOUS);
        poisonousCreatures.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.POISONOUS_TRAIT));
        if (poisonousNum == Trait.POISONOUS.getLevels().get(0)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 1));
        }
        if (poisonousNum == Trait.POISONOUS.getLevels().get(1)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 2));
        }
        if (poisonousNum == Trait.POISONOUS.getLevels().get(2)) {
            poisonousCreatures.forEach(c -> c.applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.POISONOUS_TRAIT, 3));
        }

        //EATER: Обжоры навсегда получают [+1 / +2 / +3] здоровья перед боем
        int eatersNum = traitsController.getTraitValue(Trait.EATER);
        List<Creature> eaters = getCreaturesByTrait(Trait.EATER);
        eaters.forEach(c -> c.clearAllChangesFromSource(StatChangeSource.EATERS_TRAIT));
        if (eatersNum == Trait.EATER.getLevels().get(0)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.EATER, StatChangeSource.EATERS_TRAIT, 1));
        }
        if (eatersNum == Trait.EATER.getLevels().get(1)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.EATER, StatChangeSource.EATERS_TRAIT, 2));
        }
        if (eatersNum == Trait.EATER.getLevels().get(2)) {
            eaters.forEach(c -> c.applyCreatureTagChange(CreatureTag.EATER, StatChangeSource.EATERS_TRAIT, 3));
        }
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
}

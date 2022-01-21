package core.player;

import java.util.HashMap;
import java.util.Map;

import core.controllers.LevelController;
import core.creature.Creature;

public class CreatureCounter {
    Map<String, Integer> counter;
    Map<String, Creature> initialCreatures;

    public static CreatureCounter empty() {
        return new CreatureCounter(new HashMap<>(), new HashMap<>());
    }

    private CreatureCounter(Map<String, Integer> counter, Map<String, Creature> initialCreatures) {
        this.counter = counter;
        this.initialCreatures = initialCreatures;
    }

    public int count(String name) {
        return counter.getOrDefault(name, 0);
    }

    public int count(Creature creature) {
        return count(creature.getName());
    }

    public void increment(Creature creature, boolean forAI) {
        int oldValue = count(creature);
        int newValue = oldValue + 1;
        if (!initialCreatures.containsKey(creature.getName())) {
            initialCreatures.put(creature.getName(), creature);
        }
        Creature initialCreature = initialCreatures.get(creature.getName());
        if (newValue > initialCreature.getLevel()) {
            initialCreature.setLevel(newValue);
            LevelController.levelUpCreature(initialCreature, newValue, forAI);
        }
        counter.put(creature.getName(), newValue);
    }

    public void clear(String name) {
        counter.put(name, 0);
        initialCreatures.remove(name);
    }
}

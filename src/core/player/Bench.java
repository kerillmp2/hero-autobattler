package core.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.creature.Creature;

public class Bench {

    private final List<Creature> creatures;
    private final int size;
    private int freeSpace;

    private Bench(List<Creature> creatures, int size, int freeSpace) {
        this.creatures = creatures;
        this.size = size;
        this.freeSpace = freeSpace;
    }

    public static Bench empty(int size) {
        ArrayList<Creature> creatures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            creatures.add(Creature.benchDummy());
        }
        return new Bench(creatures, size, size);
    }

    public Creature getCreatureOnPosition(int position) {
        return creatures.get(position);
    }

    public void addCreature(Creature creature) {
        for (int i = 0; i < creatures.size(); i++) {
            if (creatures.get(i).getName().equals("Пусто") || creatures.get(i).getName().equals("Empty")) {
                creatures.set(i, creature);
                freeSpace--;
                return;
            }
        }
    }

    public Creature removeCreature(int position) {
        Creature creature = creatures.get(position);
        if (!creature.getName().equals("Пусто") && !creature.getName().equals("Empty") ) {
            freeSpace++;
        }
        creatures.set(position, Creature.benchDummy());
        return creature;
    }

    public void removeCreature(Creature creature) {
        if (creature.getName().equals("Пусто") || creature.getName().equals("Empty") ) {
            return;
        }
        int index = creatures.indexOf(creature);
        if (index == -1) {
            return;
        }
        removeCreature(index);
    }

    public int positionOf(Creature creature) {
        for (int i = 0; i < creatures.size(); i++) {
            if (creature == creatures.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public int getFreeSpace() {
        return freeSpace;
    }

    public boolean hasFreeSpace() {
        return freeSpace > 0;
    }

    public int getSize() {
        return size;
    }

    public List<Creature> getCreatures() {
        return creatures.stream().filter(c -> (!c.getName().equals("Пусто") && (!c.getName().equals("Empty")))).collect(Collectors.toList());
    }

    public List<Creature> getCreaturesWithDummys() {
        return creatures;
    }

    public boolean hasCreature(Creature creature) {
        return creatures.contains(creature);
    }
}

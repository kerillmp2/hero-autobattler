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

    public boolean addCreature(Creature creature) {
        for (int i = 0; i < creatures.size(); i++) {
            if (creatures.get(i).getName().equals("Пусто")) {
                creatures.set(i, creature);
                freeSpace--;
                return true;
            }
        }
        return false;
    }

    public boolean addCreature(Creature creature, int position) {
        if (getCreatureOnPosition(position).getName().equals("Пусто")) {
            creatures.set(position, creature);
            freeSpace--;
            return true;
        }
        return false;
    }

    public Creature removeCreature(int position) {
        Creature creature = creatures.get(position);
        if (!creature.getName().equals("Пусто")) {
            freeSpace++;
        }
        creatures.set(position, Creature.benchDummy());
        return creature;
    }

    public boolean removeCreature(Creature creature) {
        if (creature.getName().equals("Пусто")) {
            return false;
        }
        int index = creatures.indexOf(creature);
        if (index == -1) {
            return false;
        }
        removeCreature(index);
        return true;
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

    public int getSize() {
        return size;
    }

    public List<Creature> getCreatures() {
        return creatures.stream().filter(c -> !c.getName().equals("Пусто")).collect(Collectors.toList());
    }

    public List<Creature> getCreaturesWithDummys() {
        return creatures;
    }

    public boolean hasCreature(Creature creature) {
        return creatures.contains(creature);
    }
}

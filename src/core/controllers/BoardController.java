package core.controllers;

import core.creature.Creature;
import core.player.Bench;
import core.player.Board;
import core.player.CreatureCounter;

public class BoardController {
    protected Board board;
    protected Bench bench;
    protected CreatureCounter creatureCounter;

    protected BoardController(Board board, Bench bench, CreatureCounter creatureCounter) {
        this.board = board;
        this.bench = bench;
        this.creatureCounter = creatureCounter;
    }

    public static BoardController empty(int boardMaxSize, int benchMaxSize) {
        return new BoardController(new Board(boardMaxSize), Bench.empty(benchMaxSize), CreatureCounter.empty());
    }

    public void removeCreatureFromBoard(Creature creature) {
        board.removeCreature(creature);
    }

    public void removeCreatureFromBench(Creature creature) {
        bench.removeCreature(creature);
    }

    public boolean canAddCreatureToBoard() {
        return board.getCreatures().size() + 1 <= board.getMaxSize();
    }

    public boolean canAddCreatureToBench() {
        return bench.hasFreeSpace();
    }

    public void addCreatureToBoard(Creature creature) {
        if (canAddCreatureToBoard()) {
            board.addCreature(creature);
        }
    }

    public void addCreatureToBench(Creature creature) {
        if (canAddCreatureToBench()) {
            bench.addCreature(creature);
        }
    }

    public boolean addCreature(Creature creature) {
        if (creatureCounter.count(creature) > 0) {
            creatureCounter.increment(creature, false);
            return true;
        }
        if (canAddCreatureToBoard()) {
            creatureCounter.increment(creature, false);
            addCreatureToBoard(creature);
            return true;
        }
        if (canAddCreatureToBench()) {
            creatureCounter.increment(creature, false);
            addCreatureToBench(creature);
            return true;
        }
        return false;
    }

    public void incrementBoardMaxSize() {
        board.setMaxSize(board.getMaxSize() + 1);
    }

    public Board getBoard() {
        return board;
    }

    public Bench getBench() {
        return bench;
    }

    public CreatureCounter getCreatureCounter() {
        return creatureCounter;
    }
}

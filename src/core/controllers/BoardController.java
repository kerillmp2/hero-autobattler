package core.controllers;

import core.battlefield.Position;
import core.creature.Creature;
import core.player.Bench;
import core.player.Board;
import core.player.CreatureCounter;

public class BoardController {
    private Board board;
    private Bench bench;
    private CreatureCounter creatureCounter;

    private BoardController(Board board, Bench bench, CreatureCounter creatureCounter) {
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
        return board.getAllCreatures().size() + 1 <= board.getMaxSize();
    }

    public boolean canAddCreatureToBench() {
        return bench.hasFreeSpace();
    }

    public void addCreatureToBoard(Creature creature) {
        if (canAddCreatureToBoard()) {
            board.addCreature(creature, Position.FIRST_LINE);
        }
    }

    public void addCreatureToBench(Creature creature) {
        if (canAddCreatureToBench()) {
            bench.addCreature(creature);
        }
    }

    public boolean addCreature(Creature creature) {
        if (creatureCounter.count(creature) > 0) {
            creatureCounter.increment(creature);
            return true;
        }
        if (canAddCreatureToBoard()) {
            creatureCounter.increment(creature);
            addCreatureToBoard(creature);
            return true;
        }
        if (canAddCreatureToBench()) {
            creatureCounter.increment(creature);
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

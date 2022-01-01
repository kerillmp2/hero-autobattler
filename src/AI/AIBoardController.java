package AI;

import core.controllers.BoardController;
import core.creature.Creature;
import core.player.Bench;
import core.player.Board;
import core.player.CreatureCounter;

public class AIBoardController extends BoardController {

    protected AIBoardController(Board board, Bench bench, CreatureCounter creatureCounter) {
        super(board, bench, creatureCounter);
    }

    public static AIBoardController empty(int boardMaxSize, int benchMaxSize) {
        return new AIBoardController(new Board(boardMaxSize), Bench.empty(benchMaxSize), CreatureCounter.empty());
    }

    @Override
    public boolean addCreature(Creature creature) {
        if (creatureCounter.count(creature) > 0) {
            creatureCounter.increment(creature, true);
            return true;
        }
        if (canAddCreatureToBoard()) {
            creatureCounter.increment(creature, true);
            addCreatureToBoard(creature);
            return true;
        }
        if (canAddCreatureToBench()) {
            creatureCounter.increment(creature, true);
            addCreatureToBench(creature);
            return true;
        }
        return false;
    }
}

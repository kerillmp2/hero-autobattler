package core.battlefield;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.controllers.BattleController;
import core.controllers.utils.RandomController;
import core.player.Board;

public class Battlefield {
    private BattleController battleController;
    private final BattlefieldSide firstSide;
    private final BattlefieldSide secondSide;

    private Battlefield(BattlefieldSide firstSide, BattlefieldSide secondSide) {
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        getAllCreatures().forEach(creature -> creature.setBattlefield(this));
    }

    public static Battlefield fromTwoBoards(Board firstBoard, Board secondBoard) {
        BattlefieldSide firstBattlefieldSide = BattlefieldSide.fromBoard(firstBoard);
        BattlefieldSide secondBattlefieldSide = BattlefieldSide.fromBoard(secondBoard);
        firstBattlefieldSide.setOppositeSide(secondBattlefieldSide);
        secondBattlefieldSide.setOppositeSide(firstBattlefieldSide);

        return new Battlefield(firstBattlefieldSide, secondBattlefieldSide);
    }

    public BattlefieldSide getFirstSide() {
        return firstSide;
    }

    public BattlefieldSide getSecondSide() {
        return secondSide;
    }

    public List<BattlefieldCreature> getAllCreatures() {
        List<BattlefieldCreature> firstSideCreatures = this.getFirstSide().getCreatures();
        List<BattlefieldCreature> secondSideCreatures = this.getSecondSide().getCreatures();
        return Stream.concat(firstSideCreatures.stream(), secondSideCreatures.stream()).collect(Collectors.toList());
    }

    public BattlefieldCreature getRandomFirstSideCreature() {
        return getRandomSideCreature(this.firstSide);
    }

    public BattlefieldCreature getRandomSecondSideCreature() {
        return getRandomSideCreature(this.secondSide);
    }

    private BattlefieldCreature getRandomSideCreature(BattlefieldSide side) {
        List<BattlefieldCreature> allSideCreatures = side.getCreatures();
        return allSideCreatures.get(RandomController.randomInt(allSideCreatures.size()));
    }

    public BattleController getBattleController() {
        return battleController;
    }

    public void setBattleController(BattleController battleController) {
        this.battleController = battleController;
    }
}

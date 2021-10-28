package core.battlefield;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.player.Board;

public class Battlefield {
    private final BattlefieldSide firstSide;
    private final BattlefieldSide secondSide;
    private final Random random;

    private Battlefield(BattlefieldSide firstSide, BattlefieldSide secondSide, Random random) {
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.random = random;
        getAllCreatures().forEach(creature -> creature.setBattlefield(this));
    }

    public static Battlefield fromTwoBoards(Board firstBoard, Board secondBoard) {
        BattlefieldSide firstBattlefieldSide = BattlefieldSide.fromBoard(firstBoard);
        BattlefieldSide secondBattlefieldSide = BattlefieldSide.fromBoard(secondBoard);
        firstBattlefieldSide.setOppositeSide(secondBattlefieldSide);
        secondBattlefieldSide.setOppositeSide(firstBattlefieldSide);
        return new Battlefield(firstBattlefieldSide, secondBattlefieldSide, new Random());
    }

    public BattlefieldSide getFirstSide() {
        return firstSide;
    }

    public BattlefieldSide getSecondSide() {
        return secondSide;
    }

    public List<BattlefieldCreature> getAllCreatures() {
        List<BattlefieldCreature> firstSideCreatures = this.getFirstSide().getAllCreatures();
        List<BattlefieldCreature> secondSideCreatures = this.getSecondSide().getAllCreatures();
        return Stream.concat(firstSideCreatures.stream(), secondSideCreatures.stream()).collect(Collectors.toList());
    }

    public BattlefieldCreature getRandomFirstSideCreature() {
        return getRandomSideCreature(this.firstSide);
    }

    public BattlefieldCreature getRandomFirstSideCreature(Position... positions) {
        return getRandomSideCreature(this.firstSide, Arrays.asList(positions));
    }

    public BattlefieldCreature getRandomFirstSideCreature(List<Position> positions) {
        return getRandomSideCreature(this.firstSide, positions);
    }

    public BattlefieldCreature getRandomSecondSideCreature() {
        return getRandomSideCreature(this.secondSide);
    }

    public BattlefieldCreature getRandomSecondSideCreature(Position... positions) {
        return getRandomSideCreature(this.secondSide, Arrays.asList(positions));
    }

    public BattlefieldCreature getRandomSecondSideCreature(List<Position> positions) {
        return getRandomSideCreature(this.secondSide, positions);
    }

    private BattlefieldCreature getRandomSideCreature(BattlefieldSide side) {
        return getRandomSideCreature(side, Arrays.asList(Position.values()));
    }

    private BattlefieldCreature getRandomSideCreature(BattlefieldSide side, List<Position> positions) {
        List<BattlefieldCreature> allSideCreatures = side.getCreaturesOnPositions(positions);
        return allSideCreatures.get(random.nextInt(allSideCreatures.size()));
    }
}

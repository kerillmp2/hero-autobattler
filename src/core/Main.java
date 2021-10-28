package core;

import core.player.Player;

public class Main {
    public static void main(String[] args) {
        Player kirill = Player.newPlayerWithName("Kirill");
        Player valera = Player.newPlayerWithName("Valera");

        GameController gameController = GameController.forPlayers(kirill, valera);
        gameController.startGame();

      /*  Creature warrior = new Creature("Warrior", 20, 5, 2, 2, 1, 1, 1, CreatureTag.HAVE_BASIC_ATTACK);
        Creature rogue = new Creature("Rogue", 7, 3, 1, 2, 2, 1, 1, CreatureTag.HAVE_BASIC_ATTACK);
        Creature warrior_2 = new Creature("Warrior_2", 20, 5, 1, 1, 1, 1, 1, CreatureTag.HAVE_BASIC_ATTACK);
        Creature rogue_2 = new Creature("Rogue_2", 7, 3, 1, 2, 2, 1, 1, CreatureTag.HAVE_BASIC_ATTACK);
        rogue.addTagValue(CreatureTag.POISONOUS, 3);
        rogue_2.addTagValue(CreatureTag.POISONOUS, 3);

        Map<Position, List<Creature>> firstSide = new HashMap<>();
        List<Creature> firstLine = new ArrayList<>();
        firstLine.add(warrior);
        firstSide.put(Position.FIRST_LINE, firstLine);
        List<Creature> firstBackLine = new ArrayList<>();
        firstBackLine.add(rogue);
        firstSide.put(Position.FIRST_LINE, firstLine);
        firstSide.put(Position.SECOND_LINE, firstBackLine);
        Board firstBoard = new Board(firstSide);

        Map<Position, List<Creature>> secondSide = new HashMap<>();
        List<Creature> secondLine = new ArrayList<>();
        secondLine.add(rogue_2);
        secondLine.add(warrior_2);
        secondSide.put(Position.FIRST_LINE, secondLine);
        Board secondBoard = new Board(secondSide);

        System.out.println("FirstBoardCreatures: " + firstBoard.getAllCreatures());
        System.out.println("SecondBoardCreatures: " + secondBoard.getAllCreatures());

        BattleController battleController = BattleController.forBattlefield(
                Battlefield.fromTwoBoards(firstBoard, secondBoard)
        );
        MessageController.print(battleController.battle().name());*/
    }
}

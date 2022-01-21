package core.player;

import java.util.ArrayList;
import java.util.List;

import core.controllers.BoardController;
import utils.Constants;
import utils.HasName;
import utils.Option;

public class Player implements HasName {
    private String name;
    private PlayerState state;
    private int money;
    private int hp;
    BoardController boardController;

    protected Player(String name, PlayerState state, int money, int hp, BoardController boardController) {
        this.name = name;
        this.state = state;
        this.money = money;
        this.hp = hp;
        this.boardController = boardController;
    }

    public static Player newPlayerWithName(String name) {
        int money = Constants.PLAYER_MONEY.value;
        return new Player(name, PlayerState.NOT_READY_FOR_BATTLE, 1000, Constants.PLAYER_HP.value, BoardController.empty(1, Constants.BENCH_SIZE.value));
    }

    public List<Option<TurnOption>> getTurnOptions() {
        List<Option<TurnOption>> options = new ArrayList<>();
        options.add(new Option<>(TurnOption.END_TURN, "End turn"));
        options.add(new Option<>(TurnOption.VIEW_BOARD, "Field"));
        options.add(new Option<>(TurnOption.OPEN_SHOP, "Shop [Level " + getShopLevel() + "]"));
        return options;
    }

    public boolean reduceMoney(int amount) {
        if (hasMoney(amount)) {
            money -= amount;
            return true;
        }
        return false;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public boolean hasMoney(int amount) {
        return money >= amount;
    }

    public Board getBoard() {
        return boardController.getBoard();
    }

    public Bench getBench() {
        return boardController.getBench();
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public int getMoney() {
        return money;
    }

    public PlayerState getState() {
        return state;
    }

    public void reduceHp(int amount) {
        this.hp -= amount;
        if (this.hp <= 0) {
            this.setState(PlayerState.DEAD);
        }
    }

    public int getHp() {
        return hp;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void incrementShopLevel() {
        this.boardController.incrementBoardMaxSize();
    }

    public int getShopLevel() {
        return this.boardController.getBoard().getMaxSize();
    }

    @Override
    public String getName() {
        return name;
    }
}

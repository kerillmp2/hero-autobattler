package core.player;

import java.util.ArrayList;
import java.util.List;

import utils.Constants;
import utils.HasName;
import utils.Option;

public class Player implements HasName {
    private String name;
    private PlayerState state;
    private int money;
    private int hp;
    private int creatureShopLevel;
    private Board board;
    private Bench bench;

    public Player(String name, PlayerState state, int money, int hp, int creatureShopLevel, Board board, Bench bench) {
        this.name = name;
        this.state = state;
        this.money = money;
        this.hp = hp;
        this.creatureShopLevel = creatureShopLevel;
        this.board = board;
        this.bench = bench;
    }

    public static Player newPlayerWithName(String name) {
        return new Player(name, PlayerState.NOT_READY_FOR_BATTLE, 100, 20, 1, new Board(), Bench.empty(Constants.BENCH_SIZE.value));
    }

    public List<Option<TurnOption>> getTurnOptions() {
        List<Option<TurnOption>> options = new ArrayList<>();
        options.add(new Option<>(TurnOption.END_TURN, "End turn"));
        options.add(new Option<>(TurnOption.VIEW_BOARD, "Field"));
        options.add(new Option<>(TurnOption.OPEN_SHOP, "Shop [Level " + creatureShopLevel + "]"));
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
        return board;
    }

    public Bench getBench() {
        return bench;
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

    public int getCreatureShopLevel() {
        return creatureShopLevel;
    }

    public void incrementShopLevel() {
        this.creatureShopLevel++;
    }

    @Override
    public String getName() {
        return name;
    }
}

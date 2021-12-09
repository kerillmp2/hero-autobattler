package core.player;

import utils.HasName;

public enum TurnOption implements HasName {
    UNDEFINED("UNDEFINED"),
    END_TURN("END_TURN"),
    OPEN_SHOP("OPEN_SHOP"),
    VIEW_BOARD("VIEW_BOARD"),
    SELL_CREATURE("SELL_CREATURE"),
    MOVE_FROM_BOARD("REMOVE_FROM_BOARD"),
    MOVE_TO_BOARD("MOVE_TO_BOARD"),
    DEFAULT("DEFAULT");

    private final String name;

    TurnOption(String name) {
        this.name = name;
    }

    public static TurnOption byName(String name) {
        for (TurnOption status : TurnOption.values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }
        return UNDEFINED;
    }

    @Override
    public String getName() {
        return name;
    }
}

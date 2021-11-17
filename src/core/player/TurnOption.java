package core.player;

import core.utils.HasName;

public enum TurnOption implements HasName {
    UNDEFINED("UNDEFINED"),
    END_TURN("END_TURN"),
    OPEN_SHOP("OPEN_SHOP"),
    VIEW_BOARD("VIEW_BOARD"),
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

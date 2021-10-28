package core.utils;

import core.player.TurnOption;

public class Option implements HasName {
    private String name;
    private TurnOption tag;

    public Option(TurnOption tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public TurnOption getTag() {
        return tag;
    }
}

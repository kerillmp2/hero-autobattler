package core.battlefield;

public enum Position {
    UNDEFINED("UNDEFINED", 0),
    FIRST_LINE("FIRST_LINE", 1),
    SECOND_LINE("SECOND_LINE", 2),
    THIRD_LINE("THIRD_LINE", 3),
    FOURTH_LINE("FOURTH_LINE", 4),
    AIR("AIR", 5),
    UNDERGROUND("UNDERGROUND", 6),
    NOT_ON_BATTLEFIELD("NOT_ON_BATTLEFIELD", 8);

    public String name;
    public String id;

    Position(String name, int id) {

    }
}

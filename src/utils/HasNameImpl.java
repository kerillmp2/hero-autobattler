package utils;

public class HasNameImpl implements HasName {
    private String name;

    public HasNameImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

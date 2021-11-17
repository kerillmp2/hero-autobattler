package core.utils;

public class Option<T extends HasName> implements HasName {
    private String name;
    private T tag;

    public Option(T tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public T getTag() {
        return tag;
    }
}

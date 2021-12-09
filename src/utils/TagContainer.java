package utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class TagContainer<T extends Tag> {
    protected Map<T, Integer> tagValues;

    public TagContainer() {
        tagValues = new TreeMap<>(Comparator.comparing(HasId::getId));
    }

    public TagContainer(Map<T, Integer> tagValues) {
        this.tagValues = tagValues;
    }

    public TagContainer(Iterable<T> tags) {
        tagValues = new TreeMap<>(Comparator.comparing(HasId::getId));
        for (T tag : tags) {
            tagValues.put(tag, 0);
        }
    }

    public void addTag(T tag) {
        tagValues.put(tag, 0);
    }

    public void addTagValue(T tag, Integer value) {
        if (!tagValues.containsKey(tag)) {
            tagValues.put(tag, value);
        } else {
            int oldValue = tagValues.get(tag);
            tagValues.put(tag, oldValue + value);
        }
    }

    public int getTagValue(T tag) {
        return tagValues.getOrDefault(tag, 0);
    }

    public boolean hasTag(T tag) {
        return tagValues.containsKey(tag);
    }

    public boolean hasTags(T... tags) {
        for(T tag : tags) {
            if (!this.hasTag(tag)) {
                return false;
            }
        }
        return true;
    }

    public int removeTag(T tag) {
        return tagValues.remove(tag);
    }

    public void setTagValue(T tag, int value) {
        tagValues.put(tag, value);
    }

    public void clearTagValue(T tag) {
        if (tagValues.containsKey(tag)) {
            tagValues.put(tag, 0);
        }
    }

    public Map<T, Integer> getTagValues() {
        return tagValues;
    }

    public Set<T> getTags() {
        return tagValues.keySet();
    }
}

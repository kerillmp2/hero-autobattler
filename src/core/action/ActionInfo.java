package core.action;

import java.util.Arrays;
import java.util.Map;

import core.battlefield.BattlefieldCreature;
import utils.TagContainer;

public class ActionInfo extends TagContainer<ActionTag> {
    public BattlefieldCreature performer;
    public BattlefieldCreature target;
    public ResolveTime resolveTime;

    public ActionInfo(BattlefieldCreature performer, BattlefieldCreature target, ResolveTime resolveTime) {
        super();
        this.performer = performer;
        this.target = target;
        this.resolveTime = resolveTime;
    }

    public static ActionInfo empty() {
        return new ActionInfo(null, null, ResolveTime.UNDEFINED);
    }

    public ActionInfo wrapTag(ActionTag tag) {
        addTag(tag);
        return this;
    }

    public ActionInfo wrapTag(ActionTag tag, Integer value) {
        addTagValue(tag, value);
        return this;
    }

    public ActionInfo overrideTag(ActionTag tag, Integer value) {
        tagValues.put(tag, value);
        return this;
    }

    public ActionInfo overrideTagMax(ActionTag tag, Integer value) {
        if (tagValues.containsKey(tag)) {
            tagValues.put(tag, Math.max(value, tagValues.get(tag)));
        } else {
            tagValues.put(tag, value);
        }
        return this;
    }

    public ActionInfo overrideTagMin(ActionTag tag, Integer value) {
        if (tagValues.containsKey(tag)) {
            tagValues.put(tag, Math.min(value, tagValues.get(tag)));
        } else {
            tagValues.put(tag, value);
        }
        return this;
    }

    public ActionInfo from(BattlefieldCreature performer) {
        this.performer = performer;
        return this;
    }

    public ActionInfo to(BattlefieldCreature target) {
        this.target = target;
        return this;
    }

    public ActionInfo withTime(ResolveTime resolveTime) {
        this.resolveTime = resolveTime;
        return this;
    }
}

package core.action;

import core.battlefield.BattlefieldCreature;
import core.creature.stat.Stat;
import utils.TagContainer;

public class ActionInfo extends TagContainer<ActionTag> {
    public BattlefieldCreature performer;
    public BattlefieldCreature target;
    public ResolveTime resolveTime;
    public String prefix = "";
    public String postfix = "";

    private ActionInfo(BattlefieldCreature performer, BattlefieldCreature target, ResolveTime resolveTime, String prefix, String postfix) {
        super();
        this.performer = performer;
        this.target = target;
        this.resolveTime = resolveTime;
        this.prefix = prefix;
        this.postfix = postfix;
    }

    public static ActionInfo empty() {
        return new ActionInfo(null, null, ResolveTime.UNDEFINED, "", "");
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

    public ActionInfo withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ActionInfo withPostfix(String postfix) {
        this.postfix = postfix;
        return this;
    }

    public Stat getStat() {
        for (Stat stat : Stat.values()) {
            if (stat != Stat.UNDEFINED) {
                if (hasTag(ActionTag.addStat(stat))) {
                    return stat;
                }
            }
        }
        return Stat.UNDEFINED;
    }
}

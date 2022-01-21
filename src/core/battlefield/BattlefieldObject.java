package core.battlefield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;

public class BattlefieldObject {
    private final Set<ObjectStatus> statusSet;
    private final Map<ResolveTime, List<Action>> actions;

    public BattlefieldObject(Set<ObjectStatus> statusSet, Map<ResolveTime, List<Action>> actions) {
        this.statusSet = statusSet;
        this.actions = actions;
        rebaseActions();
    }

    public BattlefieldObject(Set<ObjectStatus> statusSet) {
        this(statusSet, new HashMap<>());
    }

    private void rebaseActions() {
        for (ResolveTime resolveTime : ResolveTime.values()) {
            if (!actions.containsKey(resolveTime)) {
                actions.put(resolveTime, new ArrayList<>());
            }
        }
    }

    public void removeActionsByTags(ActionTag... tags) {
        for (ResolveTime resolveTime : this.actions.keySet()) {
            this.actions.get(resolveTime).removeIf(action -> action.getActionInfo().hasTags(tags));
        }
    }

    public void clearActionsByResolveTime(ResolveTime resolveTime) {
        this.actions.get(resolveTime).clear();
    }

    public boolean removeAction(Action action) {
        return this.actions.get(action.getActionInfo().resolveTime).remove(action);
    }

    public boolean addAction(Action action) {
        return this.actions.get(action.getActionInfo().resolveTime).add(action);
    }

    public List<Action> getActions(ResolveTime resolveTime) {
        return actions.get(resolveTime);
    }

    public Map<ResolveTime, List<Action>> getActions() {
        return actions;
    }

    public boolean addStatus(ObjectStatus status) {
        return this.statusSet.add(status);
    }

    public boolean hasStatus(ObjectStatus status) {
        return this.statusSet.contains(status);
    }

    public boolean hasStatus(String statusName) {
        return this.statusSet.contains(ObjectStatus.byName(statusName));
    }

    public boolean hasStatus(int statusId) {
        return this.statusSet.contains(ObjectStatus.byId(statusId));
    }

    public boolean hasStatuses(ObjectStatus... statuses) {
        for(ObjectStatus status : statuses) {
            if (!hasStatus(status)) {
                return false;
            }
        }
        return true;
    }

    public boolean removeStatus(ObjectStatus status) {
        return this.statusSet.remove(status);
    }

    public Set<ObjectStatus> getStatusSet() {
        return statusSet;
    }

    public Action getActionByTags(ActionTag... actionTags) {
        for (ResolveTime resolveTime : ResolveTime.values()) {
            for (Action action : getActions(resolveTime)) {
                if (action.getActionInfo().hasTags(actionTags)) {
                    return action;
                }
            }
        }
        return ActionFactory.undefinedAction();
    }

    public List<Action> getActionsByTags(ActionTag... actionTags) {
        List<Action> actions = new ArrayList<>();
        for (ResolveTime resolveTime : ResolveTime.values()) {
            for (Action action : getActions(resolveTime)) {
                if (action.getActionInfo().hasTags(actionTags)) {
                    actions.add(action);
                }
            }
        }
        return actions;
    }

    public boolean hasActionWithTags(ActionTag... actionTags) {
        Action action = getActionByTags(actionTags);
        return !action.getActionInfo().hasTag(ActionTag.UNDEFINED);
    }
}

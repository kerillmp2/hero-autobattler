package core.action;

import core.battlefield.BattlefieldCreature;

public class Action {
    protected final ActionInfo actionInfo;

    public Action(ActionInfo actionInfo) {
        this.actionInfo = actionInfo;
    }

    public Action withPerformer(BattlefieldCreature performer) {
        actionInfo.performer = performer;
        return this;
    }

    public ActionInfo getActionInfo() {
        return actionInfo;
    }

    public void addTag(ActionTag actionTag) {
        actionInfo.addTag(actionTag);
    }

    @Override
    public String toString() {
        return "Action{" +
                "actionInfo=" + actionInfo +
                '}';
    }
}

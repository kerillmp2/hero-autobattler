package core.action;

public class Action {
    protected final ActionInfo actionInfo;

    public Action(ActionInfo actionInfo) {
        this.actionInfo = actionInfo;
    }

    public ActionInfo getActionInfo() {
        return actionInfo;
    }

    public void addTag(ActionTag actionTag) {
        actionInfo.addTag(actionTag);
    }
}

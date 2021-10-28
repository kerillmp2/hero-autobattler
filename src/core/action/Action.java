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

    public void addTagValue(ActionTag tag, int value) {
        actionInfo.addTagValue(tag, value);
    }
}

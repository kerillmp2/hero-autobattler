package core.controllers;

import java.util.ArrayList;
import java.util.List;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;
import core.battlefield.BattlefieldCreature;
import core.battlefield.BattlefieldObjectTag;
import core.battlefield.ObjectStatus;
import core.battlefield.Position;
import core.utils.Calculator;
import core.utils.Constants;

public class ActionController {

    public static List<String> resolve(List<Action> actions) {
        List<String> messages = new ArrayList<>();
        List<Action> resolving = List.copyOf(actions);
        resolving.forEach(action -> {
            String actionMessage = resolve(action);
            if(!actionMessage.equals(Constants.UNDEFINED.name)) {
                messages.add(actionMessage);
            }
        });
        return messages;
    }

    public static String resolve(BattlefieldCreature creature, ResolveTime... resolveTimes) {
        StringBuilder message = new StringBuilder();
        for (ResolveTime resolveTime : resolveTimes) {
            List <String> messages = resolve(creature.getActions(resolveTime));
            if (!messages.isEmpty()) {
                message.append(String.join("\n", messages));
            }
        }
        return message.toString();
    }

    public static String resolve(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = action.getActionInfo().target;
        String message = "";

        if (!performer.hasStatus(ObjectStatus.ALIVE) || performer.hasStatus(ObjectStatus.DEAD)) {
            return message;
        }

        if (action.getActionInfo().hasTag(ActionTag.APPLY_POISON_DAMAGE)) {
            int targetHp = target.getCurrentHp();
            int poisonDamage = action.getActionInfo().getTagValue(ActionTag.APPLY_POISON_DAMAGE);
            int dealedDamage = Calculator.calculateMagicDamage(poisonDamage, target);
            target.setCurrentHp(targetHp - dealedDamage);
            message += target.getBattleName() + " получает " + dealedDamage + " урона от яда\n";
        }

        if (action.getActionInfo().hasTag(ActionTag.BASIC_ATTACK)) {
            message += resolveBasicAttackAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.TAKE_BASIC_DAMAGE)) {
            int targetHp = performer.getCurrentHp();
            int damage = action.getActionInfo().getTagValue(ActionTag.TAKE_BASIC_DAMAGE);
            performer.setCurrentHp(targetHp - damage);
            message += performer.getBattleName() + " получает " + damage + " урона" + "\n";
        }

        if (action.getActionInfo().hasTag(ActionTag.GENERATE_BASIC_ATTACK)) {
            BattlefieldCreature attackTarget = performer.getBattlefieldSide().getRandomOppositeSideAliveCreature(
                    Position.FIRST_LINE,
                    Position.SECOND_LINE,
                    Position.THIRD_LINE
            );
            performer.addAction(ActionFactory.attackAction(performer, attackTarget));
        }

        if (action.getActionInfo().hasTag(ActionTag.TURNS_LEFT)) {
            action.getActionInfo().wrapTag(ActionTag.TURNS_LEFT, -1);
            if (action.getActionInfo().getTagValue(ActionTag.TURNS_LEFT) <= 0) {
                action.addTag(ActionTag.DELETE_AFTER_RESOLVE);
            }
        }

        if (action.getActionInfo().hasTag(ActionTag.DELETE_AFTER_RESOLVE)) {
            performer.removeAction(action);
        }

        return message;
    }

    public static String resolveBasicAttackAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = action.getActionInfo().target;
        String message = Constants.UNDEFINED.name;

        message = resolve(performer, ResolveTime.BEFORE_DEALING_DAMAGE);

        Action takeDamageAction = ActionFactory.takeDamageAction(performer, target);
        target.addAction(takeDamageAction);
        message += performer.getBattleName() + " атакует " + target.getBattleName() + "\n";

        message += resolve(performer, ResolveTime.ON_DEALING_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.ON_TAKING_DAMAGE);
        if (performer.hasTag(BattlefieldObjectTag.POISONOUS)) {
            int poisonDamage = performer.getTagValue(BattlefieldObjectTag.POISONOUS);
            int turnsLeft = 2;
            if (!target.hasActionWithTags(ActionTag.APPLY_POISON_DAMAGE)) {
                target.addAction(ActionFactory.applyPoisonDamageAction(performer, target, poisonDamage, turnsLeft));
            } else {
                target.getActionByTags(ActionTag.APPLY_POISON_DAMAGE).getActionInfo()
                        .wrapTag(ActionTag.APPLY_POISON_DAMAGE, poisonDamage)
                        .overrideTagMax(ActionTag.TURNS_LEFT, turnsLeft);
            }
            message += performer.getBattleName() + " отравляет " + target.getBattleName() + " на " + poisonDamage + "\n";
            message += target.getBattleName() + " имеет "
                    + target.getActionByTags(ActionTag.APPLY_POISON_DAMAGE).getActionInfo().getTagValue(ActionTag.APPLY_POISON_DAMAGE)
                    + " отравления на "
                    + target.getActionByTags(ActionTag.APPLY_POISON_DAMAGE).getActionInfo().getTagValue(ActionTag.TURNS_LEFT)
                    + " ходов\n";
        }
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE);
        message += resolve(performer, ResolveTime.AFTER_DEALING_DAMAGE);

        return message;
    }
}

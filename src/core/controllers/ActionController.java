package core.controllers;

import java.util.ArrayList;
import java.util.List;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;
import core.battlefield.BattlefieldCreature;
import core.battlefield.ObjectStatus;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import utils.Calculator;
import utils.Constants;

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
        String message = "";

        if (!performer.hasStatus(ObjectStatus.ALIVE) || performer.hasStatus(ObjectStatus.DEAD)) {
            performer.getBattlefield().getBattleController().getTurnController().removeCreatureFromTurnOrder(performer);
            return message;
        }

        if (action.getActionInfo().hasTag(ActionTag.CHOOSE_MAIN_ACTION)) {
            if (performer.hasEnoughManaForSkill()) {
                resolveGenerateUseSkillAction(action);
            } else {
                if (performer.hasTag(CreatureTag.HAVE_BASIC_ATTACK)) {
                    resolveGenerateBasicAttackAction(action);
                }
            }
        }

        if (action.getActionInfo().hasTag(ActionTag.USE_SKILL)) {
            message += resolveUseSkillAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.BASIC_ATTACK)) {
            message += resolveBasicAttackAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.ADD_STAT)) {
            message += resolveAddStatAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.ADD_MANA)) {
            message += resolveGetManaAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.HEAL)) {
            message += resolveHealingAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.APPLY_POISON_DAMAGE)) {
            message += resolveApplyPoissonDamageAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.DEAL_DAMAGE_TO_ALL_ENEMIES)) {
            message += resolveDealDamageToAllEnemies(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.TAKE_BASIC_DAMAGE)) {
            message += resolveTakeBasicDamageAction(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.TURNS_LEFT)) {
            resolveTurnsCountdown(action);
        }

        if (action.getActionInfo().hasTag(ActionTag.DELETE_AFTER_RESOLVE)) {
            performer.removeAction(action);
        }

        return message;
    }

    private static String resolveAddStatAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        String message = "";
        if (action.getActionInfo().hasTag(ActionTag.ATTACK)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.ATTACK);
            performer.applyBuff(Stat.ATTACK, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " attack! [" + performer.getStat(Stat.ATTACK) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPEED)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.SPEED);
            performer.applyBuff(Stat.SPEED, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " speed! [" + performer.getStat(Stat.SPEED) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.PHYSICAL_ARMOR)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.PHYSICAL_ARMOR);
            performer.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " physical armor! [" + performer.getStat(Stat.PHYSICAL_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.MAGIC_ARMOR)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.MAGIC_ARMOR);
            performer.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " magic armor! [" + performer.getStat(Stat.MAGIC_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPELL_POWER)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.SPELL_POWER);
            performer.applyBuff(Stat.SPELL_POWER, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " spell power! [" + performer.getStat(Stat.SPELL_POWER) + "]\n";
        }
        return message;
    }

    private static String resolveDealDamageToAllEnemies(Action action) {
        List<BattlefieldCreature> targets = action.getActionInfo().performer.getBattlefieldSide().getOppositeSide().getCreatures(ObjectStatus.ALIVE);
        BattlefieldCreature performer = action.getActionInfo().performer;
        int damage = action.getActionInfo().getTagValue(ActionTag.DEAL_DAMAGE_TO_ALL_ENEMIES);
        StringBuilder message = new StringBuilder();
        for (BattlefieldCreature target : targets) {
            message.append(resolve(performer, ResolveTime.BEFORE_DEALING_DAMAGE));
            message.append(resolve(performer, ResolveTime.BEFORE_DEALING_MAGIC_DAMAGE));
            Action takeDamageAction = ActionFactory.takeMagicDamageAction(target, damage);
            target.addAction(takeDamageAction);
            message.append(resolve(performer, ResolveTime.ON_DEALING_DAMAGE));
            message.append(resolve(performer, ResolveTime.ON_DEALING_MAGIC_DAMAGE));
            message.append(resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE));
            message.append(resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE));
            message.append(resolve(performer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE));
        }
        return message.toString();
    }

    private static String resolveHealingAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().performer;
        int amount = action.getActionInfo().getTagValue(ActionTag.HEAL);
        int currentHP = target.getCurrentHp();
        target.setCurrentHp(currentHP + amount);
        return target.getBattleName() + " restores " + amount + " HP! "
                + "[" + target.getCurrentHp() + "/" + target.getMaxHp() + "]\n";
    }

    private static String resolveGetManaAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().performer;
        int amount = action.getActionInfo().getTagValue(ActionTag.ADD_MANA);
        target.addMana(amount);
        return target.getBattleName() + " gains " + amount + " mana! "
                + "[" + target.getCurrentMana() + "/" + target.getMaxMana() + "]\n";
    }

    private static String resolveUseSkillAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        List<BattlefieldCreature> creatures = performer.getBattlefieldSide().getCreatures();
        StringBuilder message = new StringBuilder();
        for (BattlefieldCreature creature : creatures) {
            message.append(resolve(creature, ResolveTime.BEFORE_ALLY_USING_SKILL));
        }
        message.append(resolve(performer, ResolveTime.BEFORE_USING_SKILL));
        message.append(performer.useSkill());
        message.append(resolve(performer, ResolveTime.AFTER_USING_SKILL));
        for (BattlefieldCreature creature : creatures) {
            message.append(resolve(creature, ResolveTime.AFTER_ALLY_USING_SKILL));
        }
        return message.toString();
    }

    private static void resolveTurnsCountdown(Action action) {
        action.getActionInfo().wrapTag(ActionTag.TURNS_LEFT, -1);
        if (action.getActionInfo().getTagValue(ActionTag.TURNS_LEFT) <= 0) {
            action.addTag(ActionTag.DELETE_AFTER_RESOLVE);
        }
    }

    private static void resolveGenerateUseSkillAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        performer.addAction(ActionFactory.useSkillAction(performer));
    }

    private static void resolveGenerateBasicAttackAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;

        BattlefieldCreature attackTarget = performer.getBattlefieldSide().getRandomOppositeSideAliveCreature();
        performer.addAction(ActionFactory.attackAction(performer, attackTarget));
    }

    private static String resolveTakeBasicDamageAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;

        int targetHp = performer.getCurrentHp();
        int damage = action.getActionInfo().getTagValue(ActionTag.TAKE_BASIC_DAMAGE);
        performer.setCurrentHp(targetHp - damage);
        return performer.getBattleName() + " takes " + damage + " damage\n";
    }

    private static String resolveApplyPoissonDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;

        int targetHp = target.getCurrentHp();
        int poisonDamage = action.getActionInfo().getTagValue(ActionTag.APPLY_POISON_DAMAGE);
        int dealedDamage = Calculator.calculateMagicDamage(poisonDamage, target);
        target.setCurrentHp(targetHp - dealedDamage);
        return target.getBattleName() + " takes " + dealedDamage + " damage from poison\n";
    }

    private static String resolveBasicAttackAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = action.getActionInfo().target;
        String message;

        message = resolve(performer, ResolveTime.BEFORE_ATTACK);
        message += resolve(performer, ResolveTime.BEFORE_DEALING_PHYSICAL_DAMAGE, ResolveTime.BEFORE_DEALING_DAMAGE);

        Action takeDamageAction = ActionFactory.takeBasicAtackDamageAction(performer, target);
        target.addAction(takeDamageAction);
        message += performer.getBattleName() + " attacks " + target.getBattleName() + "\n";

        message += resolve(performer, ResolveTime.ON_DEALING_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.ON_TAKING_DAMAGE);
        if (performer.hasTag(CreatureTag.POISONOUS)) {
            int poisonDamage = performer.getTagValue(CreatureTag.POISONOUS);
            int turnsLeft = 2;
            if (!target.hasActionWithTags(ActionTag.APPLY_POISON_DAMAGE)) {
                target.addAction(ActionFactory.applyPoisonDamageAction(performer, target, poisonDamage, turnsLeft));
            } else {
                target.getActionByTags(ActionTag.APPLY_POISON_DAMAGE).getActionInfo()
                        .wrapTag(ActionTag.APPLY_POISON_DAMAGE, poisonDamage)
                        .overrideTagMax(ActionTag.TURNS_LEFT, turnsLeft);
            }
            message += performer.getBattleName() + " poisons " + target.getBattleName() + " by " + poisonDamage + "\n";
            message += target.getBattleName() + " has "
                    + target.getActionByTags(ActionTag.APPLY_POISON_DAMAGE).getActionInfo().getTagValue(ActionTag.APPLY_POISON_DAMAGE)
                    + " poison on "
                    + target.getActionByTags(ActionTag.APPLY_POISON_DAMAGE).getActionInfo().getTagValue(ActionTag.TURNS_LEFT)
                    + " turns\n";
        }
        message += resolve(performer, ResolveTime.AFTER_ATTACK);
        message += resolve(target, ResolveTime.AFTER_TAKING_PHYSICAL_DAMAGE, ResolveTime.AFTER_TAKING_DAMAGE);
        message += resolve(performer, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE, ResolveTime.AFTER_DEALING_DAMAGE);

        return message;
    }
}

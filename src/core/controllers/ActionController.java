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

import static core.action.ActionTag.*;

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
        BattlefieldCreature target;
        String message = "";

        if (!performer.hasStatus(ObjectStatus.ALIVE) || performer.hasStatus(ObjectStatus.DEAD)) {
            performer.getBattlefield().getBattleController().getTurnController().removeCreatureFromTurnOrder(performer);
            return message;
        }

        if (action.getActionInfo().hasTag(CHOOSE_MAIN_ACTION)) {
            if (performer.hasEnoughManaForSkill()) {
                resolveGenerateUseSkillAction(action);
            } else {
                if (performer.hasTag(CreatureTag.HAVE_BASIC_ATTACK)) {
                    resolveGenerateBasicAttackAction(action);
                }
            }
        }

        if (action.getActionInfo().hasTag(USE_SKILL)) {
            message += resolveUseSkillAction(action);
        }

        if (action.getActionInfo().hasTag(BASIC_ATTACK)) {
            message += resolveBasicAttackAction(action);
        }

        if (action.getActionInfo().hasTag(ADD_FLOAT_STAT)) {
            message += resolveAddFloatStatAction(action);
        }

        if (action.getActionInfo().hasTag(ADD_PERCENTAGE_STAT)) {
            message += resolveAddPercentageStatAction(action);
        }

        if (action.getActionInfo().hasTag(ADD_MANA)) {
            message += resolveGetManaAction(action);
        }

        if (action.getActionInfo().hasTag(HEAL_FLOAT)) {
            message += resolveFloatHealingAction(action);
        }

        if (action.getActionInfo().hasTag(HEAL_PERCENT_OF_MAX)) {
            message += resolvePercentHealingAction(action, true);
        }

        if (action.getActionInfo().hasTag(HEAL_PERCENT_OF_MISSING)) {
            message += resolvePercentHealingAction(action, false);
        }

        if (action.getActionInfo().hasTag(APPLY_POISON_DAMAGE)) {
            message += resolveApplyPoissonDamageAction(action);
        }

        if (action.getActionInfo().hasTag(APPLY_BURN_DAMAGE)) {
            message += resolveApplyBurnDamageAction(action);
        }

        if (action.getActionInfo().hasTag(DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY)) {
            message += resolveDealPhysicalDamageToRandomEnemy(action);
        }

        if (action.getActionInfo().hasTag(DEAL_DAMAGE_TO_ALL_ENEMIES)) {
            message += resolveDealDamageToAllEnemies(action);
        }

        if (action.getActionInfo().hasTag(TAKE_BASIC_DAMAGE)) {
            message += resolveTakeBasicDamageAction(action);
        }

        if (action.getActionInfo().hasTag(TURNS_LEFT)) {
            resolveTurnsCountdown(action);
        }

        if (action.getActionInfo().hasTag(DELETE_AFTER_RESOLVE)) {
            performer.removeAction(action);
        }

        performer = action.getActionInfo().performer;
        target = action.getActionInfo().target;

        if (!action.getActionInfo().prefix.equals("")) {
            if (action.getActionInfo().target != null) {
                message = String.format(action.getActionInfo().prefix, performer.getBattleName(), target.getBattleName()) + "\n" + message;
            } else {
                message += String.format(action.getActionInfo().prefix, performer.getBattleName()) + "\n" + message;
            }
        }

        if (!action.getActionInfo().postfix.equals("")) {
            message += "\n" + action.getActionInfo().postfix;
        }

        return message;
    }

    private static String resolveAddFloatStatAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        String message = "";
        if (action.getActionInfo().hasTag(ActionTag.ATTACK)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.ATTACK);
            performer.applyBuff(Stat.ATTACK, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.ATTACK.getName() + "! [" + performer.getStat(Stat.ATTACK) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPEED)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.SPEED);
            performer.applyBuff(Stat.SPEED, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.SPEED.getName() + "! [" + performer.getStat(Stat.SPEED) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.PHYSICAL_ARMOR)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.PHYSICAL_ARMOR);
            performer.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.PHYSICAL_ARMOR.getName() + "! ["+ performer.getStat(Stat.PHYSICAL_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.MAGIC_ARMOR)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.MAGIC_ARMOR);
            performer.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.MAGIC_ARMOR.getName() + "! [" + performer.getStat(Stat.MAGIC_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPELL_POWER)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.SPELL_POWER);
            performer.applyBuff(Stat.SPELL_POWER, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.SPELL_POWER.getName() + "! ["+ performer.getStat(Stat.SPELL_POWER) + "]\n";
        }
        return message;
    }

    private static String resolveAddPercentageStatAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        String message = "";
        if (action.getActionInfo().hasTag(ActionTag.ATTACK)) {
            int percentage = action.getActionInfo().getTagValue(ActionTag.ATTACK);
            int amount = (int) (performer.getStat(Stat.ATTACK) / 100.0 * percentage);
            performer.applyBuff(Stat.ATTACK, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.ATTACK.getName() + "! [" + performer.getStat(Stat.ATTACK) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPEED)) {
            int percentage = action.getActionInfo().getTagValue(SPEED);
            int amount = (int) (performer.getStat(Stat.SPEED) / 100.0 * percentage);
            performer.applyBuff(Stat.SPEED, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.SPEED.getName() + "! [" + performer.getStat(Stat.SPEED) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.PHYSICAL_ARMOR)) {
            int percentage = action.getActionInfo().getTagValue(PHYSICAL_ARMOR);
            int amount = (int) (performer.getStat(Stat.PHYSICAL_ARMOR) / 100.0 * percentage);
            performer.applyBuff(Stat.PHYSICAL_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.PHYSICAL_ARMOR.getName() + "! [" + performer.getStat(Stat.PHYSICAL_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.MAGIC_ARMOR)) {
            int percentage = action.getActionInfo().getTagValue(MAGIC_ARMOR);
            int amount = (int) (performer.getStat(Stat.MAGIC_ARMOR) / 100.0 * percentage);
            performer.applyBuff(Stat.MAGIC_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.MAGIC_ARMOR.getName() + "! [" + performer.getStat(Stat.MAGIC_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPELL_POWER)) {
            int percentage = action.getActionInfo().getTagValue(SPELL_POWER);
            int amount = (int) (performer.getStat(Stat.SPELL_POWER) / 100.0 * percentage);
            performer.applyBuff(Stat.SPELL_POWER, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " gains " + amount + " " + Stat.SPELL_POWER.getName() + "! [" + performer.getStat(Stat.SPELL_POWER) + "]\n";
        }

        return message;
    }

    private static String resolveDealPhysicalDamageToRandomEnemy(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = performer.getBattlefieldSide().getRandomOppositeSideAliveCreature();
        String message = "";
        if (target != null) {
            action.getActionInfo().target = target;
            message += resolve(performer, ResolveTime.BEFORE_DEALING_PHYSICAL_DAMAGE, ResolveTime.BEFORE_DEALING_DAMAGE);
            Action takeDamageAction;

            if (action.getActionInfo().hasTag(PERCENTAGE)) {
                double coeff = (action.getActionInfo().getTagValue(ActionTag.DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY)) / 100.0;
                takeDamageAction = ActionFactory.takePhysicalDamageAction(target, (int) (performer.getCurrentAttack() * coeff));
            } else {
                int amount = action.getActionInfo().getTagValue(DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY);
                takeDamageAction = ActionFactory.takeTrueDamageAction(target, amount);
            }

            target.addAction(takeDamageAction);

            message += resolve(performer, ResolveTime.ON_DEALING_DAMAGE);
            message += resolve(target, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.ON_TAKING_DAMAGE);
            message += resolve(target, ResolveTime.AFTER_TAKING_PHYSICAL_DAMAGE, ResolveTime.AFTER_TAKING_DAMAGE);
            message += resolve(performer, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE, ResolveTime.AFTER_DEALING_DAMAGE);
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

    private static String resolveFloatHealingAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().performer;
        int amount = action.getActionInfo().getTagValue(HEAL_FLOAT);
        int currentHP = target.getCurrentHp();
        target.setCurrentHp(currentHP + amount);
        return target.getBattleName() + " restores " + amount + " HP! "
                + "[" + target.getCurrentHp() + "/" + target.getMaxHp() + "]\n";
    }

    private static String resolvePercentHealingAction(Action action, boolean fromMax) {
        BattlefieldCreature target = action.getActionInfo().performer;
        int percent;
        int amount;
        int currentHP = target.getCurrentHp();
        int maxHp = target.getMaxHp();
        if (fromMax) {
            percent = action.getActionInfo().getTagValue(HEAL_PERCENT_OF_MAX);
            amount = (int) (((double) maxHp / 100.0) * percent);
        } else {
            percent =  action.getActionInfo().getTagValue(HEAL_PERCENT_OF_MISSING);
            amount = (int) ((((double) maxHp - currentHP) / 100.0) * percent);
        }
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

    private static String resolveApplyBurnDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;

        int targetHp = target.getCurrentHp();
        int burnDamage = action.getActionInfo().getTagValue(APPLY_BURN_DAMAGE);
        int dealedDamage = Calculator.calculateMagicDamage(burnDamage, target);
        target.setCurrentHp(targetHp - dealedDamage);
        return target.getBattleName() + " takes " + dealedDamage + " damage from burn\n";
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

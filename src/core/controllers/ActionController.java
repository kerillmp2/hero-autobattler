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
                message += resolveUseSkillAction(ActionFactory.useSkillAction(performer));
            } else {
                if (performer.hasTag(CreatureTag.HAVE_BASIC_ATTACK)) {
                    message += resolveBasicAttackAction(ActionFactory.attackAction(performer));
                } else {
                    message += performer.getBattleName() + " skips turn!";
                }
            }
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

        if (action.getActionInfo().hasTag(REDUCE_FLOAT_STAT)) {
            message += resolveReduceFloatStatAction(action);
        }

        if (action.getActionInfo().hasTag(REDUCE_PERCENTAGE_STAT)) {
            message += resolveReducePercentageStatAction(action);
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

        if (action.getActionInfo().hasTag(TAKE_PHYSICAL_DAMAGE)) {
            message += resolveTakePhysicalDamageAction(action);
        }

        if (action.getActionInfo().hasTag(TAKE_MAGIC_DAMAGE)) {
            message += resolveTakeMagicDamageAction(action);
        }

        if (action.getActionInfo().hasTag(DEAL_MAGIC_DAMAGE)) {
            message += resolveDealMagicDamageAction(action);
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
        if (action.getActionInfo().hasTag(MANA)) {
            int add = action.getActionInfo().getTagValue(MANA);
            performer.addMana(add);
            message += performer.getBattleName() + " gains " + add + " " + Stat.MANA.getName() + "! [" + performer.getCurrentMana() + "/" + performer.getMaxMana() + "]\n";
        }
        return message;
    }

    private static String resolveReduceFloatStatAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;
        String message = "";
        if (action.getActionInfo().hasTag(ActionTag.ATTACK)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.ATTACK);
            target.applyDebuff(Stat.ATTACK, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += target.getBattleName() + " loses " + amount + " " + Stat.ATTACK.getName() + "! [" + target.getStat(Stat.ATTACK) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPEED)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.SPEED);
            target.applyDebuff(Stat.SPEED, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += target.getBattleName() + " loses " + amount + " " + Stat.SPEED.getName() + "! [" + target.getStat(Stat.SPEED) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.PHYSICAL_ARMOR)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.PHYSICAL_ARMOR);
            target.applyDebuff(Stat.PHYSICAL_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += target.getBattleName() + " loses " + amount + " " + Stat.PHYSICAL_ARMOR.getName() + "! ["+ target.getStat(Stat.PHYSICAL_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.MAGIC_ARMOR)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.MAGIC_ARMOR);
            target.applyDebuff(Stat.MAGIC_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += target.getBattleName() + " loses " + amount + " " + Stat.MAGIC_ARMOR.getName() + "! [" + target.getStat(Stat.MAGIC_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPELL_POWER)) {
            int amount = action.getActionInfo().getTagValue(ActionTag.SPELL_POWER);
            target.applyDebuff(Stat.SPELL_POWER, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += target.getBattleName() + " loses " + amount + " " + Stat.SPELL_POWER.getName() + "! ["+ target.getStat(Stat.SPELL_POWER) + "]\n";
        }
        if (action.getActionInfo().hasTag(MANA)) {
            int reduce = action.getActionInfo().getTagValue(MANA);
            int newValue = Math.max(0, target.getCurrentMana() - reduce);
            target.setCurrentMana(newValue);
            message += target.getBattleName() + " loses " + reduce + " " + Stat.MANA.getName() + "! [" + target.getCurrentMana() + "/" + target.getMaxMana() + "]\n";
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
        if (action.getActionInfo().hasTag(MANA)) {
            int percentage = action.getActionInfo().getTagValue(MANA);
            int add = (int) (performer.getMaxMana() / 100.0 * percentage);
            performer.addMana(add);
            message += performer.getBattleName() + " gains " + add + " " + Stat.MANA.getName() + "! [" + performer.getCurrentMana() + "/" + performer.getMaxMana() + "]\n";
        }
        return message;
    }

    private static String resolveReducePercentageStatAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().target;
        String message = "";
        if (action.getActionInfo().hasTag(ActionTag.ATTACK)) {
            int percentage = action.getActionInfo().getTagValue(ActionTag.ATTACK);
            int amount = (int) (performer.getStat(Stat.ATTACK) / 100.0 * percentage);
            performer.applyDebuff(Stat.ATTACK, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " loses " + amount + " " + Stat.ATTACK.getName() + "! [" + performer.getStat(Stat.ATTACK) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPEED)) {
            int percentage = action.getActionInfo().getTagValue(SPEED);
            int amount = (int) (performer.getStat(Stat.SPEED) / 100.0 * percentage);
            performer.applyDebuff(Stat.SPEED, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " loses " + amount + " " + Stat.SPEED.getName() + "! [" + performer.getStat(Stat.SPEED) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.PHYSICAL_ARMOR)) {
            int percentage = action.getActionInfo().getTagValue(PHYSICAL_ARMOR);
            int amount = (int) (performer.getStat(Stat.PHYSICAL_ARMOR) / 100.0 * percentage);
            performer.applyDebuff(Stat.PHYSICAL_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " loses " + amount + " " + Stat.PHYSICAL_ARMOR.getName() + "! [" + performer.getStat(Stat.PHYSICAL_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.MAGIC_ARMOR)) {
            int percentage = action.getActionInfo().getTagValue(MAGIC_ARMOR);
            int amount = (int) (performer.getStat(Stat.MAGIC_ARMOR) / 100.0 * percentage);
            performer.applyDebuff(Stat.MAGIC_ARMOR, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " loses " + amount + " " + Stat.MAGIC_ARMOR.getName() + "! [" + performer.getStat(Stat.MAGIC_ARMOR) + "]\n";
        }
        if (action.getActionInfo().hasTag(ActionTag.SPELL_POWER)) {
            int percentage = action.getActionInfo().getTagValue(SPELL_POWER);
            int amount = (int) (performer.getStat(Stat.SPELL_POWER) / 100.0 * percentage);
            performer.applyDebuff(Stat.SPELL_POWER, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += performer.getBattleName() + " loses " + amount + " " + Stat.MANA.getName() + "! [" + performer.getStat(Stat.SPELL_POWER) + "]\n";
        }
        if (action.getActionInfo().hasTag(MANA)) {
            int percentage = action.getActionInfo().getTagValue(MANA);
            int lost = (int) (performer.getMaxMana() / 100.0 * percentage);
            int newValue = Math.max(0, performer.getCurrentMana() - lost);
            performer.setCurrentMana(newValue);
            message += performer.getBattleName() + " loses " + lost + " " + Stat.SPELL_POWER.getName() + "! [" + performer.getCurrentMana() + "/" + performer.getMaxMana() + "]\n";
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
                takeDamageAction = ActionFactory.takePhysicalDamageAction(target, amount);
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

    private static String resolveTakePhysicalDamageAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature dealer = action.getActionInfo().target;

        int amount = action.getActionInfo().getTagValue(TAKE_PHYSICAL_DAMAGE);
        int damage = performer.takePhysicalDamage(amount, dealer.getCreature().getName());
        return performer.getBattleName() + " takes " + damage + " damage\n";
    }

    private static String resolveTakeMagicDamageAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature dealer = action.getActionInfo().target;

        int amount = action.getActionInfo().getTagValue(TAKE_MAGIC_DAMAGE);
        int damage = performer.takeMagicDamage(amount, dealer.getCreature().getName());
        return performer.getBattleName() + " takes " + damage + " damage\n";
    }

    private static String resolveDealMagicDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;

        int amount = action.getActionInfo().getTagValue(DEAL_MAGIC_DAMAGE);
        int damage = target.takeMagicDamage(amount, target.getCreature().getName());
        return target.getBattleName() + " takes " + damage + " damage\n";
    }

    private static String resolveApplyPoissonDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;

        int poisonDamage = action.getActionInfo().getTagValue(ActionTag.APPLY_POISON_DAMAGE);
        int dealedDamage = target.takeMagicDamage(poisonDamage, "Poison");
        return target.getBattleName() + " takes " + dealedDamage + " damage from poison\n";
    }

    private static String resolveApplyBurnDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;

        int burnDamage = action.getActionInfo().getTagValue(APPLY_BURN_DAMAGE);
        int dealedDamage = target.takeMagicDamage(burnDamage, "Burn");
        return target.getBattleName() + " takes " + dealedDamage + " damage from burn\n";
    }

    private static String resolveBasicAttackAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = performer.getBattlefieldSide().getRandomOppositeSideCreature(ObjectStatus.ALIVE);
        action.getActionInfo().target = target;
        String message;

        List<Action> updateActions = performer.getActionsByTags(BASIC_ATTACK_BUFF);
        for (Action updateAction : updateActions) {
            updateAction.getActionInfo().target = target;
        }

        message = resolve(performer, ResolveTime.BEFORE_ATTACK);
        message += resolve(performer, ResolveTime.BEFORE_DEALING_PHYSICAL_DAMAGE, ResolveTime.BEFORE_DEALING_DAMAGE);

        Action takeDamageAction = ActionFactory.takeBasicAttackDamageAction(performer, target);
        target.addAction(takeDamageAction);
        message += performer.getBattleName() + " attacks " + target.getBattleName() + "\n";

        message += resolve(performer, ResolveTime.ON_DEALING_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.ON_TAKING_DAMAGE);
        if (performer.getCreature().getTagValue(CreatureTag.POISONOUS) > 0) {
            int poisonDamage = performer.getCreature().getTagValue(CreatureTag.POISONOUS);
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

package core.controllers;

import java.util.ArrayList;
import java.util.List;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;
import core.battlefield.BattlefieldCreature;
import core.battlefield.ObjectStatus;
import core.controllers.utils.RandomController;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import statistics.Metric;
import statistics.StatisticCollector;
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
        StringBuilder message = new StringBuilder();

        if (!performer.hasStatus(ObjectStatus.ALIVE) || performer.hasStatus(ObjectStatus.DEAD)) {
            performer.getBattlefield().getBattleController().getTurnController().removeCreatureFromTurnOrder(performer);
            return message.toString();
        }

        if (!performer.getBattlefieldSide().hasAliveCreature()) {
            return message.toString();
        }

        if (!performer.getBattlefieldSide().getOppositeSide().hasAliveCreature()) {
            return message.toString();
        }

        if (action.getActionInfo().hasTag(CHANCE)) {
            int chance = action.getActionInfo().getTagValue(CHANCE);
            int random = RandomController.randomInt(1, 100, true);
            if (chance < random) {
                return "";
            }
        }

        if (action.getActionInfo().hasTag(CHOOSE_MAIN_ACTION)) {
            if (performer.hasEnoughManaForSkill()) {
                message.append(resolveUseSkillAction(ActionFactory.useSkillAction(performer)));
                int bouncingTimes = performer.getTagValue(CreatureTag.BOUNCING_SKILL);
                for (int i = 0; i < bouncingTimes; i++) {
                    if (performer.getBattlefieldSide().getOppositeSide().hasAliveCreature()) {
                        message.append(resolveUseBouncingSkillAction(ActionFactory.useBouncingSkillAction(performer)));
                    } else {
                        return message.toString();
                    }
                }
            } else {
                if (performer.hasTag(CreatureTag.HAVE_BASIC_ATTACK)) {
                    message.append(resolveBasicAttackAction(ActionFactory.attackAction(performer)));
                } else {
                    message.append(performer.getBattleName()).append(" skips turn!");
                }
            }
        }

        if (action.getActionInfo().hasTag(BASIC_ATTACK)) {
            message.append(resolveBasicAttackAction(action));
        }

        if (action.getActionInfo().hasTag(ADD_FLOAT_STAT)) {
            message.append(resolveAddFloatStatAction(action));
        }

        if (action.getActionInfo().hasTag(ADD_PERCENTAGE_STAT)) {
            message.append(resolveAddPercentageStatAction(action));
        }

        if (action.getActionInfo().hasTag(REDUCE_FLOAT_STAT)) {
            message.append(resolveReduceFloatStatAction(action));
        }

        if (action.getActionInfo().hasTag(REDUCE_PERCENTAGE_STAT)) {
            message.append(resolveReducePercentageStatAction(action));
        }

        if (action.getActionInfo().hasTag(HEAL_FLOAT)) {
            message.append(resolveFloatHealingAction(action));
        }

        if (action.getActionInfo().hasTag(HEAL_PERCENT_OF_MAX)) {
            message.append(resolvePercentHealingAction(action, true));
        }

        if (action.getActionInfo().hasTag(HEAL_PERCENT_OF_MISSING)) {
            message.append(resolvePercentHealingAction(action, false));
        }

        if (action.getActionInfo().hasTag(APPLY_POISON_DAMAGE)) {
            message.append(resolveApplyPoissonDamageAction(action));
        }

        if (action.getActionInfo().hasTag(APPLY_BURN_DAMAGE)) {
            message.append(resolveApplyBurnDamageAction(action));
        }

        if (action.getActionInfo().hasTag(DEAL_PHYSICAL_DAMAGE_TO_RANDOM_ENEMY)) {
            message.append(resolveDealPhysicalDamageToRandomEnemy(action));
        }

        if (action.getActionInfo().hasTag(DEAL_MAGIC_DAMAGE_TO_ALL_ENEMIES)) {
            message.append(resolveDealMagicDamageToAllEnemies(action));
        }

        if (action.getActionInfo().hasTag(TAKE_PHYSICAL_DAMAGE)) {
            message.append(resolveTakePhysicalDamageAction(action));
        }

        if (action.getActionInfo().hasTag(TAKE_MAGIC_DAMAGE)) {
            message.append(resolveTakeMagicDamageAction(action));
        }

        if (action.getActionInfo().hasTag(DEAL_MAGIC_DAMAGE)) {
            message.append(resolveDealMagicDamageAction(action));
        }

        if (action.getActionInfo().hasTag(DEAL_PHYSICAL_DAMAGE)) {
            message.append(resolveDealPhysicalDamageAction(action));
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
                message.insert(0, String.format(action.getActionInfo().prefix, performer.getBattleName(), target.getBattleName()) + "\n");
            } else {
                message.append(String.format(action.getActionInfo().prefix, performer.getBattleName())).append("\n").append(message);
            }
        }

        if (!action.getActionInfo().postfix.equals("")) {
            message.append("\n").append(action.getActionInfo().postfix);
        }

        return message.toString();
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
            message += resolveDealPhysicalDamageAction(action);
        }
        return message;
    }

    private static String resolveDealMagicDamageToAllEnemies(Action action) {
        List<BattlefieldCreature> targets = action.getActionInfo().performer.getBattlefieldSide().getOppositeSide().getCreatures(ObjectStatus.ALIVE);
        BattlefieldCreature performer = action.getActionInfo().performer;
        int damage = action.getActionInfo().getTagValue(ActionTag.DEAL_MAGIC_DAMAGE_TO_ALL_ENEMIES);
        StringBuilder message = new StringBuilder();
        for (BattlefieldCreature target : targets) {
            message.append(resolveDealMagicDamageAction(ActionFactory.ActionBuilder.empty().from(performer).to(target).wrapTag(DEAL_MAGIC_DAMAGE, damage).build()));
        }
        return message.toString();
    }

    private static String resolveFloatHealingAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = action.getActionInfo().target;
        if (target == null) {
            target = performer;
        }
        int amount = action.getActionInfo().getTagValue(HEAL_FLOAT);
        int currentHP = target.getCurrentHp();
        target.setCurrentHp(currentHP + amount);
        if (Constants.COLLECT_STATISTIC.value != 0) {
            StatisticCollector.updateCreatureMetric(performer.getCreature().getName(), Metric.HP_HEALED, amount);
        }
        return performer.getBattleName() + " heals " + target.getBattleName() + " for " + amount + " HP! "
                + "[" + target.getCurrentHp() + "/" + target.getMaxHp() + "]\n";
    }

    private static String resolvePercentHealingAction(Action action, boolean fromMax) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = action.getActionInfo().target;
        if (target == null) {
            target = performer;
        }
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
        if (Constants.COLLECT_STATISTIC.value != 0) {
            StatisticCollector.updateCreatureMetric(performer.getCreature().getName(), Metric.HP_HEALED, amount);
        }
        target.setCurrentHp(currentHP + amount);
        return performer.getBattleName() + " heals " + target.getBattleName() + " for " + amount + " HP! "
                + "[" + target.getCurrentHp() + "/" + target.getMaxHp() + "]\n";
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

    private static String resolveUseBouncingSkillAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        List<BattlefieldCreature> creatures = performer.getBattlefieldSide().getCreatures();
        StringBuilder message = new StringBuilder();
        for (BattlefieldCreature creature : creatures) {
            message.append(resolve(creature, ResolveTime.BEFORE_ALLY_USING_SKILL));
        }
        message.append(resolve(performer, ResolveTime.BEFORE_USING_SKILL));
        message.append(performer.useBouncingSkill());
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
        String message = "";

        int amount = action.getActionInfo().getTagValue(TAKE_PHYSICAL_DAMAGE);
        message += resolve(dealer, ResolveTime.BEFORE_DEALING_DAMAGE, ResolveTime.BEFORE_DEALING_PHYSICAL_DAMAGE);
        message += resolve(performer, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE);
        message += performer.takePhysicalDamage(amount, dealer.getCreature().getName());
        message += resolve(performer, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE);
        message += resolve(dealer, ResolveTime.ON_DEALING_DAMAGE, ResolveTime.ON_DEALING_MAGIC_DAMAGE);
        message += resolve(performer, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
        message += resolve(dealer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE);
        return message;
    }

    private static String resolveTakeMagicDamageAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature dealer = action.getActionInfo().target;
        String message = "";

        int amount = action.getActionInfo().getTagValue(TAKE_MAGIC_DAMAGE);
        message += resolve(dealer, ResolveTime.BEFORE_DEALING_DAMAGE, ResolveTime.BEFORE_DEALING_MAGIC_DAMAGE);
        message += resolve(performer, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE);
        message += performer.takeMagicDamage(amount, dealer.getCreature().getName());
        message += resolve(performer, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE);
        message += resolve(dealer, ResolveTime.ON_DEALING_DAMAGE, ResolveTime.ON_DEALING_MAGIC_DAMAGE);
        message += resolve(performer, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
        message += resolve(dealer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE);
        return message;
    }

    private static String resolveDealMagicDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;
        BattlefieldCreature performer = action.getActionInfo().performer;
        String message = "";
        int amount;
        if (action.getActionInfo().hasTag(BASED_ON_STAT)) {
            int percent = action.getActionInfo().getTagValue(BASED_ON_STAT);
            amount = (int) (performer.getStat(action.getActionInfo().getStat()) / 100.0 * percent);
        } else {
            amount = action.getActionInfo().getTagValue(DEAL_MAGIC_DAMAGE);
        }
        message += resolve(performer, ResolveTime.BEFORE_DEALING_DAMAGE, ResolveTime.BEFORE_DEALING_MAGIC_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE);
        message += target.takeMagicDamage(amount, performer.getCreature().getName());
        message += resolve(target, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE);
        message += resolve(performer, ResolveTime.ON_DEALING_DAMAGE, ResolveTime.ON_DEALING_MAGIC_DAMAGE);
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
        message += resolve(performer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE);
        return message;
    }

    private static String resolveDealPhysicalDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;
        BattlefieldCreature performer = action.getActionInfo().performer;
        String message = "";
        int amount;
        if (action.getActionInfo().hasTag(BASED_ON_STAT)) {
            int percent = action.getActionInfo().getTagValue(BASED_ON_STAT);
            amount = (int) (performer.getStat(action.getActionInfo().getStat()) / 100.0 * percent);
        } else {
            amount = action.getActionInfo().getTagValue(DEAL_PHYSICAL_DAMAGE);
        }
        message += resolve(performer, ResolveTime.BEFORE_DEALING_DAMAGE, ResolveTime.BEFORE_DEALING_PHYSICAL_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE);
        message += target.takePhysicalDamage(amount, performer.getCreature().getName());
        message += resolve(target, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_PHYSICAL_DAMAGE);
        message += resolve(performer, ResolveTime.ON_DEALING_DAMAGE, ResolveTime.ON_DEALING_PHYSICAL_DAMAGE);
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_PHYSICAL_DAMAGE);
        message += resolve(performer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE);
        return message;
    }

    private static String resolveApplyPoissonDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;
        String message = resolve(target, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE, ResolveTime.ON_TAKING_DAMAGE);
        int poisonDamage = action.getActionInfo().getTagValue(ActionTag.APPLY_POISON_DAMAGE);
        message += target.takeMagicDamage(poisonDamage, "Poison");
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
        return message;
    }

    private static String resolveApplyBurnDamageAction(Action action) {
        BattlefieldCreature target = action.getActionInfo().target;
        String message = resolve(target, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE, ResolveTime.ON_TAKING_DAMAGE);
        int burnDamage = action.getActionInfo().getTagValue(APPLY_BURN_DAMAGE);
        message += target.takeMagicDamage(burnDamage, "Burn");
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
        return message;
    }

    private static String resolveBasicAttackAction(Action action) {
        BattlefieldCreature performer = action.getActionInfo().performer;
        BattlefieldCreature target = performer.getBattlefieldSide().getRandomOppositeSideCreature(ObjectStatus.ALIVE);
        action.getActionInfo().target = target;
        String message;

        if (target.hasActionWithTags(PARRY)) {
            Action parryAction = target.getActionByTags(PARRY);
            parryAction.getActionInfo().target = performer;
            return resolve(parryAction);
        }

        List<Action> updatePerformerActions = performer.getActionsByTags(BASIC_ATTACK_BUFF);
        for (Action updateAction : updatePerformerActions) {
            updateAction.getActionInfo().target = target;
        }

        List<Action> updateTargetActions = target.getActionsByTags(BASIC_ATTACK_RESPONSE);
        for (Action updateAction : updateTargetActions) {
            updateAction.getActionInfo().target = performer;
        }

        message = resolve(performer, ResolveTime.BEFORE_ATTACK);

        message += performer.getBattleName() + " attacks " + target.getBattleName() + "\n";
        resolveTakePhysicalDamageAction(ActionFactory.takeBasicAttackDamageAction(performer, target));

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

        message += resolve(target, ResolveTime.AFTER_ATTACKED);
        message += resolve(performer, ResolveTime.AFTER_ATTACK);

        return message;
    }
}

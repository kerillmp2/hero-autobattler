package core.creature.skills;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ResolveTime;
import core.battlefield.BattlefieldCreature;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;

import static core.controllers.ActionController.resolve;

public class CreatureSkillFactory {

    public static CreatureSkill emptySkill() {
        return (battleController, user) -> user.getBattleName() + " casts skill!\n";
    }

    public static CreatureSkill dunkanSkill() {
        return (battleController, dunkan) -> {
            int damage = dunkan.getCurrentAttack() / 2 + dunkan.getCurrentPhysicalArmor() * 2 + dunkan.getCurrentSpellPower() + dunkan.getCreature().getLevel() * 2;
            BattlefieldCreature target = dunkan.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;

            message = resolve(dunkan, ResolveTime.BEFORE_DEALING_DAMAGE);
            Action takeDamageAction = ActionFactory.takePhysicalDamageAction(target, damage);
            target.addAction(takeDamageAction);
            message += dunkan.getBattleName() + " casts \"Shield slam\" on " + target.getBattleName() + "!\n";
            message += resolve(dunkan, ResolveTime.ON_DEALING_DAMAGE);
            message += resolve(dunkan, ResolveTime.ON_DEALING_PHYSICAL_DAMAGE);
            message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_PHYSICAL_DAMAGE);
            message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE);
            message += resolve(dunkan, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE);
            return message;
        };
    }

    public static CreatureSkill salviraSkill() {
        return (battleController, salvira) -> {
            String message;
            BattlefieldCreature target = salvira.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            int amount = salvira.getCurrentSpellPower() + salvira.getCreature().getLevel();
            message = salvira.getBattleName() + " casts \"Poison daggers\" and gains +" + amount + " poison!\n";
            salvira.getCreature().applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += resolve(ActionFactory.attackAction(salvira, target));

            return message;
        };
    }

    public static CreatureSkill ignarSkill() {
        return (battleController, ignar) -> {
            String message;
            int healingAmount = ignar.getCreature().getHp() / 6 + ignar.getCurrentSpellPower() * 4 + ignar.getCreature().getLevel() * 3;
            message = ignar.getBattleName() + " casts \"Tasty meal\"!\n";
            message += resolve(ActionFactory.healingAction(ignar, healingAmount));

            return message;
        };
    }

    public static CreatureSkill warbotSkill() {
        return (battleController, warbot) -> {
          String message;
          int buffAmount = warbot.getCurrentSpellPower() + warbot.getCreature().getLevel() * 2;
          message = warbot.getBattleName() + " casts \"Cold steel\"!\n";
          message += warbot.getBattleName() + " gains " + buffAmount + " attack!\n";
          warbot.setCurrentAttack(warbot.getCurrentAttack() + buffAmount);
          return message;
        };
    }

    public static CreatureSkill kodjiSkill() {
        return (battleController, kodji) -> {
            BattlefieldCreature target = kodji.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;

            int damage = kodji.getCurrentSpellPower() * 2 + kodji.getCreature().getLevel() * 3;
            int slow = kodji.getCurrentSpellPower() + kodji.getCreature().getLevel() * 2;
            message = kodji.getBattleName() + " casts \"Frost bolt\" on " + target.getBattleName() + "\n";
            message += resolve(kodji, ResolveTime.BEFORE_DEALING_DAMAGE);
            message += resolve(kodji, ResolveTime.BEFORE_DEALING_MAGIC_DAMAGE);
            Action takeDamageAction = ActionFactory.takeMagicDamageAction(target, damage);
            target.addAction(takeDamageAction);
            message += resolve(kodji, ResolveTime.ON_DEALING_DAMAGE);
            message += resolve(kodji, ResolveTime.ON_DEALING_MAGIC_DAMAGE);
            message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE);
            message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
            message += resolve(kodji, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE);
            target.setCurrentSpeed(target.getCurrentSpeed() - slow);
            message += target.getBattleName() + " slows by " + slow + "\n";
            battleController.getTurnController().regenerateTurnOrder();
            return message;
        };
    }

    public static CreatureSkill miraSkill() {
        return (battleController, mira) -> {
            BattlefieldCreature target = mira.getBattlefieldSide().getCreatureWithLowest(Stat.HP);
            String message;

            int missingHealth = target.getMaxHp() - target.getCurrentHp();
            int healingAmount = 2 * mira.getCreature().getLevel() + 2 * mira.getStat(Stat.SPELL_POWER) + missingHealth / 4;
            message = mira.getBattleName() + " casts \"Healing potion\" on " + target.getBattleName() + "\n";
            message += resolve(ActionFactory.healingAction(target, healingAmount));

            return message;
        };
    }
}

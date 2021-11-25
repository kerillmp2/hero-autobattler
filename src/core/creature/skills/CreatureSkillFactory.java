package core.creature.skills;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ResolveTime;
import core.battlefield.BattlefieldCreature;
import core.creature.CreatureTag;
import core.creature.StatChangeSource;

import static core.controllers.ActionController.resolve;

public class CreatureSkillFactory {

    public static CreatureSkill emptySkill() {
        return (battleController, user) -> user.getBattleName() + " использует способность!\n";
    }

    public static CreatureSkill dunkanSkill() {
        return (battleController, dunkan) -> {
            int damage = dunkan.getCurrentAttack() + dunkan.getCurrentPhysicalArmor() + dunkan.getCurrentSpellPower();
            BattlefieldCreature target = dunkan.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;

            message = resolve(dunkan, ResolveTime.BEFORE_DEALING_DAMAGE);
            Action takeDamageAction = ActionFactory.takePhysicalDamageAction(target, damage);
            target.addAction(takeDamageAction);
            message += dunkan.getBattleName() + " использует \"удар щитом\" на " + target.getBattleName() + "!\n";
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
            int amount = salvira.getCurrentSpellPower();
            message = salvira.getBattleName() + " использует \"ядовитые клинки\" и получает +" + amount + " яда при атаке!\n";
            salvira.getCreature().applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += resolve(ActionFactory.attackAction(salvira, target));

            return message;
        };
    }

    public static CreatureSkill ignarSkill() {
        return (battleController, ignar) -> {
            String message;
            int healingAmount = ignar.getCreature().getHp() / 4 + ignar.getCurrentSpellPower();
            message = ignar.getBattleName() + " использует \"сытный обед\"!\n";
            message += resolve(ActionFactory.healingAction(ignar, healingAmount));

            return message;
        };
    }

    public static CreatureSkill warbotSkill() {
        return (battleController, warbot) -> {
          String message;
          int buffAmount = warbot.getCurrentSpellPower();
          message = warbot.getBattleName() + " использует навык \"холодная сталь\"!\n";
          message += warbot.getBattleName() + " получает " + buffAmount + " атаки!\n";
          warbot.setCurrentAttack(warbot.getCurrentAttack() + buffAmount);
          return message;
        };
    }

    public static CreatureSkill kodjiSkill() {
        return (battleController, kodji) -> {
            BattlefieldCreature target = kodji.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;

            int damage = kodji.getCurrentSpellPower() * 2;
            int slow = kodji.getCurrentSpellPower() / 2;
            message = kodji.getBattleName() + " использует навык \"ледяная стрела\" на " + target.getBattleName() + "\n";
            message += resolve(kodji, ResolveTime.BEFORE_DEALING_DAMAGE);
            message += resolve(kodji, ResolveTime.BEFORE_DEALING_MAGIC_DAMAGE);
            Action takeDamageAction = ActionFactory.takeMagicDamageAction(target, damage);
            target.addAction(takeDamageAction);
            message += resolve(kodji, ResolveTime.ON_DEALING_DAMAGE);
            message += resolve(kodji, ResolveTime.ON_DEALING_MAGIC_DAMAGE);
            message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE);
            message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
            message += resolve(kodji, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE);
            target.setCurrentSpeed(target.getCurrentSpeed() + slow);
            message += target.getBattleName() + " замедляется на " + slow + "\n";
            battleController.getTurnController().regenerateTurnOrder();
            return message;
        };
    }
}

package core.creature.skills;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;
import core.battlefield.BattlefieldCreature;
import core.battlefield.ObjectStatus;
import core.creature.Creature;
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
            message = dunkan.getBattleName() + " casts \"Shield slam\" on " + target.getBattleName() + "!\n";
            message += dealPhysicalDamage(dunkan, target, damage);
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
            int level = ignar.getCreature().getLevel();
            int coeff = level >= 9 ? 5 : level >= 6 ? 7 : level >= 3 ? 10 : 12;
            int healingAmount = ignar.getCreature().getHp() / coeff + ignar.getCurrentSpellPower() * 4;
            message = ignar.getBattleName() + " casts \"Tasty meal\"!\n";
            message += resolve(ActionFactory.healingAction(ignar, healingAmount));
            return message;
        };
    }

    public static CreatureSkill warbotSkill() {
        return (battleController, warbot) -> {
          String message;
          BattlefieldCreature target = warbot.getBattlefieldSide().getRandomOppositeSideAliveCreature();
          int buffAmount = warbot.getCurrentSpellPower() + warbot.getCreature().getLevel();
          message = warbot.getBattleName() + " casts \"Cold steel\"!\n";
          message += resolve(ActionFactory.addStatAction(warbot, Stat.ATTACK, buffAmount, ResolveTime.UNDEFINED));
          message += resolve(ActionFactory.attackAction(warbot, target));
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
            message += dealMagicDamage(kodji, target, damage);
            target.applyDebuff(Stat.SPEED, StatChangeSource.UNTIL_BATTLE_END, slow);
            message += target.getBattleName() + " slows by " + slow + "! [" + target.getCurrentSpeed() + "]\n";
            battleController.getTurnController().regenerateTurnOrder();
            return message;
        };
    }

    public static CreatureSkill miraSkill() {
        return (battleController, mira) -> {
            BattlefieldCreature target = mira.getBattlefieldSide().getCreatureWithLowest(Stat.HP);
            String message;

            int missingHealth = target.getMaxHp() - target.getCurrentHp();
            int healingAmount = 4 * mira.getStat(Stat.SPELL_POWER) + missingHealth / (9 - mira.getCreature().getLevel());
            message = mira.getBattleName() + " casts \"Healing potion\" on " + target.getBattleName() + "\n";
            message += resolve(ActionFactory.healingAction(target, healingAmount));

            return message;
        };
    }

    public static CreatureSkill obbySkill() {
        return (battleController, obby) -> {
            String message;
            BattlefieldCreature target = obby.getBattlefieldSide().getRandomOppositeSideAliveCreature();

            int damage = obby.getCurrentSpellPower() * 2 + obby.getCreature().getLevel() * 3 + obby.getCurrentAttack() / 2;
            int burn = obby.getCurrentSpellPower() * 2 + obby.getCreature().getLevel() * 2;

            message = obby.getBattleName() + " casts \"Fire spit\" on " + target.getBattleName() + "\n";
            message += dealMagicDamage(obby, target, damage);

            if (!target.hasActionWithTags(ActionTag.APPLY_BURN_DAMAGE)) {
                target.addAction(ActionFactory.ActionBuilder.empty().to(target).from(target).wrapTag(ActionTag.APPLY_BURN_DAMAGE, burn).wrapTag(ActionTag.DELETE_AFTER_RESOLVE).withTime(ResolveTime.ON_START_TURN).build());
            } else {
                target.getActionByTags(ActionTag.APPLY_BURN_DAMAGE).getActionInfo()
                        .wrapTag(ActionTag.APPLY_BURN_DAMAGE, burn);
            }

            int currentBurn = target.getActionByTags(ActionTag.APPLY_BURN_DAMAGE).getActionInfo().getTagValue(ActionTag.APPLY_BURN_DAMAGE);

            message += target.getBattleName() + " takes " + burn + " burn stacks! (" + currentBurn + " total)\n";

            battleController.getTurnController().regenerateTurnOrder();
            return message;
        };
    }

    public static CreatureSkill letoSkill() {
        return (battleController, leto) -> {
            int level = leto.getCreature().getLevel();
            double coeff = level >= 9 ? 1.0 : level >= 6 ? 0.8 : level >= 3 ? 0.6 : 0.4;
            int damage = (int) (leto.getCurrentAttack() * coeff) + leto.getCurrentSpellPower() * 3 + level;
            BattlefieldCreature firstTarget = leto.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            BattlefieldCreature secondTarget = leto.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;
            if (firstTarget == secondTarget) {
                message = leto.getBattleName() + " casts \"Double shoot\" on " + firstTarget.getBattleName() + "!\n";
            } else {
                message = leto.getBattleName() + " casts \"Double shoot\" on " + firstTarget.getBattleName() + " and " + secondTarget.getBattleName() + "!\n";
            }

            message += dealPhysicalDamage(leto, firstTarget, damage);
            if (secondTarget.hasStatus(ObjectStatus.ALIVE)) {
                message += dealPhysicalDamage(leto, secondTarget, damage);
            }
            return message;
        };
    }

    public static CreatureSkill annieSkill() {
        return (battleController, annie) -> {
            int coeff = annie.getCurrentSpeed() / 100;
            int damage = (annie.getCurrentAttack()) * coeff;
            int speedBoost = annie.getCreature().getLevel() * 2 + annie.getCurrentSpellPower() * 2;
            BattlefieldCreature target = annie.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;
            message = annie.getBattleName() + " casts \"Blade swing\" on " + target.getBattleName() + "!\n";
            message += dealPhysicalDamage(annie, target, damage);
            annie.applyBuff(Stat.SPEED, StatChangeSource.UNTIL_BATTLE_END, speedBoost);
            message += annie.getBattleName() + " gains " + speedBoost + " speed! [" + annie.getCurrentSpeed() + "]\n";
            return message;
        };
    }

    public static CreatureSkill bolverSkill() {
        return (battleController, bolver) -> {
            String message;
            BattlefieldCreature target = bolver.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            int level = bolver.getCreature().getLevel();
            double coeff = level >= 9 ? 0.26 : level >= 6 ? 0.21 : level >= 3 ? 0.17 : 0.15;
            coeff += bolver.getCurrentSpellPower() / 100.0;
            int damage = (int) (coeff * bolver.getMaxHp());
            message = bolver.getBattleName() + " casts \"Body slam\" on " + target.getBattleName() + "!\n";
            message += dealPhysicalDamage(bolver, target, damage);
            return message;
        };
    }

    public static CreatureSkill shayaSkill() {
        return (battleController, shaya) -> {
            String message = shaya.getBattleName() + " casts \"Frog summon\"!\n";;
            int sp = shaya.getCurrentSpellPower();
            int level = shaya.getCreature().getLevel();
            int frogHP = (2 + level) * sp;
            int frogAttack = level * 2 + sp;
            int frogPhysArmor = sp / 2 + level;
            int frogMagArmor = (level + sp) / 2;
            int frogSP = (level + sp) / 2;
            int frogSpeed = 80 + sp * (1 + level);
            Creature frog = Creature.withStats("Frog", frogHP, frogAttack, frogPhysArmor, frogMagArmor, frogSP, frogSpeed, 80, 1)
                    .wrapSkill(frogSkill());
            BattlefieldCreature battleFrog = BattlefieldCreature.fromCreature(frog);
            shaya.getBattlefieldSide().addCreature(battleFrog);
            battleFrog.setBattlefield(shaya.getBattlefield());
            battleController.getTurnController().addCreatureToTurnOrder(battleFrog);
            message += battleFrog.getBattleName() + " appears on battlefield!\n";;
            return message;
        };
    }

    private static CreatureSkill frogSkill() {
        return (battleController, frog) -> {
            BattlefieldCreature target = frog.getBattlefieldSide().getOppositeSide().getCreatureWithLowest(Stat.HP);
            String message = frog.getBattleName() + " casts \"Tongue hit\" on " + target.getBattleName() + "!\n";
            message += dealPhysicalDamage(frog, target, frog.getCurrentAttack() + frog.getCurrentSpellPower());
            return message;
        };
    }

    public static String dealMagicDamage(BattlefieldCreature dealer, BattlefieldCreature target, int amount) {
        String message;
        message = resolve(dealer, ResolveTime.BEFORE_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.BEFORE_DEALING_MAGIC_DAMAGE);
        Action takeDamageAction = ActionFactory.takeMagicDamageAction(target, amount);
        target.addAction(takeDamageAction);
        message += resolve(dealer, ResolveTime.ON_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.ON_DEALING_MAGIC_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE);
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
        message += resolve(dealer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE);
        return message;
    }

    public static String dealPhysicalDamage(BattlefieldCreature dealer, BattlefieldCreature target, int amount) {
        String message;
        message = resolve(dealer, ResolveTime.BEFORE_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.BEFORE_DEALING_PHYSICAL_DAMAGE);
        Action takeDamageAction = ActionFactory.takePhysicalDamageAction(target, amount);
        target.addAction(takeDamageAction);
        message += resolve(dealer, ResolveTime.ON_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.ON_DEALING_PHYSICAL_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_PHYSICAL_DAMAGE);
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_PHYSICAL_DAMAGE);
        message += resolve(dealer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE);
        return message;
    }

    public static CreatureSkill dealPhysicalDamageSkill(String skillName, int amount) {
        return (battleController, user) -> {
            BattlefieldCreature target = user.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message = user.getBattleName() + " casts \"" + skillName + "\" on " + target.getBattleName() + "!\n";
            message += dealPhysicalDamage(user, target, amount);
            return message;
        };
    }
}

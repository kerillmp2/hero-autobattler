package core.creature.skills;

import java.util.Comparator;
import java.util.List;

import core.action.Action;
import core.action.ActionFactory;
import core.action.ActionTag;
import core.action.ResolveTime;
import core.battlefield.BattlefieldCreature;
import core.battlefield.ObjectStatus;
import core.controllers.utils.MessageController;
import core.creature.Creature;
import core.creature.CreatureTag;
import core.creature.stat.Stat;
import core.creature.stat.StatChangeSource;
import statistics.Metric;
import statistics.StatisticCollector;
import utils.Constants;

import static core.controllers.ActionController.resolve;

public class CreatureSkillFactory {

    public static CreatureSkill emptySkill() {
        return (battleController, user) -> user.getBattleName() + " casts skill!\n";
    }

    public static CreatureSkill dunkanSkill() {
        return (battleController, dunkan) -> {
            int armorGain = dunkan.getCurrentSpellPower() / 3 + dunkan.getLevel() / 2 + 1;
            BattlefieldCreature target = dunkan.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;
            message = dunkan.getBattleName() + " casts \"Shield slam\" on " + target.getBattleName() + "!\n";
            message += buff(dunkan, Stat.PHYSICAL_ARMOR, armorGain);
            int damage = dunkan.getCurrentAttack() / 4 + dunkan.getCurrentPhysicalArmor() * 2 + dunkan.getCurrentSpellPower();
            message += dealPhysicalDamage(dunkan, target, damage);
            return message;
        };
    }

    public static CreatureSkill salviraSkill() {
        return (battleController, salvira) -> {
            String message;
            int amount = salvira.getCurrentSpellPower() / 2 + salvira.getCreature().getLevel();
            message = salvira.getBattleName() + " casts \"Poison daggers\" and gains +" + amount + " poison!\n";
            salvira.getCreature().applyCreatureTagChange(CreatureTag.POISONOUS, StatChangeSource.UNTIL_BATTLE_END, amount);
            message += resolve(ActionFactory.attackAction(salvira));

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
          int buffAmount = warbot.getCurrentSpellPower() / 2 + warbot.getCreature().getLevel();
          message = warbot.getBattleName() + " casts \"Attack command\"!\n";
          message += buff(warbot, Stat.ATTACK, buffAmount);
          message += resolve(ActionFactory.attackAction(warbot));
          return message;
        };
    }

    public static CreatureSkill kodjiSkill() {
        return (battleController, kodji) -> {
            BattlefieldCreature target = kodji.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;

            int damage = (int) (kodji.getCurrentSpellPower() * 2.5 + kodji.getCreature().getLevel() * 3);
            int slow = kodji.getCurrentSpellPower() + kodji.getCreature().getLevel() * 2;
            message = kodji.getBattleName() + " casts \"Frost bolt\" on " + target.getBattleName() + "\n";
            message += dealMagicDamage(kodji, target, damage);
            message += debuff(target, Stat.SPEED, slow);
            battleController.getTurnController().regenerateTurnOrder();
            return message;
        };
    }

    public static CreatureSkill miraSkill() {
        return (battleController, mira) -> {
            BattlefieldCreature target = mira.getBattlefieldSide().getCreatureWithHighestBy((o1, o2) -> {
                int firstMissing = o1.getMaxHp() - o1.getCurrentHp();
                int secondMissing = o2.getMaxHp() - o1.getCurrentHp();
                return firstMissing - secondMissing;
            });
            String message;
            int healingAmount = (target.getMaxHp() / 100 * 9) + mira.getCurrentSpellPower() * 3;
            message = mira.getBattleName() + " casts \"Healing potion\" on " + target.getBattleName() + "\n";
            message += resolve(ActionFactory.healingAction(target, healingAmount));

            return message;
        };
    }

    public static CreatureSkill miraBouncingSkill() {
        return (battleController, mira) -> {
            BattlefieldCreature target = mira.getBattlefieldSide().getCreatureWithHighestBy((o1, o2) -> {
                int firstMissing = o1.getMaxHp() - o1.getCurrentHp();
                int secondMissing = o2.getMaxHp() - o1.getCurrentHp();
                return firstMissing - secondMissing;
            });
            String message;
            int healingAmount = ((target.getMaxHp() / 100 * 9) + mira.getCurrentSpellPower() * 3) / 2;
            message = mira.getBattleName() + " \"Healing potion\" bounces on " + target.getBattleName() + "!\n";
            message += resolve(ActionFactory.healingAction(target, healingAmount));

            return message;
        };
    }

    public static CreatureSkill obbySkill() {
        return (battleController, obby) -> {
            String message;
            BattlefieldCreature target = obby.getBattlefieldSide().getRandomOppositeSideAliveCreature();

            int damage = obby.getCurrentSpellPower() + obby.getCurrentAttack() / 2;
            int burn = obby.getCurrentSpellPower() * 2 + obby.getCreature().getLevel() * 2;

            message = obby.getBattleName() + " casts \"Fire spit\" on " + target.getBattleName() + "\n";
            message += dealMagicDamage(obby, target, damage);
            message += applyBurn(target, burn);

            return message;
        };
    }

    public static CreatureSkill letoSkill() {
        return (battleController, leto) -> {
            int level = leto.getCreature().getLevel();
            double coeff = level >= 9 ? 1.0 : level >= 6 ? 0.8 : level >= 3 ? 0.6 : 0.4;
            int damage = (int) (leto.getCurrentAttack() * coeff) + leto.getCurrentSpellPower() * 2 + level * 2;
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
            int damage = (annie.getCurrentAttack()) * coeff + annie.getCreature().getLevel() + annie.getCurrentSpellPower();
            int speedBoost = annie.getCreature().getLevel() * 2 + annie.getCurrentSpellPower() * 4;
            BattlefieldCreature target = annie.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message;
            message = annie.getBattleName() + " casts \"Blade swing\" on " + target.getBattleName() + "!\n";
            message += dealPhysicalDamage(annie, target, damage);
            int curSpeed = annie.getCurrentSpeed();
            int buff = (int) ((curSpeed / 100.0) * speedBoost);
            message += buff(annie, Stat.SPEED, buff);
            return message;
        };
    }

    public static CreatureSkill bolverSkill() {
        return (battleController, bolver) -> {
            String message;
            BattlefieldCreature target = bolver.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            int level = bolver.getCreature().getLevel();
            double coeff = level >= 9 ? 0.23 : level >= 6 ? 0.16 : level >= 3 ? 0.12 : 0.09;
            coeff += bolver.getCurrentSpellPower() / 110.0;
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
            int frogHP = (int) (2.4 * sp);
            int frogAttack = level + sp;
            int frogPhysArmor = sp / 6 + 1;
            int frogMagArmor = sp / 3 + 1;
            int frogSP = level + sp;
            int frogSpeed = 80 + sp * (3 + level);
            Creature frog = Creature.withStats("Frog", frogHP, frogAttack, frogPhysArmor, frogMagArmor, frogSP, frogSpeed, 40, 1)
                    .wrapSkill(frogSkill());
            frog.setLevel(level);
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
            int damage = frog.getCurrentAttack() + frog.getCurrentSpellPower() + 1;
            int armorLoss = frog.getLevel();
            message += dealPhysicalDamage(frog, target, damage);
            message += debuff(target, Stat.PHYSICAL_ARMOR, armorLoss);
            return message;
        };
    }

    public static CreatureSkill roverSkill() {
        return (battleController, rover) -> {
            int level = rover.getCreature().getLevel();
            double coeff = level >= 9 ? 0.75 : level >= 6 ? 0.5 : level >= 3 ? 0.33 : 0.25;
            int parm = (int) ((rover.getCurrentPhysicalArmor() + rover.getCurrentSpellPower() / 2) * coeff);
            int marm = (int) ((rover.getCurrentMagicArmor() + rover.getCurrentSpellPower() / 2) * coeff);
            BattlefieldCreature target = rover.getBattlefieldSide().getCreatureWithLowest(Stat.PHYSICAL_ARMOR);
            String message;
            message = rover.getBattleName() + " casts \"Protect command\" on " + target.getBattleName() + "!\n";
            message += buff(target, Stat.PHYSICAL_ARMOR, parm);
            message += buff(target, Stat.MAGIC_ARMOR, parm);
            return message;
        };
    }

    public static CreatureSkill cathyraSkill() {
        return (battleController, cathyra) -> {
            String message;
            BattlefieldCreature first_target = cathyra.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            BattlefieldCreature second_target = cathyra.getBattlefieldSide().getRandomOppositeSideAliveCreature();

            int damage = cathyra.getCurrentAttack() / 3 + cathyra.getCurrentSpeed() / 40;
            int burn = cathyra.getCurrentSpeed() / 50 + (cathyra.getCurrentSpellPower()) * 2;

            if (first_target == second_target) {
                message = cathyra.getBattleName() + " casts \"Burning knives\" on " + first_target.getBattleName() + "\n";
            } else {
                message = cathyra.getBattleName() + " casts \"Burning knives\" on " + first_target.getBattleName() + " and " + second_target.getBattleName() + "\n";
            }

            message += dealPhysicalDamage(cathyra, first_target, damage);
            message += applyBurn(first_target, burn);
            if (second_target.hasStatuses(ObjectStatus.ALIVE)) {
                message += dealPhysicalDamage(cathyra, second_target, damage);
                message += applyBurn(second_target, burn);
            }

            return message;
        };
    }

    public static CreatureSkill coldySkill() {
        return (battleController, coldy) -> {
            BattlefieldCreature target = coldy.getBattlefieldSide().getOppositeSide().getCreatureWithHighest(Stat.PHYSICAL_ARMOR);
            String message;
            int level = coldy.getLevel();
            int armorSteal = (level >= 9 ? 70 : level >= 6 ? 55 : level >= 3 ? 40 : 30) + coldy.getCurrentSpellPower();
            int slow = (level >= 9 ? 25 : level >= 6 ? 16 : level >= 3 ? 10 : 6) + coldy.getCurrentSpellPower() / 4;
            message = coldy.getBattleName() + " casts \"Freezing steel\" on " + target.getBattleName() + "\n";
            int stealAmount = (int) (target.getCurrentPhysicalArmor() / 100.0 * armorSteal);
            stealAmount = Math.max(stealAmount, level);
            int slowAmount = (int) (target.getCurrentSpeed() / 100.0 * slow);
            message += debuff(target, Stat.PHYSICAL_ARMOR, stealAmount);
            message += debuff(target, Stat.SPEED, slowAmount);
            message += buff(coldy, Stat.PHYSICAL_ARMOR, stealAmount);
            battleController.getTurnController().regenerateTurnOrder();
            return message;
        };
    }

    public static CreatureSkill jackSkill() {
        return (battleController, jack) -> {
            String message = jack.getBattleName() + " casts \"Mini jack\"!\n";
            int sp = jack.getCurrentSpellPower();
            int level = jack.getCreature().getLevel();
            int monkeyHP = (int) (2.6 * sp);
            int monkeyAttack = (int) (sp * 1.2) + level;
            int monkeyPhysArmor = sp / 5 + 1;
            int monkeyMagArmor = sp / 3 + 2;
            int monkeySP = 1 + sp / 2;
            int monkeySpeed = (int) (90 + sp * (3.5));
            Creature monkey = Creature.withStats("Monkey", monkeyHP, monkeyAttack, monkeyPhysArmor, monkeyMagArmor, monkeySP, monkeySpeed, 50, 1)
                    .wrapSkill(monkeySkill());
            monkey.setLevel(level);
            BattlefieldCreature battleMonkey = BattlefieldCreature.fromCreature(monkey);
            jack.getBattlefieldSide().addCreature(battleMonkey);
            battleMonkey.setBattlefield(jack.getBattlefield());
            battleController.getTurnController().addCreatureToTurnOrder(battleMonkey);
            message += battleMonkey.getBattleName() + " appears on battlefield!\n";;
            return message;
        };
    }

    private static CreatureSkill monkeySkill() {
        return (battleController, monkey) -> {
            BattlefieldCreature target = monkey.getBattlefieldSide().getOppositeSide().getRandomOppositeSideCreature(ObjectStatus.ALIVE);
            String message = monkey.getBattleName() + " casts \"Flying banana\" on " + target.getBattleName() + "!\n";
            int heal = monkey.getCurrentSpellPower();
            int damageGain = monkey.getCurrentSpellPower() / 5 + monkey.getLevel();
            message += resolve(ActionFactory.healingAction(target, heal));
            message += buff(target, Stat.ATTACK, damageGain);
            return message;
        };
    }

    public static CreatureSkill aralisSkill() {
        return (battleController, aralis) -> {
            String message = aralis.getBattleName() + " casts \"Demonic arrows\"!\n";
            int level = aralis.getLevel();
            int penetrationAmount = level >= 9 ? 10 : level >= 6 ? 7 : level >= 3 ? 5 : 2;
            int damage = aralis.getCurrentAttack() / 5 + aralis.getCurrentSpellPower();
            aralis.addAction(ActionFactory.ActionBuilder.empty().from(aralis).withTime(ResolveTime.AFTER_ATTACK).wrapTag(ActionTag.BASIC_ATTACK_BUFF).wrapTag(ActionTag.DEAL_MAGIC_DAMAGE, damage).build());
            aralis.addAction(ActionFactory.ActionBuilder.empty().from(aralis).withTime(ResolveTime.AFTER_ATTACK).wrapTag(ActionTag.BASIC_ATTACK_BUFF).wrapTag(ActionTag.REDUCE_FLOAT_STAT).wrapTag(ActionTag.PHYSICAL_ARMOR, penetrationAmount).build());
            message += resolve(ActionFactory.attackAction(aralis));
            return message;
        };
    }

    public static CreatureSkill weissSkill() {
        return (battleController, weiss) -> {
            BattlefieldCreature target = weiss.getBattlefieldSide().getRandomOppositeSideCreature(ObjectStatus.ALIVE);
            String message = weiss.getBattleName() + " casts \"Curse potion\" on " + target.getBattleName() + "!\n";
            int debuffCoeff = (int) (weiss.getLevel() * 3.5 + weiss.getCurrentSpellPower() * 2);
            int damage = weiss.getCurrentSpellPower() + weiss.getLevel() * 4;
            message += dealMagicDamage(weiss, target, damage);
            target.applyCreatureTagChange(CreatureTag.TAKE_MORE_DAMAGE, StatChangeSource.UNTIL_BATTLE_END, debuffCoeff);
            message += target.getBattleName() + " cursed and takes " + debuffCoeff + "% more damage!\n";
            return message;
        };
    }

    public static CreatureSkill weissBouncingSkill() {
        return (battleController, weiss) -> {
            BattlefieldCreature target = weiss.getBattlefieldSide().getRandomOppositeSideCreature(ObjectStatus.ALIVE);
            String message = "\"Curse potion\" bounces on " + target.getBattleName() + "!\n";
            int debuffCoeff = (int) (weiss.getLevel() * 3.5 + weiss.getCurrentSpellPower() * 2) / 2;
            int damage = (weiss.getCurrentSpellPower() + weiss.getLevel()) / 2;
            message += dealMagicDamage(weiss, target, damage);
            target.applyCreatureTagChange(CreatureTag.TAKE_MORE_DAMAGE, StatChangeSource.UNTIL_BATTLE_END, debuffCoeff);
            message += target.getBattleName() + " cursed and takes " + debuffCoeff + "% more damage!\n";
            return message;
        };
    }

    public static CreatureSkill aramisSkill() {
        return (battleController, aramis) -> {
            int parryAmount = aramis.getLevel() / 3 + 1;
            String message = aramis.getBattleName() + " casts \"Parry stance\"! He will parry next " + parryAmount + " attacks!\n";
            int coeff = aramis.getLevel() * 10 + 10;
            int reflectDamage = (int) (aramis.getCurrentAttack() / 100.0 * coeff);
            aramis.addAction(ActionFactory.ActionBuilder.empty().from(aramis)
                    .wrapTag(ActionTag.PARRY)
                    .wrapTag(ActionTag.DEAL_PHYSICAL_DAMAGE, reflectDamage)
                    .wrapTag(ActionTag.TURNS_LEFT, parryAmount)
                    .withPrefix("%s parries %s attack!").build());
            return message;
        };
    }


    public static String dealMagicDamage(BattlefieldCreature dealer, BattlefieldCreature target, int amount) {
        String message;
        message = resolve(dealer, ResolveTime.BEFORE_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.BEFORE_DEALING_MAGIC_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_MAGIC_DAMAGE);
        int damage = target.takeMagicDamage(amount, dealer.getCreature().getName());
        message += target.getBattleName() + " takes " + damage + " damage!\n";
        message += resolve(dealer, ResolveTime.ON_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.ON_DEALING_MAGIC_DAMAGE);
        message += resolve(target, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_MAGIC_DAMAGE);
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_MAGIC_DAMAGE);
        message += resolve(dealer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_MAGIC_DAMAGE);
        updateSkillStatistic(dealer.getCreature().getName(), amount);
        return message;
    }

    public static String dealPhysicalDamage(BattlefieldCreature dealer, BattlefieldCreature target, int amount) {
        String message;
        message = resolve(dealer, ResolveTime.BEFORE_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.BEFORE_DEALING_PHYSICAL_DAMAGE);
        message += resolve(target, ResolveTime.BEFORE_TAKING_DAMAGE, ResolveTime.BEFORE_TAKING_PHYSICAL_DAMAGE);
        int damage = target.takePhysicalDamage(amount, dealer.getCreature().getName());
        message += target.getBattleName() + " takes " + damage + " damage!\n";
        message += resolve(dealer, ResolveTime.ON_DEALING_DAMAGE);
        message += resolve(dealer, ResolveTime.ON_DEALING_PHYSICAL_DAMAGE);
        message += resolve(target, ResolveTime.ON_TAKING_DAMAGE, ResolveTime.ON_TAKING_PHYSICAL_DAMAGE);
        message += resolve(target, ResolveTime.AFTER_TAKING_DAMAGE, ResolveTime.AFTER_TAKING_PHYSICAL_DAMAGE);
        message += resolve(dealer, ResolveTime.AFTER_DEALING_DAMAGE, ResolveTime.AFTER_DEALING_PHYSICAL_DAMAGE);
        updateSkillStatistic(dealer.getCreature().getName(), amount);
        return message;
    }

    private static void updateSkillStatistic(String name, int amount) {
        if (Constants.COLLECT_STATISTIC.value > 0) {
            StatisticCollector.updateCreatureMetric(name, Metric.DAMAGE_FROM_SKILL, amount);
        }
    }

    private static String buff(BattlefieldCreature creature, Stat stat, int amount) {
        creature.applyBuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
        return creature.getBattleName() + " gets " + amount + " " + stat.getName() + " [" + creature.getStat(stat) + "]\n";
    }

    private static String debuff(BattlefieldCreature creature, Stat stat, int amount) {
        creature.applyDebuff(stat, StatChangeSource.UNTIL_BATTLE_END, amount);
        return creature.getBattleName() + " loses " + amount + " " + stat.getName() + " [" + creature.getStat(stat) + "]\n";
    }

    public static CreatureSkill dealPhysicalDamageSkill(String skillName, int amount) {
        return (battleController, user) -> {
            BattlefieldCreature target = user.getBattlefieldSide().getRandomOppositeSideAliveCreature();
            String message = user.getBattleName() + " casts \"" + skillName + "\" on " + target.getBattleName() + "!\n";
            message += dealPhysicalDamage(user, target, amount);
            return message;
        };
    }

    public static String applyBurn(BattlefieldCreature target, int amount) {
        if (!target.hasActionWithTags(ActionTag.APPLY_BURN_DAMAGE)) {
            target.addAction(ActionFactory.ActionBuilder.empty().to(target).from(target).wrapTag(ActionTag.APPLY_BURN_DAMAGE, amount).wrapTag(ActionTag.DELETE_AFTER_RESOLVE).withTime(ResolveTime.ON_START_TURN).build());
        } else {
            target.getActionByTags(ActionTag.APPLY_BURN_DAMAGE).getActionInfo()
                    .wrapTag(ActionTag.APPLY_BURN_DAMAGE, amount);
        }

        int currentBurn = target.getActionByTags(ActionTag.APPLY_BURN_DAMAGE).getActionInfo().getTagValue(ActionTag.APPLY_BURN_DAMAGE);

        return target.getBattleName() + " takes " + amount + " burn stacks! (" + currentBurn + " total)\n";
    }
}

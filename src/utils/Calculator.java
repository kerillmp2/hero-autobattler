package utils;

import core.battlefield.BattlefieldCreature;
import core.creature.CreatureTag;
import core.creature.stat.WithStats;

public class Calculator {
    public static int calculateTrueDamage(int amount, BattlefieldCreature target, BattlefieldCreature dealer, boolean canBeZero) {
        int coeff = 100
                + target.getTagValue(CreatureTag.TAKE_MORE_DAMAGE) - target.getTagValue(CreatureTag.TAKE_LESS_DAMAGE)
                + dealer.getTagValue(CreatureTag.DEAL_MORE_DAMAGE) - target.getTagValue(CreatureTag.DEAL_LESS_DAMAGE);
        coeff = Math.max(0, coeff);
        return (int) Math.max((amount / 100.0 * coeff), canBeZero ? 0 : 1);
    }

    public static int calculatePhysicalDamage(int amount, BattlefieldCreature target, BattlefieldCreature dealer, boolean canBeZero) {
        int initDamage = amount - target.getCurrentPhysicalArmor();
        int coeff = 100
                + target.getTagValue(CreatureTag.TAKE_MORE_PHYSICAL_DAMAGE) + target.getTagValue(CreatureTag.TAKE_MORE_DAMAGE)
                - target.getTagValue(CreatureTag.TAKE_LESS_PHYSICAL_DAMAGE) - target.getTagValue(CreatureTag.TAKE_LESS_DAMAGE)
                + dealer.getTagValue(CreatureTag.DEAL_MORE_PHYSICAL_DAMAGE) + dealer.getTagValue(CreatureTag.DEAL_MORE_DAMAGE)
                - dealer.getTagValue(CreatureTag.DEAL_LESS_PHYSICAL_DAMAGE) - dealer.getTagValue(CreatureTag.DEAL_LESS_DAMAGE);
        coeff = Math.max(0, coeff);
        return (int) Math.max((initDamage / 100.0 * coeff), canBeZero ? 0 : 1);
    }

    public static int calculateMagicDamage(int amount, BattlefieldCreature target, boolean canBeZero) {
        int initDamage = amount - target.getCurrentMagicArmor();
        int coeff = 100
                + target.getTagValue(CreatureTag.TAKE_MORE_MAGIC_DAMAGE) + target.getTagValue(CreatureTag.TAKE_MORE_DAMAGE)
                - target.getTagValue(CreatureTag.TAKE_LESS_MAGIC_DAMAGE) - target.getTagValue(CreatureTag.TAKE_LESS_DAMAGE);
        coeff = Math.max(0, coeff);
        return (int) Math.max((initDamage / 100.0 * coeff), canBeZero ? 0 : 1);
    }

    public static int calculateMagicDamage(int amount, BattlefieldCreature target, BattlefieldCreature dealer, boolean canBeZero) {
        int initDamage = amount - target.getCurrentMagicArmor();
        int coeff = 100
                + target.getTagValue(CreatureTag.TAKE_MORE_MAGIC_DAMAGE) + target.getTagValue(CreatureTag.TAKE_MORE_DAMAGE)
                - target.getTagValue(CreatureTag.TAKE_LESS_MAGIC_DAMAGE) - target.getTagValue(CreatureTag.TAKE_LESS_DAMAGE)
                + dealer.getTagValue(CreatureTag.DEAL_MORE_MAGIC_DAMAGE) + dealer.getTagValue(CreatureTag.DEAL_MORE_DAMAGE)
                - dealer.getTagValue(CreatureTag.DEAL_LESS_MAGIC_DAMAGE) - dealer.getTagValue(CreatureTag.DEAL_LESS_DAMAGE);
        coeff = Math.max(0, coeff);
        return (int) Math.max((initDamage / 100.0 * coeff), canBeZero ? 0 : 1);    }
}

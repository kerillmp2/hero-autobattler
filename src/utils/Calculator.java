package utils;

import core.battlefield.BattlefieldCreature;
import core.creature.CreatureTag;
import core.creature.stat.WithStats;

public class Calculator {
    public static int calculateTrueDamage(int amount, BattlefieldCreature target, boolean canBeZero) {
        int initDamage = Math.max(amount, canBeZero ? 0 : 1);
        int coeff = 100
                + target.getTagValue(CreatureTag.TAKE_MORE_DAMAGE) - target.getTagValue(CreatureTag.TAKE_LESS_DAMAGE);
        coeff = Math.max(0, coeff);
        return (int) (initDamage / 100.0 * coeff);
    }

    public static int calculatePhysicalDamage(int amount, BattlefieldCreature target, boolean canBeZero) {
        int initDamage = Math.max(amount - target.getCurrentPhysicalArmor(), canBeZero ? 0 : 1);
        int coeff = 100
                + target.getTagValue(CreatureTag.TAKE_MORE_PHYSICAL_DAMAGE) + target.getTagValue(CreatureTag.TAKE_MORE_DAMAGE)
                - target.getTagValue(CreatureTag.TAKE_LESS_PHYSICAL_DAMAGE) - target.getTagValue(CreatureTag.TAKE_LESS_DAMAGE);
        coeff = Math.max(0, coeff);
        return (int) (initDamage / 100.0 * coeff);
    }

    public static int calculatePhysicalDamage(int amount, BattlefieldCreature target) {
        return calculatePhysicalDamage(amount, target, false);
    }

    public static int calculateMagicDamage(int amount, BattlefieldCreature target) {
        return calculateMagicDamage(amount, target, false);
    }

    public static int calculateMagicDamage(int amount, BattlefieldCreature target, boolean canBeZero) {
        int initDamage = Math.max(amount - target.getCurrentMagicArmor(), canBeZero ? 0 : 1);
        int coeff = 100
                + target.getTagValue(CreatureTag.TAKE_MORE_MAGIC_DAMAGE) + target.getTagValue(CreatureTag.TAKE_MORE_DAMAGE)
                - target.getTagValue(CreatureTag.TAKE_LESS_MAGIC_DAMAGE) - target.getTagValue(CreatureTag.TAKE_LESS_DAMAGE);
        coeff = Math.max(0, coeff);
        return (int) (initDamage / 100.0 * coeff);
    }
}

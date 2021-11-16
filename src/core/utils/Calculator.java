package core.utils;

import core.creature.WithStats;

public class Calculator {
    public static int calculateBasicAttackDamage(WithStats attacker, WithStats target) {
        return calculatePhysicalDamage(attacker.getCurrentAttack(), target, false);
    }

    public static int calculatePhysicalDamage(int amount, WithStats target, boolean canBeZero) {
        return Math.max(amount - target.getCurrentPhysicalArmor(), canBeZero ? 0 : 1);
    }

    public static int calculateMagicDamage(int amount, WithStats target) {
        return calculateMagicDamage(amount, target, false);
    }

    public static int calculateMagicDamage(int amount, WithStats target, boolean canBeZero) {
        return Math.max(amount - target.getCurrentMagicArmor(), canBeZero ? 0 : 1);
    }
}

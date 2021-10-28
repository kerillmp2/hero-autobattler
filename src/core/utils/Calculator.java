package core.utils;

import core.creature.WithStats;

public class Calculator {
    public static int calculateBasicAttackDamage(WithStats attacker, WithStats target) {
        return Math.max(attacker.getCurrentAttack() - target.getCurrentPhysicalArmor(), 0);
    }
}

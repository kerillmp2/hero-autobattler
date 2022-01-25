package core.creature.stat;

public enum StatChangeSource {
    PERMANENT,
    UNTIL_BATTLE_END,
    KNIGHT_TRAIT(true),
    POISONOUS_TRAIT(true),
    EATERS_TRAIT(true),
    ROBOT_TRAIT(true),
    MAGE_TRAIT(true),
    WARRIOR_TRAIT(true),
    ASSASSIN_TRAIT(true),
    STUDENT_TRAIT(true),
    DEFENDER_TRAIT(true),
    DEMON_TRAIT(true),
    ALCHEMIST_TRAIT(true),
    DUELISTS_TRAIT(true),
    FROSTBORN_TRAIT(true);

    private boolean isTrait = false;

    StatChangeSource(boolean isTrait) {
        this.isTrait = isTrait;
    }

    StatChangeSource() {
    }

    public boolean isTrait() {
        return isTrait;
    }
}

package core.creature;

public interface WithStats {
    int getCurrentHp();
    int getCurrentAttack();
    int getCurrentPhysicalArmor();
    int getCurrentMagicArmor();
    int getCurrentSpellPower();
    int getCurrentSpeed();
    void setCurrentHp(int hp);
    void setCurrentAttack(int attack);
    void setCurrentPhysicalArmor(int physicalArmor);
    void setCurrentMagicArmor(int magicArmor);
    void setCurrentSpellPower(int spellPower);
    void setCurrentSpeed(int speed);
}

package statistics;

public enum Metric {
    UNDEFINED,
    PHYSICAL_DAMAGE_DEALT,
    MAGIC_DAMAGE_DEALT,
    TRUE_DAMAGE_DEALT,
    TOTAL_DAMAGE_DEALT,
    DAMAGE_FROM_ATTACKS,
    DAMAGE_FROM_SKILL,
    PHYSICAL_DAMAGE_TAKEN,
    MAGIC_DAMAGE_TAKEN,
    TRUE_DAMAGE_TAKEN,
    TOTAL_DAMAGE_TAKEN,
    HP_HEALED,
    ATTACK_GAINED,
    PHYSICAL_ARMOR_GAINED,
    MAGIC_ARMOR_GAINED,
    SPEED_GAINED,
    SPELL_POWER_GAINED,
    MANA_GAINED,
    ATTACK_LOST,
    PHYSICAL_ARMOR_LOST,
    MAGIC_ARMOR_LOST,
    SPEED_LOST,
    SPELL_POWER_LOST,
    MANA_LOST,
    MAGIC_BARRIER_ABSORBED,
    PHYSICAL_BARRIER_ABSORBED,
    BARRIER_ABSORBED,
    MAGIC_BARRIER_GAINED,
    PHYSICAL_BARRIER_GAINED,
    BARRIER_GAINED,;

    private String name;

    Metric(String name) {
        this.name = name;
    }

    Metric() {
        this.name = this.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

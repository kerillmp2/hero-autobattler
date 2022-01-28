package core.creature.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.controllers.utils.MessageController;
import core.creature.CreatureTag;
import utils.TagContainer;

public class StatsContainer extends TagContainer<Stat> {
    private List<StatChange> buffs;
    private List<StatChange> debuffs;
    private List<CreatureTagChange> tagChanges;

    public StatsContainer() {
        super();
        buffs = new ArrayList<>();
        debuffs = new ArrayList<>();
        tagChanges = new ArrayList<>();
    }

    @Override
    public int getTagValue(Stat stat) {
        int defaultValue = super.getTagValue(stat);
        int floatBuffs = Math.max(calculateFloatBuffsForStat(stat), 0);
        int percentageBuffs = Math.max(calculatePercentageBuffsForStat(stat), 0);
        double buffCoefficient = (100.0 + (double) percentageBuffs) / 100.0;
        int debuffs = Math.max(calculateFloatDebuffsForStat(stat), 0);
        int percentageDebuffs = Math.max(calculatePercentageDebuffsForStat(stat), 0);
        double debuffCoefficient = (100.0 - (double) percentageDebuffs) / 100.0;
        /*if (stat == Stat.PHYSICAL_ARMOR) {
            MessageController.print("defaultValue: " + defaultValue);
            MessageController.print("floatBuffs: " + floatBuffs);
            MessageController.print("percentageBuffs: " + percentageBuffs);
            MessageController.print("buffCoefficient: " + buffCoefficient);
            MessageController.print("debuffs: " + debuffs);
            MessageController.print("percentageDebuffs: " + percentageDebuffs);
            MessageController.print("debuffCoefficient: " + debuffCoefficient);
        }*/
        return Math.max((int) (((defaultValue + floatBuffs) * buffCoefficient - debuffs) * debuffCoefficient), 0);
    }

    public int getTagValue(CreatureTag tag) {
        return tagChanges.stream().filter(c -> c.creatureTag.getId() == tag.getId()).mapToInt(c -> c.amount).sum();
    }

    private int calculateFloatBuffsForStat(Stat stat) {
        int buff = 0;
        List<StatChange> floatBuffs = getBuffsForStat(stat).stream().filter(b -> !b.isPercentage()).collect(Collectors.toList());
        for (StatChange statChange : floatBuffs) {
            buff += statChange.getAmount();
        }
        return buff;
    }

    private int calculatePercentageBuffsForStat(Stat stat) {
        int buff = 0;
        List<StatChange> percentageBuffs = getBuffsForStat(stat).stream().filter(StatChange::isPercentage).collect(Collectors.toList());
        for (StatChange statChange : percentageBuffs) {
            buff += statChange.getAmount();
        }
        return buff;
    }

    private int calculateFloatDebuffsForStat(Stat stat) {
        int debuff = 0;
        List<StatChange> debuffs = getDebuffsForStat(stat);
        for (StatChange statChange : debuffs) {
            debuff += statChange.getAmount();
        }
        return debuff;
    }

    private int calculatePercentageDebuffsForStat(Stat stat) {
        int debuff = 0;
        List<StatChange> percentageDebuffs = getDebuffsForStat(stat).stream().filter(StatChange::isPercentage).collect(Collectors.toList());
        for (StatChange statChange : percentageDebuffs) {
            debuff += statChange.getAmount();
        }
        return debuff;
    }

    private List<StatChange> getBuffsForStat(Stat stat) {
        return buffs.stream().filter(statChange -> statChange.getStat() == stat).collect(Collectors.toList());
    }

    private List<StatChange> getDebuffsForStat(Stat stat) {
        return debuffs.stream().filter(statChange -> statChange.getStat() == stat).collect(Collectors.toList());
    }

    public void setTag(CreatureTag creatureTag, int amount) {
        clearAllChangesForTag(creatureTag);
    }

    public void clearAllChangesForTag(CreatureTag creatureTag) {
        tagChanges.removeIf(c -> c.creatureTag == creatureTag);
    }

    public void addTagChange(CreatureTag creatureTag, StatChangeSource source, int amount) {
        CreatureTagChange creatureTagChange = new CreatureTagChange(creatureTag, source, amount);
        tagChanges.add(creatureTagChange);
    }

    public void addBuff(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        buffs.add(new StatChange(stat, source, amount, isPercentage));
    }

    public void addDebuff(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
        debuffs.add(new StatChange(stat, source, amount, isPercentage));
    }

    public void clearAllChangesFromSource(StatChangeSource source) {
        clearBuffsFromSource(source);
        clearDebuffsFromSource(source);
        clearCreatureTagChangesFromSource(source);
    }

    public void clearBuffsFromSource(Stat stat, StatChangeSource source) {
        buffs.removeIf(statChange -> (statChange.stat == stat && statChange.source == source));
    }

    public void clearBuffsFromSource(StatChangeSource source) {
        buffs.removeIf(statChange -> statChange.source == source);
    }

    public void clearDebuffsFromSource(Stat stat, StatChangeSource source) {
        debuffs.removeIf(statChange -> (statChange.stat == stat && statChange.source == source));
    }

    public void clearDebuffsFromSource(StatChangeSource source) {
        debuffs.removeIf(statChange -> statChange.source == source);
    }

    public void clearCreatureTagChangesFromSource(StatChangeSource source) {
        tagChanges.removeIf(tagChange -> tagChange.source == source);
    }

    public void clearCreatureTagChangesFromSource(CreatureTag tag, StatChangeSource source) {
        tagChanges.removeIf(tagChange -> (tagChange.creatureTag == tag && tagChange.source == source));
    }

    public static class StatChange {
        private final Stat stat;
        private final StatChangeSource source;
        private final int amount;
        private final boolean isPercentage;

        @Override
        public String toString() {
            return stat.getName() + " " + source.toString() + " " + amount + " " + isPercentage;
        }

        public StatChange(Stat stat, StatChangeSource source, int amount, boolean isPercentage) {
            this.stat = stat;
            this.source = source;
            this.amount = amount;
            this.isPercentage = isPercentage;
        }

        public Stat getStat() {
            return stat;
        }

        public StatChangeSource getSource() {
            return source;
        }

        public int getAmount() {
            return amount;
        }

        public boolean isPercentage() {
            return isPercentage;
        }
    }

    private static class CreatureTagChange {
        private final CreatureTag creatureTag;
        private final StatChangeSource source;
        private final int amount;

        @Override
        public String toString() {
            return creatureTag.getName() + " " + source.toString() + " " + amount;
        }

        public CreatureTagChange(CreatureTag creatureTag, StatChangeSource source, int amount) {
            this.creatureTag = creatureTag;
            this.source = source;
            this.amount = amount;
        }

        public CreatureTag getCreatureTag() {
            return creatureTag;
        }

        public StatChangeSource getSource() {
            return source;
        }

        public int getAmount() {
            return amount;
        }
    }
}

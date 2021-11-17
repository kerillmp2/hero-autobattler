package core.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.utils.TagContainer;

public class StatsContainer extends TagContainer<Stat> {
    private List<StatChange> buffs = new ArrayList<>();
    private List<StatChange> debuffs = new ArrayList<>();

    @Override
    public int getTagValue(Stat stat) {
        int defaultValue = super.getTagValue(stat);
        int buffs = Math.max(calculateBuffsForStat(stat), 0);
        int debuffs = Math.max(calculateDebuffsForStat(stat), 0);
        return Math.max(defaultValue + buffs - debuffs, 0);
    }

    private int calculateBuffsForStat(Stat stat) {
        int buff = 0;
        List<StatChange> buffs = getBuffsForStat(stat);
        for (StatChange statChange : buffs) {
            buff += statChange.getAmount();
        }
        return buff;
    }

    private int calculateDebuffsForStat(Stat stat) {
        int debuff = 0;
        List<StatChange> debuffs = getDebuffsForStat(stat);
        for (StatChange statChange : debuffs) {
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

    public void addBuff(Stat stat, StatChangeSource source, int amount) {
        buffs.add(new StatChange(stat, source, amount));
    }

    public void addDebuff(Stat stat, StatChangeSource source, int amount) {
        debuffs.add(new StatChange(stat, source, amount));
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

    public static class StatChange {
        private final Stat stat;
        private final StatChangeSource source;
        private final int amount;

        public StatChange(Stat stat, StatChangeSource source, int amount) {
            this.stat = stat;
            this.source = source;
            this.amount = amount;
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
    }
}

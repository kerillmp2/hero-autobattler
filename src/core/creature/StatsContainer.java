package core.creature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        int buffs = Math.max(calculateBuffsForStat(stat), 0);
        int debuffs = Math.max(calculateDebuffsForStat(stat), 0);
        return Math.max(defaultValue + buffs - debuffs, 0);
    }

    public int getTagValue(CreatureTag tag) {
        return tagChanges.stream().filter(c -> c.creatureTag == tag).mapToInt(c -> c.amount).sum();
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

    public void addTagChange(CreatureTag creatureTag, StatChangeSource source, int amount) {
        tagChanges.add(new CreatureTagChange(creatureTag, source, amount));
    }

    public void addBuff(Stat stat, StatChangeSource source, int amount) {
        buffs.add(new StatChange(stat, source, amount));
    }

    public void addDebuff(Stat stat, StatChangeSource source, int amount) {
        debuffs.add(new StatChange(stat, source, amount));
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

    private static class StatChange {
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

    private static class CreatureTagChange {
        private final CreatureTag creatureTag;
        private final StatChangeSource source;
        private final int amount;

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

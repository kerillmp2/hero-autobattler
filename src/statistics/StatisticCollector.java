package statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.creature.stat.Stat;
import utils.Constants;
import utils.Pair;

public class StatisticCollector {

    public static Map<String, CreatureStatistic> statistics;

    public static void init() {
        Constants.COLLECT_STATISTIC.value = 1;
        statistics = new HashMap<>();
    }

    public static void resetEachBattle() {
        Constants.EACH_BATTLE_STATISTIC.value = 1;
    }

    public static void updateCreatureStatistic(String name, Stat stat, int oldValue, int newValue) {
        int diff = newValue - oldValue;
        Metric metric = Metric.UNDEFINED;
        if (diff >= 0) {
            switch (stat) {
                case HP:
                    metric = Metric.HP_HEALED;
                    break;
                case ATTACK:
                    metric = Metric.ATTACK_GAINED;
                    break;
                case PHYSICAL_ARMOR:
                    metric = Metric.PHYSICAL_ARMOR_GAINED;
                    break;
                case MAGIC_ARMOR:
                    metric = Metric.MAGIC_ARMOR_GAINED;
                    break;
                case SPELL_POWER:
                    metric = Metric.SPELL_POWER_GAINED;
                    break;
                case SPEED:
                    metric = Metric.SPEED_GAINED;
                    break;
                case MANA:
                    metric = Metric.MANA_GAINED;
                    break;
            }
        } else {
            switch (stat) {
                case HP:
                    //????????????
                    metric = Metric.UNDEFINED;
                    break;
                case ATTACK:
                    metric = Metric.ATTACK_LOST;
                    break;
                case PHYSICAL_ARMOR:
                    metric = Metric.PHYSICAL_ARMOR_LOST;
                    break;
                case MAGIC_ARMOR:
                    metric = Metric.MAGIC_ARMOR_LOST;
                    break;
                case SPELL_POWER:
                    metric = Metric.SPELL_POWER_LOST;
                    break;
                case SPEED:
                    metric = Metric.SPEED_LOST;
                    break;
                case MANA:
                    metric = Metric.MANA_LOST;
                    break;
            }
        }
        updateCreatureMetric(name, metric, Math.abs(diff));
    }

    public static void updateCreatureMetric(String name, Metric metric, int value) {
        if (!statistics.containsKey(name)) {
            statistics.put(name, CreatureStatistic.forCreature(name));
        }
        statistics.get(name).addMetric(metric, value);
    }

    public static List<Pair<String, Integer>> getSortedMetrics(Metric metric, int top) {
        List<Pair<String, Integer>> metrics = new ArrayList<>();
        for (Map.Entry<String, CreatureStatistic> statistic : statistics.entrySet()) {
            int value = statistic.getValue().getMetric(metric);
            if (value != 0) {
                metrics.add(new Pair<>(statistic.getKey(), value));
            }
        }
        metrics.sort(Comparator.comparingInt(o -> o.second));
        Collections.reverse(metrics);
        return metrics.subList(0, Math.min(top + 1, metrics.size()));
    }
}

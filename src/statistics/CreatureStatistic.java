package statistics;

import java.util.HashMap;
import java.util.Map;

public class CreatureStatistic {
    private Map<Metric, Integer> metrics;
    private String creatureName;

    private CreatureStatistic(Map<Metric, Integer> statistic, String creatureName) {
        this.metrics = statistic;
        this.creatureName = creatureName;
    }

    public static CreatureStatistic forCreature(String creatureName) {
        Map<Metric, Integer> metrics = new HashMap<>();
        for (Metric metric : Metric.values()) {
            metrics.put(metric, 0);
        }
        return new CreatureStatistic(new HashMap<>(), creatureName);
    }

    public String getCreatureName() {
        return creatureName;
    }

    public void setCreatureName(String creatureName) {
        this.creatureName = creatureName;
    }

    public Map<Metric, Integer> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<Metric, Integer> metrics) {
        this.metrics = metrics;
    }

    public int getMetric(Metric metric) {
        return metrics.getOrDefault(metric, 0);
    }

    public void addMetric(Metric metric, int value) {
        int newValue = metrics.getOrDefault(metric, 0) + value;
        metrics.put(metric, newValue);
    }
}

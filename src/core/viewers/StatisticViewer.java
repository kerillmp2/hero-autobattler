package core.viewers;

import java.util.List;

import statistics.Metric;
import statistics.StatisticCollector;
import utils.Constants;
import utils.Pair;

public class StatisticViewer extends Viewer {
    public static String getStatisticView(Metric... metrics) {
        Window window = new Window();
        window.add(header());
        int top = Constants.PRINT_TOP_STATISTIC.value;
        if (Constants.COLLECT_STATISTIC.value > 0) {
            for (Metric metric : metrics) {
                List<Pair<String, Integer>> metricList = StatisticCollector.getSortedMetrics(metric, top);
                if (metricList.size() > 0) {
                    window.line(metric.getName() + " (TOP " + top + "):");
                    window.emptyLine();
                    for (Pair<String, Integer> result : metricList) {
                        window.line(result.first + ": " + result.second);
                    }
                    window.lineWithAngles();
                }
            }
        }
        return window.getView();
    }

    public static String header() {
        Window window = new Window();
        window.lineWithAngles();
        window.line("STATISTIC");
        window.lineWithAngles();

        return window.getView();
    }
}

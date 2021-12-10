package core.viewers;

import java.util.List;

import utils.Constants;

public class Viewer {
    private static final int ROW_SIZE = Constants.SHOP_VIEW_SIZE.value;
    private static final int OFFSET = Constants.MAP_OFFSET.value;

    protected static String emptyLine() {
        return line(" ", "|", "|", true);
    }

    protected static String lineWithAngles() {
        return line("-", "+", "+", true);
    }

    protected static String line() {
        return line("-", "-", "-", true);
    }

    protected static String line(String sym, String leftAngle, String rightAngle, boolean endLine) {
        return leftAngle + sym.repeat(ROW_SIZE - 2) + rightAngle + (endLine ? "\n" : "");
    }

    protected static String windowWith(List<String>... windows) {
        StringBuilder view = new StringBuilder();
        for (List<String> lines : windows) {
            if (lines.size() > 0) {
                view.append(lineWithAngles());
                for (String line : lines) {
                    StringBuilder row = new StringBuilder();
                    row.append("|").append(" ".repeat(OFFSET));
                    row.append(line);
                    row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
                    view.append(row);
                    view.append(emptyLine());
                }
            }
        }
        view.append(lineWithAngles());
        return view.toString();
    }

    protected static class Window {
        StringBuilder view = new StringBuilder();

        public Window lineWithAngles() {
            view.append(Viewer.lineWithAngles());
            return this;
        }

        public Window emptyLine() {
            view.append(Viewer.emptyLine());
            return this;
        }

        public Window line() {
            view.append(Viewer.line());
            return this;
        }

        public Window line(String line) {
            StringBuilder row = new StringBuilder();
            row.append("|").append(" ".repeat(OFFSET));
            row.append(line);
            row.append(" ".repeat(ROW_SIZE - row.length() - 1)).append("|\n");
            add(row);
            return this;
        }

        public Window list(List<String> lines) {
            list(lines, true, 0, true, true);
            return this;
        }

        public Window list(List<String> lines, boolean enumerate, int from, boolean addEmptyLines, boolean hasBack) {
            int counter = from;
            if (hasBack) {
                line(String.format("%d. %s", counter, "Back"));
                counter++;
                if (addEmptyLines) {
                    emptyLine();
                }
            }
            for (String line : lines) {
                if (enumerate) {
                    line(String.format("%d. %s", counter, line));
                    counter++;
                } else {
                    line(line);
                }
                if (addEmptyLines) {
                    emptyLine();
                }
            }
            return this;
        }

        public Window add(Object s) {
            view.append(s);
            return this;
        }

        public Window clear() {
            view = new StringBuilder();
            return this;
        }

        public String getView() {
            return view.toString();
        }
    }
}

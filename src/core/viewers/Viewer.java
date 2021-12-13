package core.viewers;

import java.util.List;

import utils.Constants;

public class Viewer {
    private static final int ROW_SIZE = Constants.SHOP_VIEW_SIZE.value;
    private static final int OFFSET = Constants.MAP_OFFSET.value;

    protected static String emptyLine() {
        return line(" ", "|", "|", true);
    }

    protected static String emptyLine(int length) {
        return line(" ", "|", "|", length, true);
    }

    protected static String lineWithAngles() {
        return line("-", "+", "+", true);
    }

    protected static String lineWithAngles(int length) {
        return line("-", "+", "+", length, true);
    }

    protected static String line() {
        return line("-", "-", "-", true);
    }

    protected static String line(int length) {
        return line("-", "-", "-", length, true);
    }

    protected static String line(String sym, String leftAngle, String rightAngle, boolean endLine) {
        return leftAngle + sym.repeat(ROW_SIZE - 2) + rightAngle + (endLine ? "\n" : "");
    }

    protected static String line(String sym, String leftAngle, String rightAngle, int length, boolean endLine) {
        return leftAngle + sym.repeat(length - 2) + rightAngle + (endLine ? "\n" : "");
    }

    protected static class Window {
        private StringBuilder view = new StringBuilder();
        private int length = ROW_SIZE;
        private int offset = OFFSET;
        private int currentHeight = 0;

        public Window() {
        }

        public Window(int length, int offset) {
            this.length = length;
            this.offset = offset;
        }

        public Window lineWithAngles() {
            view.append(Viewer.lineWithAngles(length));
            currentHeight++;
            return this;
        }

        public Window emptyLine() {
            view.append(Viewer.emptyLine(length));
            currentHeight++;
            return this;
        }

        public Window line() {
            view.append(Viewer.line(length));
            currentHeight++;
            return this;
        }

        public Window line(String line) {
            StringBuilder row = new StringBuilder();
            line = line.replaceAll("\\$", "");
            row.append("|").append(" ".repeat(offset));
            row.append(line);
            row.append(" ".repeat(length - row.length() - 1)).append("|\n");
            add(row);
            currentHeight++;
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
            currentHeight = 0;
            return this;
        }

        public String getView() {
            return view.toString();
        }

        public int getCurrentHeight() {
            return currentHeight;
        }
    }
}

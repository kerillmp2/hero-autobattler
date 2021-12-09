package core.viewers;

import utils.Constants;

public class Viewer {
    private static final int ROW_SIZE = Constants.SHOP_VIEW_SIZE.value;

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
}

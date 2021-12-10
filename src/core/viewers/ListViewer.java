package core.viewers;

import java.util.List;
import java.util.stream.Collectors;

import utils.HasName;

public class ListViewer extends Viewer {

    public static String viewList(List<String> strings) {
        Window window = new Window();
        window.lineWithAngles();
        window.emptyLine();
        window.list(strings, true, 1, true, false);
        window.lineWithAngles();
        return window.getView();
    }

    public static String viewList(List<String> strings, boolean withBackOption) {
        Window window = new Window();
        window.lineWithAngles();
        window.emptyLine();
        window.list(strings, true, 0, true, withBackOption);
        window.lineWithAngles();
        return window.getView();
    }
}

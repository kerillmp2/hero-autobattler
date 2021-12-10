package core.viewers;

import java.util.List;
import java.util.stream.Collectors;

import utils.HasName;

public class TurnOptionsViewer extends Viewer {

    public static String getOptionsView(List<? extends HasName> options) {
        Window window = new Window();
        window.lineWithAngles();
        window.emptyLine();
        window.list(options.stream().map(HasName::getName).collect(Collectors.toList()), true, 0, true, false);
        window.lineWithAngles();
        return window.getView();
    }
}

package metatagger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

class LocalizeJTabbedPane extends JTabbedPane {
    int calculateMaxWidth() {
        int res = 0;
        FontMetrics fm = getFontMetrics(getFont());
        for (int i = 1; i <= 9; i++) {
            int width = fm.stringWidth(Util.getLabel(i + ""));
            if (width > res)
                res = width;
        }

        return res;
    }

    String completePaneLabel(String s) {
        int maxWidth = calculateMaxWidth();
        FontMetrics fm = getFontMetrics(getFont());
        while (fm.stringWidth(s) < maxWidth)
            s += " ";

        return s;
    }

    public String getTitleAt(int index) {
        String title = Util.getLabel(index + 1 + "");
        if (getTabPlacement() == NORTH && getSelectedIndex() != index)
            title = title.substring(0, 7);
        if (getTabPlacement() == LEFT)
            title = completePaneLabel(title);
        return title;
    }

    public String getToolTipText(MouseEvent event) {
        int index = indexAtLocation(event.getX(), event.getY());
        if (index != -1)
            return Util.getLabel(index + 1 + "");
        else
            return null;
    }
}
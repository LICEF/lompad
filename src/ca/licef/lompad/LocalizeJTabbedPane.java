/*
 * Copyright (C) 2005  Alexis Miara (alexis.miara@licef.ca)
 *
 * This file is part of LomPad.
 *
 * LomPad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LomPad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LomPad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.licef.lompad;

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

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 2004-06-23
 */
class JDialogAlert extends JDialog {

    public JDialogAlert(JFrame parent, String title, String text) {
        super(parent, title, true);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel jPanelNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        JLabel jLabelText = new JLabel(text);
        jLabelText.setFont(new Font("Dialog", Font.PLAIN, 12));
        jPanelNorth.add(jLabelText);
        cp.add(BorderLayout.NORTH, jPanelNorth);
        JPanel jPanelSouth = new JPanel(new FlowLayout());
        JButton ok = new JButton("ok");
        ok.setFont(jLabelText.getFont());
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        jPanelSouth.add(ok);
        cp.add(BorderLayout.SOUTH, jPanelSouth);
        setSize(250, 125);

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogAlertRes", Util.locale);
        setTitle(" " + resBundle.getString(getTitle()));
        jLabelText.setText(resBundle.getString(jLabelText.getText()));
        ok.setText(resBundle.getString(ok.getText()));
    }

    public void setVisible( boolean isVisible ) {
        if( isVisible ) {
            Rectangle bounds = getParent().getBounds();
            Rectangle abounds = getBounds();
            setLocation(bounds.x + (bounds.width - abounds.width) / 2,
                    bounds.y + (bounds.height - abounds.height) / 2);
        }
        super.setVisible( isVisible );
    }

}


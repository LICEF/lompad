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

/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 2004-11-25
 */
class JWindowAbout extends JWindow {

    private static final String revision        = "51";
    private static final String contactEmail    = "paloma@licef.ca";
    private static final String licefSite       = "http://www.licef.ca/";
    private static final String teluqSite       = "http://www.teluq.uquebec.ca/";
    private static final String cogigraphSite   = "http://www.cogigraph.com/";

    JPanel jPanelLicef;
    JPanel jPanelTeluq;
    JPanel jPanelCogigraph;

    JLabel jLabelBuild;
    JLabel jLabelContact;
    JLabel jLabelMail;

    boolean firstTime;

    public JWindowAbout(Frame owner, boolean firstTime) {
        super(owner);
        this.firstTime = firstTime;
        getRootPane().setLayout(null);
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(Util.imageAbout, 0, 0, this);
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(null);
        panel.setBounds(0, 0, 376, 314);
        getRootPane().add(panel);

        SymMouse aSymMouse = new SymMouse();
        this.addMouseListener(aSymMouse);

        jPanelLicef = new JPanel();
        jPanelLicef.setOpaque(false);
        jPanelLicef.setBounds(263, 226, 39, 41);
        jPanelLicef.addMouseListener(aSymMouse);
        panel.add(jPanelLicef);
        jPanelTeluq = new JPanel();
        jPanelTeluq.setOpaque(false);
        jPanelTeluq.setBounds(317, 231, 45, 41);
        jPanelTeluq.addMouseListener(aSymMouse);
        panel.add(jPanelTeluq);
        jPanelCogigraph = new JPanel();
        jPanelCogigraph.setOpaque(false);
        jPanelCogigraph.setBounds(59, 298, 188, 14);
        jPanelCogigraph.addMouseListener(aSymMouse);
        panel.add(jPanelCogigraph);

        jLabelBuild = new JLabel("(revision " + revision + ")");
        jLabelBuild.setFont(new Font("Dialog", Font.PLAIN, 10));
        jLabelBuild.setForeground(Color.LIGHT_GRAY);
        jLabelBuild.setBounds(260, 97, 150, 24);
        panel.add(jLabelBuild);

        jLabelContact = new JLabel("Contact:");
        jLabelContact.setFont(jLabelBuild.getFont());
        jLabelContact.setForeground(Color.CYAN);
        jLabelContact.setBounds(106, 270, 40, 24);
        panel.add(jLabelContact);

        jLabelMail = new JLabel(contactEmail);
        jLabelMail.setFont(jLabelBuild.getFont());
        jLabelMail.setForeground(Color.CYAN);
        jLabelMail.setBounds(150, 270, 200, 24);
        jLabelMail.addMouseListener(aSymMouse);
        panel.add(jLabelMail);
    }

    public void setVisible(boolean b) {
        if (b) {
            if (firstTime) {
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                setLocation((d.width - getSize().width) / 2,
                        (d.height - getSize().height) / 2);
            } else {
                Rectangle bounds = getParent().getBounds();
                Rectangle abounds = getBounds();
                setLocation(bounds.x + (bounds.width - abounds.width) / 2,
                        bounds.y + (bounds.height - abounds.height) / 2);
            }
        }
        super.setVisible(b);
    }

    class SymMouse extends java.awt.event.MouseAdapter {
        public void mouseEntered(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object == jPanelLicef || object == jPanelTeluq ||
                    object == jPanelCogigraph || object == jLabelMail)
                setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void mouseExited(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object == jPanelLicef || object == jPanelTeluq ||
                    object == jPanelCogigraph || object == jLabelMail)
                setCursor(Cursor.getDefaultCursor());
        }

        public void mousePressed(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object == jPanelLicef)
                Util.launchFile(licefSite);
            else if (object == jPanelTeluq)
                Util.launchFile(teluqSite);
            else if (object == jPanelCogigraph)
                Util.launchFile(cogigraphSite);
            else if (object == jLabelMail)
                Util.launchFile("mailto:" + jLabelMail.getText());
            dispose();
        }

        public void mouseReleased(java.awt.event.MouseEvent event) {
            dispose();
        }
    }
}

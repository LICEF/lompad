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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.ResourceBundle;


/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 2004-11-09
 */
class JDialogVCardEditor extends JDialog {

    JPanel jPanelContent;
    JPanel jPanelButton;
    JButton jButtonOk;
    JButton jButtonCancel;
    boolean bOk;
    JTextAreaPopup jTextAreaVCard;

    public JDialogVCardEditor(JFrame parent, String vcard) {
        super(parent, "title", true);

        setSize(600, 400);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(0, 5));

        jPanelContent = new JPanel() {
            public Insets getInsets() {
                return new Insets(10, 10, 10, 10);
            }
        };
        jPanelContent.setLayout(new BorderLayout(0, 5));
        cp.add(jPanelContent, BorderLayout.CENTER);

        jTextAreaVCard = new JTextAreaPopup(vcard);
        jTextAreaVCard.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        jPanelContent.add(new JScrollPane(jTextAreaVCard), BorderLayout.CENTER);
        jButtonOk = new JButton("OK");
        jButtonOk.setFont(new Font("Dialog", Font.PLAIN, 12));
        jButtonCancel = new JButton("cancel");
        jButtonCancel.setFont(jButtonOk.getFont());
        jPanelButton = new JPanel();
        jPanelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanelButton.add(jButtonOk);
        jPanelButton.add(jButtonCancel);
        cp.add(jPanelButton, BorderLayout.SOUTH);

        SymAction lSymAction = new SymAction();
        jButtonOk.addActionListener(lSymAction);
        jButtonCancel.addActionListener(lSymAction);

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogFormatSelectorRes", Preferences.getInstance().getLocale());
        setTitle("VCard");
        jButtonCancel.setText(resBundle.getString("cancel"));
    }

    public void setVisible(boolean b) {
        if (b) {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
        }
        super.setVisible(b);
    }

    public String getVCardValue() {
        return jTextAreaVCard.getText();
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jButtonOk)
                jButtonOk_actionPerformed();
            else if (object == jButtonCancel)
                jButtonCancel_actionPerformed();
        }
    }

    void jButtonOk_actionPerformed() {
        bOk = true;
        setVisible(false);
    }

    void jButtonCancel_actionPerformed() {
        setVisible(false);
    }


}





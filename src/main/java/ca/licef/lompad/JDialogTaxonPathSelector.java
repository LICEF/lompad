/*
 * Copyright (C) 2007 Alexis Miara (alexis.miara@licef.ca)
 * 
 * This file is part of LomPad.
 * 
 * PALOMA is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * PALOMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with PALOMA; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.licef.lompad;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class JDialogTaxonPathSelector extends JDialog {

    JPanel jPanelContent;
    JPanelTaxonomy jPanelTaxonomy;
    JPanel jPanelButton;
    JButton jButtonOk;
    JButton jButtonCancel;

    SymTree aSymTree;

    String source;
    Object[] taxonPath;

    boolean bOk = false;

    public JDialogTaxonPathSelector(Frame parent) {
        super(parent, "title", true);

        setSize(500, 400);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        jPanelContent = new JPanel() {
            public Insets getInsets() {
                return new Insets(5, 5, 5, 5);
            }
        };
        jPanelContent.setLayout(new BorderLayout());
        cp.add(jPanelContent, BorderLayout.CENTER);

        jPanelTaxonomy = new JPanelTaxonomy( this );
        jPanelContent.add(BorderLayout.CENTER, jPanelTaxonomy);

        jButtonOk = new JButton("OK");
        jButtonOk.setFont(new Font("Dialog", Font.PLAIN, 12));
        jButtonOk.setEnabled(false);
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

        SymMouse aSymMouse = new SymMouse();
        jPanelTaxonomy.jComboBoxClassification.addMouseListener(aSymMouse);
        aSymTree = new SymTree();
        for (Iterator it = jPanelTaxonomy.getTrees().iterator(); it.hasNext();)
            ((JTree) it.next()).addTreeSelectionListener(aSymTree);
    }

    void updateLocalization() {
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogTaxonPathSelectorRes", Util.locale);
        setTitle(resBundle.getString("title"));
        jButtonCancel.setText(resBundle.getString("cancel"));
    }

    public void setVisible(boolean b) {
        if (b) {
            bOk = false;
            updateLocalization();
        }
        jPanelTaxonomy.setVisible(b);
        super.setVisible(b);
    }

    void addTreeListener( JTree tree ) {
        tree.addTreeSelectionListener( aSymTree );
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
        int selectedIndex = jPanelTaxonomy.getSelectedIndex();
        ArrayList titles = jPanelTaxonomy.getTitles(selectedIndex);
        String url = jPanelTaxonomy.getURL(selectedIndex);
        String lang = Util.locale.getLanguage();
        if ("".equals(lang))
            lang = "en";
        int index = titles.indexOf(lang);
        if (index == -1) //first choice if language not defined
            index = 0;
        String title = (String) titles.get(index + 1);
        source = title + " - " + url;

        taxonPath = jPanelTaxonomy.getTaxonPath();
        bOk = true;
        setVisible(false);
    }

    void jButtonCancel_actionPerformed() {
        setVisible(false);
    }

    class SymTree implements TreeSelectionListener {
        public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
            update();
        }
    }

    class SymMouse extends MouseAdapter {
        public void mouseReleased(MouseEvent e) {
            Object object = e.getSource();
            if (object.equals(jPanelTaxonomy.jComboBoxClassification))
                update();
        }
    }

    void update() {
        JTree currentTree = jPanelTaxonomy.getCurrentTree();
        jButtonOk.setEnabled(currentTree != null && 
            currentTree.getSelectionPath() != null && currentTree.getSelectionPath().getPathCount() != 0);
    }
}

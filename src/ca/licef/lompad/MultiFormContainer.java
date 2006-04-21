/*
 * Copyright (C) 2005  Alexis Miara (amiara@licef.teluq.uquebec.ca)
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

import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Vector;

class MultiFormContainer extends JPanel {
    protected Vector vComponents;
    String formClassName;
    String label;
    int cpt = 0;

    JPanel jPanelGauche = new JPanel();
    JScrollPane jScrollPaneForms = new JScrollPane();
    DefaultListModel listModel = new DefaultListModel();
    JList jListForms = new JList(listModel);
    JPanel jPanelControls = new JPanel();
    JButton jButtonAdd = new JButton();
    JButton jButtonRemove = new JButton();
    JPanel jPanelCard = new JPanel();

    SymAction lSymAction;
    SymList lSymList;

    public MultiFormContainer(String formClassName, String label) {
        this.formClassName = formClassName;
        this.label = label;

        jListForms.setFont(new Font("Dialog", Font.PLAIN, 12));

        setLayout(new BorderLayout(2, 0));
        jPanelGauche.setLayout(new BoxLayout(jPanelGauche, BoxLayout.Y_AXIS));
        add(BorderLayout.WEST, jPanelGauche);
        jPanelGauche.add(jScrollPaneForms);
        jScrollPaneForms.setMinimumSize(new Dimension(110, 200));
        jScrollPaneForms.setPreferredSize(new Dimension(110, 200));
        jScrollPaneForms.setMaximumSize(new Dimension(110, 200));
        jScrollPaneForms.getViewport().add(jListForms);
        jPanelGauche.add(jScrollPaneForms);
        jPanelControls.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 5));
        jPanelGauche.add(jPanelControls);
        jButtonAdd.setIcon(Util.plusIcon);
        jButtonAdd.setFocusPainted(false);
        jButtonAdd.setBorderPainted(false);
        jButtonAdd.setMargin(new Insets(2, 2, 2, 4));
        jButtonAdd.setPreferredSize(new Dimension(24, 24));
        jPanelControls.add(jButtonAdd);
        jButtonRemove.setIcon(Util.minusIcon);
        jButtonRemove.setFocusPainted(false);
        jButtonRemove.setBorderPainted(false);
        jButtonRemove.setMargin(new Insets(2, 2, 2, 4));
        jButtonRemove.setPreferredSize(new Dimension(24, 24));
        jPanelControls.add(jButtonRemove);
        jPanelCard.setLayout(new CardLayout(0, 0));
        add(BorderLayout.CENTER, jPanelCard);

        vComponents = new Vector();
        lSymAction = new SymAction();
        jButtonAdd.addActionListener(lSymAction);
        jButtonRemove.addActionListener(lSymAction);
        lSymList = new SymList();
        jListForms.addListSelectionListener(lSymList);
        SymMouse aSymMouse = new SymMouse();
        jButtonAdd.addMouseListener(aSymMouse);
        jButtonRemove.addMouseListener(aSymMouse);
    }

    boolean isFilled() {
        return ((FormContainer) vComponents.firstElement()).isFilled();
    }

    public void setEnabled(boolean b) {
        jButtonAdd.setVisible(b);
        jButtonRemove.setVisible(b);
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();)
            ((FormContainer) e.nextElement()).setEnabled(b);
    }

    public void setFormVisible(String key, boolean isVisible) {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormContainer c = (FormContainer) e.nextElement();
            c.setFormVisible(key, isVisible);
        }
    }

    public void preUpdateVocabularies() {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();)
            ((FormContainer) e.nextElement()).preUpdateVocabularies();
    }

    public void updateVocabularies() {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();)
            ((FormContainer) e.nextElement()).updateVocabularies();
    }

    public boolean isComplete(String key) {
        boolean res = true;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormContainer c = (FormContainer) e.nextElement();
            res = res && c.isComplete(key);
            if (!res) break;
        }
        return res;
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jButtonAdd)
                addFormContent();
            else if (object == jButtonRemove)
                removeComponent();
        }
    }

    void addFormContent() {
        FormContainer form = null;
        try {
            Class formClass = Class.forName(formClassName);
            Constructor construct = formClass.getConstructor(new Class[]{String.class, Boolean.TYPE});
            form = (FormContainer) construct.newInstance(new Object[]{"", Boolean.FALSE});
        } catch (Exception e) {
        }

        JScrollPane jScrollPane = new JScrollPane();
        JPanel jPanelContent = new JPanel();
        jPanelContent.setLayout(new BorderLayout(0, 0));
        jScrollPane.getViewport().add(jPanelContent);
        jPanelContent.setBackground(LomForm.bgColor);

        form.addFormContent();
        jPanelContent.add(form);

        vComponents.addElement(form);

        cpt++;
        String entry = label + " #" + cpt;
        jPanelCard.add(entry, jScrollPane);

        listModel.addElement(entry);
        jListForms.setSelectedIndex(listModel.getSize() - 1);

        JPanelForm.instance.doFilter();
    }

    public void removeComponent() {
        int index = jListForms.getSelectedIndex();
        vComponents.removeElementAt(index);
        jPanelCard.remove(index);
        listModel.remove(index);
        jListForms.setSelectedIndex((listModel.getSize() > index) ? index : index - 1);
    }

    class SymList implements javax.swing.event.ListSelectionListener {
        public void valueChanged(javax.swing.event.ListSelectionEvent event) {
            Object object = event.getSource();
            if (object == jListForms)
                listValueChanged();
        }
    }

    void listValueChanged() {
        ((CardLayout) jPanelCard.getLayout()).show(jPanelCard, (String) jListForms.getSelectedValue());
        jButtonRemove.setEnabled(jListForms.getSelectedIndex() > 0);
    }

    void graphicalUpdate() {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();)
            ((FormContainer) e.nextElement()).graphicalUpdate();
    }

    class SymMouse extends java.awt.event.MouseAdapter {
        public void mouseEntered(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object instanceof JButton)
                jButton_mouseInOut((JButton) object, true);
        }

        public void mouseExited(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object instanceof JButton)
                jButton_mouseInOut((JButton) object, false);
        }
    }

    void jButton_mouseInOut(JButton jButton, boolean in) {
        jButton.setBorderPainted(in);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormContainer c = (FormContainer) e.nextElement();
            String res = c.toXML(key);
            if (res != null)
                xml += res;
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        FormContainer c = null;
        if (isFilled()) {
            addFormContent();
            c = (FormContainer) vComponents.lastElement();
        } else
            c = (FormContainer) vComponents.firstElement();

        c.fromXML(path, e);

        jListForms.setSelectedIndex(0);
    }

    //HTML
    String toHTML(String key) {
        String html = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormContainer c = (FormContainer) e.nextElement();
            String res = c.toHTML(key);
            if (res != null)
                html += res;
        }

        if (html.equals(""))
            html = null;

        return html;
    }
}

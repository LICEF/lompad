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

package metatagger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

abstract class FormContainer extends FormComponent {
    protected Vector vComponents;

    int offset;
    String title;
    boolean isLine;
    boolean isMultipleContainer;
    boolean alignRight = false;

    JToggleButton jToggleButtonHandle;
    JPanel jPanelGaucheContainer;
    JButton jButtonAdd;
    Hashtable removeMapping;
    SymAction lSymAction;
    SymMouse aSymMouse;
    int CONTROL_WIDTH = 24;
    int CONTROL_SPACE = 5;

    public FormContainer() {
        this(null, false, false);
    }

    public FormContainer(String title, boolean isLine) {
        this(title, isLine, false);
    }

    public FormContainer(boolean isMultipleContainer) {
        this(null, false, isMultipleContainer);
    }

    /**
     * Constructeur du FormContainer
     */
    public FormContainer(String title, boolean isLine, boolean isMultipleContainer) {
        super();
        vComponents = new Vector();

        this.title = title;
        this.isLine = isLine;
        this.isMultipleContainer = isMultipleContainer;
        lSymAction = new SymAction();
        aSymMouse = new SymMouse();

        offset = 5; //default value

        jPanelGaucheContainer = new JPanel();
        jPanelGaucheContainer.setOpaque(false);
        jPanelGaucheContainer.setLayout(new BoxLayout(jPanelGaucheContainer, BoxLayout.Y_AXIS));
        jPanelGauche.add(BorderLayout.CENTER, jPanelGaucheContainer);

        if (isMultipleContainer) {
            removeMapping = new Hashtable();
            jButtonAdd = new JButton(Util.plusIcon);
            jButtonAdd.setBorderPainted(false);
            jButtonAdd.setFocusPainted(false);
            jButtonAdd.setMargin(new Insets(1, 1, 1, 2));
            jButtonAdd.setPreferredSize(new Dimension(24, 24));
            jButtonAdd.setOpaque(false);
            jButtonAdd.addActionListener(lSymAction);
            jButtonAdd.addMouseListener(aSymMouse);
        }

        if (title != null) {
            LocalizeTitledBorder border =
                    isLine ? new LocalizeTitledBorder(title) :
                    new LocalizeTitledBorder(BorderFactory.createEmptyBorder(), title);
            border.setTitleFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
            jPanelContent.setBorder(border);
        }
    }

    void setIcon(ImageIcon icon) {
        ((IconTitledBorder)jPanelContent.getBorder()).setIcon(icon);
    }

    boolean isFilled() {
        boolean res = false;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            res = res || c.isFilled();
            if (res) break;
        }

        return res;
    }

    public void setEnabled(boolean b) {
        if (isMultipleContainer) {
            jButtonAdd.setVisible(b);
            for (Enumeration e = removeMapping.keys(); e.hasMoreElements();) {
                JButton button = (JButton) e.nextElement();
                button.setVisible(b);
            }
            if (b)
                jPanelControl.remove(0);
            else
                jPanelControl.add(Box.createHorizontalStrut(CONTROL_WIDTH));
        }

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();)
            ((FormComponent) e.nextElement()).setEnabled(b);
    }

    abstract void addFormContent();

    void addToggle() {
        jToggleButtonHandle = new JToggleButton();
        jToggleButtonHandle.setOpaque(false);
        jToggleButtonHandle.setSelected(true);
        jToggleButtonHandle.setPreferredSize(new Dimension(18, 16));
        jToggleButtonHandle.setBorder(new EmptyBorder(jToggleButtonHandle.getInsets()));
        jToggleButtonHandle.setIcon(Util.imageIconCollapse);
        jToggleButtonHandle.setSelectedIcon(Util.imageIconExpand);
        jToggleButtonHandle.setContentAreaFilled(false);
        jToggleButtonHandle.setFocusPainted(false);
        JPanel jPanelHandle = new JPanel();
        jPanelHandle.setOpaque(false);
        jPanelHandle.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jPanelHandle.add(jToggleButtonHandle);
        add(jPanelHandle, 0);
        add(Box.createHorizontalStrut(5), 1);
        jToggleButtonHandle.addActionListener(lSymAction);
        jPanelHandle.setAlignmentY(0.0F);
        jPanelContent.setAlignmentY(0.0F);
    }

    void addSpace(int space) {
        Component c = Box.createVerticalStrut(space);
        jPanelGaucheContainer.add(c);
    }

    public void setFormVisible(boolean b) {
        if (getSpace() != null)
            getSpace().setVisible(b);
        setVisible(b);
    }

    public void setFormVisibleEff(String key, boolean isVisible) {
        if (key.indexOf(".") == -1) {
            int pos = Integer.parseInt(key);
            FormContainer c = (FormContainer) vComponents.elementAt(pos - 1);
            c.setFormVisible(isVisible);
        } else {
            int pos = Integer.parseInt(key.substring(0, key.indexOf(".")));
            FormContainer c = (FormContainer) vComponents.elementAt(pos - 1);
            c.setFormVisible(key.substring(key.indexOf(".") + 1), isVisible);
        }
    }

    public void setFormVisible(String key, boolean isVisible) {
        if (isMultipleContainer) {
            for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
                FormContainer c1 = (FormContainer) e.nextElement();
                c1.setFormVisibleEff(key, isVisible);
            }
        } else
            setFormVisibleEff(key, isVisible);
    }

    public boolean isCompleteEff(String key) {
        if (key.indexOf(".") == -1) {
            int pos = Integer.parseInt(key);
            FormContainer c = (FormContainer) vComponents.elementAt(pos - 1);
            return c.isFilled();
        } else {
            int pos = Integer.parseInt(key.substring(0, key.indexOf(".")));
            FormContainer c = (FormContainer) vComponents.elementAt(pos - 1);
            return c.isComplete(key.substring(key.indexOf(".") + 1));
        }
    }

    public boolean isComplete(String key) {
        boolean res = true;
        if (isMultipleContainer) {
            for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
                FormContainer c1 = (FormContainer) e.nextElement();
                res = res && c1.isCompleteEff(key);
                if (!res) break;
            }
        } else
            res = isCompleteEff(key);
        return res;
    }

    public void updateContentVisible() {
        jPanelGaucheContainer.setVisible(jToggleButtonHandle.isSelected());
        jToggleButtonHandle.setPressedIcon(jToggleButtonHandle.isSelected() ?
                Util.imageIconExpand : Util.imageIconCollapse);
    }

    void setAlignRight() {
        alignRight = true;
    }

    void setEnabledButtonAdd(boolean b) {
        if (isMultipleContainer)
            jButtonAdd.setEnabled(b);
    }

    void update() {
        JComponent jc = SwingUtilities.getRootPane(this);
        if (jc != null) {
            jc.invalidate();
            jc.validate();
        }
    }

    FormComponent wrapIntoRemovableComponent(FormComponent c) {
        FormWrapper c2 = new FormWrapper();
        c2.mediator = c.mediator;
        c2.jPanelGaucheContainer.add(c);
        c2.jPanelControl.add(Box.createHorizontalStrut(CONTROL_SPACE));

        if (jPanelGaucheContainer.getComponentCount() == 0)
            c2.jPanelControl.add(jButtonAdd);
        else {
            JButton jButtonRemove = new JButton(Util.minusIcon);
            jButtonRemove.setBorderPainted(false);
            jButtonRemove.setFocusPainted(false);
            jButtonRemove.setMargin(new Insets(1, 1, 1, 2));
            jButtonRemove.setOpaque(false);
            jButtonRemove.setPreferredSize(new Dimension(24, 24));
            jButtonRemove.addActionListener(lSymAction);
            jButtonRemove.addMouseListener(aSymMouse);
            removeMapping.put(jButtonRemove, c2);
            c2.jPanelControl.add(jButtonRemove);
        }
        c2.setMinimumSize(c.getMinimumSize());
        c2.setPreferredSize(c.getPreferredSize());
        c2.setMaximumSize(c.getMaximumSize());
        return c2;
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jToggleButtonHandle)
                updateContentVisible();
            else if (object == jButtonAdd)
                addFormContent();
            else if (removeMapping.containsKey(object))
                removeComponent((FormContainer) removeMapping.get(object));
        }
    }

    public void addComponent(FormComponent c) {
        vComponents.addElement(c);

        if (isMultipleContainer)
            c = wrapIntoRemovableComponent(c);
        else if (alignRight)
            c.jPanelControl.add(Box.createHorizontalStrut(CONTROL_WIDTH + CONTROL_SPACE));

        c.setAlignmentX(0.0F);

        if (jPanelGaucheContainer.getComponentCount() != 0) {
            Component space = Box.createVerticalStrut(offset);
            c.setSpace(space);
            jPanelGaucheContainer.add(space);
        }
        jPanelGaucheContainer.add(c);

        update();

        JPanelForm.instance.doFilter();
    }

    public void removeComponent(FormContainer c) {
        jPanelGaucheContainer.remove(c.getSpace());
        jPanelGaucheContainer.remove(c);
        update();

        if (mediator != null)
            mediator.buttonRemoveComponentPerformed((FormComponent) c.jPanelGaucheContainer.getComponent(0));
        else
            vComponents.removeElement((FormComponent) c.jPanelGaucheContainer.getComponent(0));
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

    int getMinimumHeight() {
        int height = 0;

        if (jPanelGaucheContainer.isVisible()) {
            Component[] arrayComp = jPanelGaucheContainer.getComponents();
            for (int i = 0; i < arrayComp.length; i++) {
                if (arrayComp[i].isVisible()) {
                    if (arrayComp[i] instanceof FormContainer)
                        height += ((FormContainer) arrayComp[i]).getMinimumHeight();
                    else
                        height += arrayComp[i].getMinimumSize().height;
                }
            }
        }
        if (title != null) {
            height += isLine ? 10 : 8;
            if (!title.equals(""))
                height += 20;
            else if (jToggleButtonHandle != null && !jToggleButtonHandle.isSelected())
                height += 16;
        }

        return height;
    }

    public Dimension getMinimumSize() {
        return new Dimension(10, getMinimumHeight());
    }

    public Dimension getPreferredSize() {
        return new Dimension(10, getMinimumHeight());
    }

    public Dimension getMaximumSize() {
        return new Dimension(2000, getMinimumHeight());
    }

    void graphicalUpdate() {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();)
            ((FormComponent) e.nextElement()).graphicalUpdate();
    }

    //XML
    String toXML(String key) {
        String xml = "";

        int i = 1;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements(); i++) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = c.toXML(key + "." + i);
            if (res != null) xml += res;
        }

        if (!xml.equals(""))
            xml = "<" + Util.getTag(key) + ">\n" + xml + "</" + Util.getTag(key) + ">\n";
        else
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        NodeList list = e.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                String pathElem = path + "/" + child.getTagName().toLowerCase();
                try {
                    int pos = Util.getPosTag(pathElem);
                    FormComponent c = (FormComponent) vComponents.elementAt(pos - 1);
                    c.fromXML(pathElem, child);
                } catch (IllegalTagException ite) {
                }
            }
        }
    }
}
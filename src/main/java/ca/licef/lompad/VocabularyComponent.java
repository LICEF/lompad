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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import licef.CommonNamespaceContext;

class VocabularyComponent extends FormComponent {
    JComboBox jComboBoxVocabulary;
    OrderedValue previousVocabulary;
    boolean isEditable;
    boolean isLomVocabulary;

    String source = "LOMv1.0";

    public VocabularyComponent(ControlValueMediator mediator, boolean isLomVocabulary, boolean isEditable) {
        super(mediator);

        this.isLomVocabulary = isLomVocabulary;

        jComboBoxVocabulary = new JComboBox();
        jComboBoxVocabulary.setFont(new Font("Dialog", Font.PLAIN, 11));
        jPanelGauche.add(jComboBoxVocabulary);

        this.isEditable = isEditable;
        jComboBoxVocabulary.setEditable(isEditable);

        if (mediator != null) {
            for (Enumeration e = mediator.getAvailableValues().elements(); e.hasMoreElements();) {
                Object obj = e.nextElement();
                jComboBoxVocabulary.addItem(obj);
            }
            previousVocabulary = getSelectedValue();
        }
        SymAction lSymAction = new SymAction();
        jComboBoxVocabulary.addActionListener(lSymAction);
    }

    boolean isFilled() {
        return getSelectedValue().value != null && !getSelectedValue().value.toString().trim().equals("");
    }

    public void setEnabled(boolean b) {
        jComboBoxVocabulary.setEnabled(b);
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jComboBoxVocabulary)
                jComboBoxVocabulary_actionPerformed();
        }
    }

    void jComboBoxVocabulary_actionPerformed() {
        if (mediator != null) {
            if (!getSelectedValue().equals(previousVocabulary)) {
                ((ControlValueMediator) mediator).comboBoxValuePerformed(this);
                previousVocabulary = getSelectedValue();
            }
        }
    }

    public JComboBox getJComboBox() {
        return jComboBoxVocabulary;
    }

    public OrderedValue getSelectedValue() {
        Object obj = jComboBoxVocabulary.getSelectedItem();
        OrderedValue v = null;
        if (obj instanceof OrderedValue)
            v = (OrderedValue) obj;
        else
            v = new OrderedValue((String) obj, -1, false);

        return v;
    }

    public Object getMatchedValue(String s) {
        for (int i = 0; i < jComboBoxVocabulary.getItemCount(); i++) {
            Object o = jComboBoxVocabulary.getItemAt(i);
            if (((OrderedValue) o).toString().equals(s))
                return o;
        }
        return s;
    }

    public OrderedValue getPreviousSelectedValue() {
        return previousVocabulary;
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (isFilled()) {
            if (isLomVocabulary) {
                String value = null;
                value = getSelectedValue().value.toString();
                if (value.startsWith("x")) //profile vocabulary
                    source = Util.getProfileVocabularySource(value.substring(1, value.indexOf("-")));

                value = Util.getXMLVocabulary(value);

                xml = "<source>" + source + "</source>\n" +
                        "<value>" + value + "</value>\n";
            } else
                xml = Util.convertSpecialCharactersForXML(getSelectedValue().toString());
        }
        return xml;
    }

    void fromXML(String path, Element e, Hashtable tableImportXML, boolean firstField, List<String> observations) {
        if (isLomVocabulary) {
            NodeList listSrc = e.getElementsByTagNameNS(CommonNamespaceContext.lomNSURI,"source");
            Element childSrc = (Element) listSrc.item(0);
            String source = childSrc.getFirstChild().getNodeValue();

            NodeList list = e.getElementsByTagNameNS(CommonNamespaceContext.lomNSURI,"value");
            if( list.getLength() > 0 ) {
                Element child = (Element) list.item(0);
                String value = child.getFirstChild().getNodeValue().trim();

                try {
                    if (child.getFirstChild() != null) {
                        int pos = Util.getPosVocabulary(value, !source.equals("LOMv1.0"));
                        Integer index = (Integer) tableImportXML.remove(new Integer(pos));
                        if (index == null) return;

                        jComboBoxVocabulary.setSelectedIndex(index.intValue());
                        for (Enumeration en = tableImportXML.keys(); en.hasMoreElements();) {
                            Integer i = (Integer) en.nextElement();
                            int val = ((Integer) tableImportXML.get(i)).intValue();
                            if (firstField) val--;
                            if (i.intValue() > pos) val--;
                            tableImportXML.put(i, new Integer(val));
                        }
                    }
                } catch (IllegalTagException ite) {
                }
            }
        } else if (e.getFirstChild() != null)
            jComboBoxVocabulary.setSelectedItem(getMatchedValue(e.getFirstChild().getNodeValue().trim()));
    }

    //HTML
    String toHTML(String key) {
        String html = null;
        if (isFilled()) {
            if (isLomVocabulary)
                html = Util.getVocabulary(getSelectedValue().value.toString()) + "<br>";
            else
                html = getSelectedValue() + "<br>";
        }
        return html;
    }

}

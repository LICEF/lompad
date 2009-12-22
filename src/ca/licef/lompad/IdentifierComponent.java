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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;

class IdentifierComponent extends FormComponent {
    JPanel jPanelCatEntr;
    JPanel jPanelCatalog;
    JPanel jPanelEntry;
    LocalizeJLabel jLabelCatalog;
    LocalizeJLabel jLabelEntry;
    JTextFieldPopup jTextFieldCatalog;
    JTextFieldPopup jTextFieldEntry;

    public IdentifierComponent() {
        super(null);

        jPanelCatEntr = new JPanel();
        jPanelCatEntr.setOpaque(false);
        jPanelCatalog = new JPanel();
        jPanelCatalog.setOpaque(false);
        jPanelEntry = new JPanel();
        jPanelEntry.setOpaque(false);
        jLabelCatalog = new LocalizeJLabel("1.1.1");
        jLabelCatalog.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
        jLabelEntry = new LocalizeJLabel("1.1.2");
        jLabelEntry.setFont(jLabelCatalog.getFont());
        jTextFieldCatalog = new JTextFieldPopup();
        jTextFieldEntry = new JTextFieldPopup();

        jPanelCatEntr.setLayout(new GridLayout(1, 2, 5, 0));

        jPanelCatalog.setLayout(new BoxLayout(jPanelCatalog, BoxLayout.X_AXIS));
        jPanelCatalog.add(Box.createHorizontalStrut(5));
        jPanelCatalog.add(jLabelCatalog);
        jPanelCatalog.add(Box.createHorizontalStrut(5));
        jPanelCatalog.add(jTextFieldCatalog);
        jPanelCatEntr.add(jPanelCatalog);

        jPanelEntry.setLayout(new BoxLayout(jPanelEntry, BoxLayout.X_AXIS));
        jPanelCatalog.add(Box.createHorizontalStrut(5));
        jPanelEntry.add(jLabelEntry);
        jPanelEntry.add(Box.createHorizontalStrut(5));
        jPanelEntry.add(jTextFieldEntry);
        jPanelCatEntr.add(jPanelEntry);

        jPanelGauche.add(jPanelCatEntr);
    }

    boolean isFilled() {
        return !(jTextFieldCatalog.getText().trim().equals("") &&
                jTextFieldEntry.getText().trim().equals(""));
    }

    public void setEnabled(boolean b) {
        jTextFieldCatalog.setEditable(b);
        jTextFieldCatalog.setBackground(Color.white);
        jTextFieldEntry.setEditable(b);
        jTextFieldEntry.setBackground(Color.white);
    }


    //XML
    String toXML(String key) {
        String xml = "";
        if (!jTextFieldCatalog.getText().trim().equals(""))
            xml += "<" + Util.getTag(key + ".1") + ">" + Util.convertSpecialCharactersForXML(jTextFieldCatalog.getText().trim()) +
                    "</" + Util.getTag(key + ".1") + ">\n";
        if (!jTextFieldEntry.getText().trim().equals(""))
            xml += "<" + Util.getTag(key + ".2") + ">" + Util.convertSpecialCharactersForXML(jTextFieldEntry.getText().trim()) +
                    "</" + Util.getTag(key + ".2") + ">\n";

        if (xml.equals("")) xml = null;
        return xml;
    }

    void fromXML(String path, Element e) {
        NodeList list = e.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                if (child.getFirstChild() != null) {
                    String value = child.getFirstChild().getNodeValue();
                    try {
                        int pos = Util.getPosTag(path + "/" + child.getTagName().toLowerCase());
                        if (pos == 1) {
                            jTextFieldCatalog.setText(value);
                            jTextFieldCatalog.setCaretPosition(0);
                        }
                        if (pos == 2) {
                            jTextFieldEntry.setText(value);
                            jTextFieldEntry.setCaretPosition(0);
                        }
                    } catch (IllegalTagException ite) {
                    }
                }
            }
        }
    }

    //HTML
    String toHTML(String key) {
        String html = "";

        //cas particulier
        if (key.startsWith("7.2"))
            key += ".1";
        
        if (!jTextFieldCatalog.getText().trim().equals(""))
            html += Util.getLabel(key + ".1") + " : " + jTextFieldCatalog.getText().trim() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        if (!jTextFieldEntry.getText().trim().equals(""))
            html += Util.getLabel(key + ".2") + " : " + jTextFieldEntry.getText().trim() + "<br>";

        if (html.equals("")) html = null;
        return html;
    }
}

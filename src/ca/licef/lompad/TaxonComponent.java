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

import javax.swing.*;
import java.awt.*;

class TaxonComponent extends FormComponent {
    JPanel jPanelIdEntry;
    LocalizeJLabel jLabelId;
    LocalizeJLabel jLabelEntry;
    JTextFieldPopup jTextFieldId;
    JTextFieldPopup jTextFieldEntry;
    JComboBox jComboBoxLang;
    private boolean isFirst;

    public TaxonComponent(LangstringMediator mediator, boolean isFirst) {
        super(mediator);
        this.isFirst = isFirst;

        jPanelIdEntry = new JPanel();
        jPanelIdEntry.setLayout(new BoxLayout(jPanelIdEntry, BoxLayout.X_AXIS));
        jPanelIdEntry.setOpaque(false);
        jLabelId = new LocalizeJLabel("9.2.2.1");
        jLabelId.setFont(new Font("Dialog", Font.PLAIN, 12));
        jLabelEntry = new LocalizeJLabel("9.2.2.2");
        jLabelEntry.setFont(jLabelId.getFont());
        jTextFieldId = new JTextFieldPopup();
        jTextFieldId.setMinimumSize(new Dimension(50, 25));
        jTextFieldId.setPreferredSize(new Dimension(50, 25));
        jTextFieldId.setMaximumSize(new Dimension(50, 25));
        jTextFieldEntry = new JTextFieldPopup();
        jTextFieldEntry.setMinimumSize(new Dimension(50, 25));
        jTextFieldEntry.setPreferredSize(new Dimension(50, 25));
        jTextFieldEntry.setMaximumSize(new Dimension(1000, 25));

        if (this.isFirst) {
            jPanelIdEntry.add(Box.createHorizontalStrut(5));
            jPanelIdEntry.add(jLabelId);
            jPanelIdEntry.add(Box.createHorizontalStrut(5));
            jPanelIdEntry.add(jTextFieldId);
        } else {
            FontMetrics fm = getFontMetrics(jLabelId.getFont());
            jPanelIdEntry.add(Box.createHorizontalStrut(5 + fm.stringWidth(jLabelId.getText()) + 5 + 50));
        }
        jPanelIdEntry.add(Box.createHorizontalStrut(5));
        jPanelIdEntry.add(jLabelEntry);
        jPanelIdEntry.add(Box.createHorizontalStrut(5));
        jPanelIdEntry.add(jTextFieldEntry);

        jPanelGauche.add(jPanelIdEntry);

        jComboBoxLang = new JComboBox();
        jComboBoxLang.setEditable(true);
        jPanelControl.add(Box.createHorizontalStrut(5));
        jPanelControl.add(jComboBoxLang);
        jComboBoxLang.setFont(new Font("Dialog", Font.PLAIN, 10));

        Object[] values = mediator.getValues();
        for (int i = 0; i < values.length; i++)
            jComboBoxLang.addItem(values[i]);

        mediator.computePreferredSize(jComboBoxLang);
        jComboBoxLang.setPreferredSize(mediator.getComboBoxPreferredSize());
    }

    boolean isFilled() {
        return !jTextFieldEntry.getText().trim().equals("");
    }

    public void setEnabled(boolean b) {
        jTextFieldId.setEditable(b);
        jTextFieldId.setBackground(Color.white);
        jTextFieldEntry.setEditable(b);
        jTextFieldEntry.setBackground(Color.white);
        jComboBoxLang.setEnabled(b);
    }


    //XML
    String toXML_Id(String key) {
        String xml = "";
        if (!jTextFieldId.getText().trim().equals(""))
            xml += "<" + Util.getTag(key + ".1") + ">" +
                    Util.convertSpecialCharactersForXML(jTextFieldId.getText().trim()) +
                    "</" + Util.getTag(key + ".1") + ">\n";
        if (xml.equals("")) xml = null;
        return xml;
    }

    String toXML_Entry() {
        String xml = "";

        if (!jTextFieldEntry.getText().trim().equals("")) {
            String lang = "";
            String selectedItem = (String)jComboBoxLang.getSelectedItem();
            if (selectedItem != null && !"".equals(selectedItem))
                lang = " language=\"" + selectedItem + "\"";
            xml = "<string" + lang + ">" +
                    Util.convertSpecialCharactersForXML(jTextFieldEntry.getText().trim()) + "</string>";
        }
        if (xml.equals("")) xml = null;
        return xml;
    }

    void fromXML_Id(Element e) {
        String value = e.getFirstChild().getNodeValue();
        jTextFieldId.setText(value);
        jTextFieldId.setCaretPosition(0);
    }

    void fromXML_Entry(Element e) {
        String value = e.getFirstChild().getNodeValue();
        jTextFieldEntry.setText(value);
        jTextFieldEntry.setCaretPosition(0);
        jComboBoxLang.setSelectedItem(e.getAttribute("language"));
    }

    //HTML
    String toHTML(String key) {
        String html = "";

        if (isFirst && !jTextFieldId.getText().trim().equals(""))
            html += jTextFieldId.getText().trim();

        if (!jTextFieldEntry.getText().trim().equals("")) {
            String lang = "";
            if (jComboBoxLang.getSelectedItem() != null && !"".equals(jComboBoxLang.getSelectedItem()))
                lang = " (" + jComboBoxLang.getSelectedItem() + ")";

            if (!html.equals(""))
                html += ":" + jTextFieldEntry.getText().trim() + lang;
            else
                html = jTextFieldEntry.getText().trim() + lang;
        }

        if (html.equals(""))
            html = null;

        return html;
    }
}

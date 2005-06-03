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

class EntityComponent extends FormComponent {
    JPanel jPanelEntity;
    LocalizeJLabel jLabelName;
    LocalizeJLabel jLabelEMail;
    LocalizeJLabel jLabelOrg;

    JTextFieldPopup jTextFieldName;
    JTextFieldPopup jTextFieldEMail;
    JTextFieldPopup jTextFieldOrg;

    public EntityComponent(boolean isFirst) {
        super(null);

        jPanelEntity = new JPanel();
        jPanelEntity.setOpaque(false);
        jTextFieldName = new JTextFieldPopup();
        jTextFieldEMail = new JTextFieldPopup();
        jTextFieldOrg = new JTextFieldPopup();

        jPanelEntity.setLayout(new GridLayout(isFirst ? 2 : 1, 3, 0, 0));
        if (isFirst) {
            jLabelName = new LocalizeJLabel("name");
            jLabelName.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
            jLabelEMail = new LocalizeJLabel("email");
            jLabelEMail.setFont(jLabelName.getFont());
            jLabelOrg = new LocalizeJLabel("org");
            jLabelOrg.setFont(jLabelName.getFont());
            jPanelEntity.add(jLabelName);
            jPanelEntity.add(jLabelEMail);
            jPanelEntity.add(jLabelOrg);
        }
        jPanelEntity.add(jTextFieldName);
        jPanelEntity.add(jTextFieldEMail);
        jPanelEntity.add(jTextFieldOrg);

        jPanelGauche.add(jPanelEntity);
    }

    boolean isFilled() {
        return !(jTextFieldName.getText().equals("") &&
                jTextFieldEMail.getText().equals("") &&
                jTextFieldOrg.getText().equals(""));
    }

    public void setEnabled(boolean b) {
        jTextFieldName.setEditable(b);
        jTextFieldName.setBackground(Color.white);
        jTextFieldEMail.setEditable(b);
        jTextFieldEMail.setBackground(Color.white);
        jTextFieldOrg.setEditable(b);
        jTextFieldOrg.setBackground(Color.white);
    }

    //XML
    String toXML(String key) {
        String vCard = "";
        if (!jTextFieldName.getText().equals(""))
            vCard += "FN:" + jTextFieldName.getText() + "\n";
        if (!jTextFieldEMail.getText().equals(""))
            vCard += "EMAIL;TYPE=INTERNET:" + jTextFieldEMail.getText() + "\n";
        if (!jTextFieldOrg.getText().equals(""))
            vCard += "ORG:" + jTextFieldOrg.getText() + "\n";

        if (vCard.equals(""))
            vCard = null;
        else
            vCard = "BEGIN:VCARD\nVERSION:3.0\n" + vCard + "END:VCARD";
        return vCard;
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() == null) return;

        String vCard = e.getFirstChild().getNodeValue();
        boolean b = true;
        while (b) {
            int index = vCard.indexOf("\n");
            if (index == -1) return;
            String token = vCard.substring(0, index);
            if (token.startsWith("FN:")) {
                jTextFieldName.setText(token.substring(token.indexOf(":") + 1));
                jTextFieldName.setCaretPosition(0);
            }
            if (token.startsWith("EMAIL")) {
                jTextFieldEMail.setText(token.substring(token.indexOf(":") + 1));
                jTextFieldEMail.setCaretPosition(0);
            }
            if (token.startsWith("ORG:")) {
                jTextFieldOrg.setText(token.substring("ORG:".length()));
                jTextFieldOrg.setCaretPosition(0);
            }

            vCard = vCard.substring(index + 1);
            if (vCard.startsWith("END:")) b = false;
        }
    }

    //HTML
    String toHTML(String key) {
        String html = "";
        if (!jTextFieldName.getText().equals(""))
            html += jTextFieldName.getText();
        if (!jTextFieldOrg.getText().equals(""))
            html += (html.equals("")?"":"; ") + jTextFieldOrg.getText();
        if (!jTextFieldEMail.getText().equals(""))
            html += (html.equals("")?"":"; ") + "<i>" + jTextFieldEMail.getText() + "</i>";

        if (html.equals(""))
            html = null;
        else
            html += "<br>";

        return html;
    }
}

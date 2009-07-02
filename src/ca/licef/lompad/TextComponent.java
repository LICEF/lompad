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
import javax.swing.text.JTextComponent;
import java.awt.*;

class TextComponent extends FormComponent {
    JScrollPane jScrollPaneTextArea;
    JTextComponent currentJTextComponent;

    public TextComponent(FormMediator mediator, boolean isOneLine) {
        super(mediator);

        if (isOneLine) {
            currentJTextComponent = new JTextFieldPopup();
            jPanelGauche.add(currentJTextComponent);
        } else {
            jScrollPaneTextArea = new JScrollPane();
            jPanelGauche.add(BorderLayout.CENTER, jScrollPaneTextArea);
            currentJTextComponent = new JTextAreaPopup();
            ((JTextAreaPopup) currentJTextComponent).setLineWrap(true);
            jScrollPaneTextArea.getViewport().add(currentJTextComponent);
        }
    }

    boolean isFilled() {
        return !currentJTextComponent.getText().equals("");
    }

    public void setEnabled(boolean b) {
        currentJTextComponent.setEditable(b);
        currentJTextComponent.setBackground(Color.white);
    }

    void updateAfterAdded() {
        currentJTextComponent.requestFocus();
    }

    void setNumericTextField(Object[] numericParam) {
        jPanelGauche.removeAll();
        currentJTextComponent =
                new NumericTextField(((Integer) numericParam[0]).intValue(),
                        (String) numericParam[1], (String) numericParam[2]);
        jPanelGauche.add(currentJTextComponent);
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (!currentJTextComponent.getText().equals(""))
            xml = Util.convertSpecialCharactersForXML(currentJTextComponent.getText());
        return xml;
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() != null) {
            currentJTextComponent.setText(e.getFirstChild().getNodeValue().trim());
            currentJTextComponent.setCaretPosition(0);
        }
    }

    //HTML
    String toHTML(String key) {
        String html = null;
        if (isFilled()) {
            html = currentJTextComponent.getText();
            //cas particulier
            if ("4.3".equals(key))
                html = "<a href=\"" + html + "\" target=\"_blank\">" + html + "</a>";
            if ("9.2.1".equals(key))
                html = Util.getLabel(key) + " : " + html;

            html += "<br>";
            if (currentJTextComponent instanceof JTextArea) {
                html = html.replaceAll("\n", "<br>");
                html += "<br>";
            }

        }

        return html;
    }
}

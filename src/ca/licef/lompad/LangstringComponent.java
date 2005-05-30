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

class LangstringComponent extends TextComponent {
    JComboBox jComboBoxLang;
    OrderedValue previousLang;

    public LangstringComponent(LangstringMediator mediator, boolean isOneLine) {
        super(mediator, isOneLine);

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

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        jComboBoxLang.setEnabled(b);
        jComboBoxLang.setEditable(b);
    }                                                                  

    public Object getSelectedValue() {
        return jComboBoxLang.getSelectedItem();
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (isFilled()) {
            String lang = "";
            if (getSelectedValue() != null && !"".equals(getSelectedValue()))
                    lang = " language=\"" + getSelectedValue() + "\"";
            xml = "<string" + lang + ">" +
                    Util.convertSpecialCharactersForXML(currentJTextComponent.getText()) + "</string>" + "\n";
        }
        return xml;
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() != null) {
            super.fromXML(path, e);
            jComboBoxLang.setSelectedItem(e.getAttribute("language"));
        }
    }
}
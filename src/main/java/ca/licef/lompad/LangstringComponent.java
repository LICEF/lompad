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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

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
        jComboBoxLang.getEditor().getEditorComponent().addKeyListener(
            new KeyAdapter() {
                public void keyReleased( KeyEvent e ) {
                    jComboBoxLang.setSelectedItem( jComboBoxLang.getEditor().getItem() );
                }
            }
        );

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

    public void clear() {
        super.clear();
        jComboBoxLang.setSelectedItem(null);
    }

    void setValues(String text, String language) {
        setValue(text);
        jComboBoxLang.setSelectedItem(language);
    }

    public Object getSelectedValue() {
        return jComboBoxLang.getSelectedItem();
    }

    //XML
    String toXML(String key) {
        String xml = null;
        if (isFilled()) {
            String lang = "";
            if (getSelectedValue() != null && !"".equals(getSelectedValue().toString().trim()))
                    lang = " language=\"" + getSelectedValue().toString().trim() + "\"";
            xml = "<string" + lang + ">" +
                    Util.convertSpecialCharactersForXML(currentJTextComponent.getText().trim()) + "</string>" + "\n";
        }
        return xml;
    }

    void fromXML(String path, Element e, List<String> observations) {
        if (e.getFirstChild() != null) {
            super.fromXML(path, e, observations);
            jComboBoxLang.setSelectedItem(e.getAttribute("language"));
        }
    }
}

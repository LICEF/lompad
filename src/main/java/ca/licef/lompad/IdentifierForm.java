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

import java.awt.*;
import java.util.Enumeration;
import java.util.List;

class IdentifierForm extends FormContainer {
    public IdentifierForm(String title, boolean isLine, boolean isMultipleContainer) {
        super(title, isLine, isMultipleContainer);
        offset = 2;
    }

    void addFormContent() {
        IdentifierComponent c = new IdentifierComponent();
        c.setMinimumSize(new Dimension(10, 25));
        c.setPreferredSize(new Dimension(10, 25));
        c.setMaximumSize(new Dimension(2000, 25));
        addComponent(c);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = c.toXML(key);
            if (res != null)
                xml += "<" + Util.getTag(key) + ">\n" + res + "</" + Util.getTag(key) + ">\n";
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e, List<String> observations) {
        IdentifierComponent c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (IdentifierComponent) vComponents.lastElement();
        } else
            c = (IdentifierComponent) vComponents.firstElement();

        c.fromXML(path, e, observations);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLRow(key);
    }
}

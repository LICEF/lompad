/*
 * Copyright (C) 2005  Alexis Miara (amiara@licef.ca)
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

import java.awt.*;

class LangstringForm extends TextForm {
    Object[] values = new Object[]{null, "fr", "en", "es"};

    public LangstringForm(String title, boolean isLine, boolean isMultipleContainer, boolean isOneLine) {
        super(title, isLine, isMultipleContainer, isOneLine);
        mediator = new LangstringMediator(this, values);
    }

    void addFormContent() {
        LangstringComponent c = new LangstringComponent((LangstringMediator) mediator, isOneLine);
        int height = isOneLine ? 25 : 60;
        c.setMinimumSize(new Dimension(10, height));
        c.setPreferredSize(new Dimension(10, height));
        c.setMaximumSize(new Dimension(2000, height));
        addComponent(c);

        mediator.buttonAddComponentPerformed(c);
    }

    //XML
    String toXML(String key) {
        return defaultToXML(key);
    }

    void fromXML(String path, Element e) {
        NodeList list = e.getElementsByTagName("string");
        for (int i = 0; i < list.getLength(); i++) {
            Element child = (Element) list.item(i);
            LangstringComponent c = null;
            if (isFilled()) {
                if (!isMultipleContainer) return; //pas d'import multiple possible
                addFormContent();
                c = (LangstringComponent) vComponents.lastElement();
            } else
                c = (LangstringComponent) vComponents.firstElement();

            c.fromXML(path, child);
        }
    }
}

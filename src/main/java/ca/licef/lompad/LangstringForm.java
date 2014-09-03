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

import java.awt.*;
import java.util.List;

import licef.CommonNamespaceContext;

class LangstringForm extends TextForm {
    public static Object[] values = new Object[]{null, "none", "fra", "fra-CA", "fra-FR", "eng", "eng-AU", "eng-CA", "eng-GB", "eng-US", "deu", "esl", "ita", "por", null, "fr", "fr-CA", "fr-FR", "en", "en-AU", "en-CA", "en-GB", "en-US", "de", "es", "it", "pt" };

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

    public void setValues(String[] values) {
        for (int i = 0; i < values.length;) {
            setValues(values[i], values[i + 1]);
            i += 2;
        }
    }

    public void setValues(String value, String language) {
        LangstringComponent c = null;
        if (isFilled()) {
            addFormContent();
            c = (LangstringComponent) vComponents.lastElement();
        } else
            c = (LangstringComponent) vComponents.firstElement();
        c.setValues(value, language);
    }

    //XML
    String toXML(String key) {
        return defaultToXML(key);
    }

    void fromXML(String path, Element e, List<String> observations) {
        NodeList list = e.getElementsByTagNameNS(CommonNamespaceContext.lomNSURI,"string");
        for (int i = 0; i < list.getLength(); i++) {
            Element child = (Element) list.item(i);
            LangstringComponent c = null;
            if (isFilled()) {
                if (!isMultipleContainer) return; //pas d'import multiple possible
                addFormContent();
                c = (LangstringComponent) vComponents.lastElement();
            } else
                c = (LangstringComponent) vComponents.firstElement();

            c.fromXML(path, child, observations);
        }
    }
}

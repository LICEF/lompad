/*
 * Copyright (C) 2005  Alexis Miara (amiara@licef..ca)
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

import java.awt.*;
import java.util.Enumeration;

class OrCompositeForm extends FormContainer {
    public OrCompositeForm(boolean isMultiple) {
        super(isMultiple);
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper("4.4.1", true);
        wrapper.setIcon(Util.greenIcon);
        wrapper.addToggle();

        OrCompositeComponent c = new OrCompositeComponent();
        c.setMinimumSize(new Dimension(10, 100));
        c.setPreferredSize(new Dimension(10, 100));
        c.setMaximumSize(new Dimension(2000, 100));
        wrapper.addComponent(c);

        addComponent(wrapper);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toXML(key, false);
            if (res != null)
                xml += "<" + Util.getTag(key) + ">\n" + res + "</" + Util.getTag(key) + ">\n";
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        NodeList list = e.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                String pathElem = path + "/" + child.getTagName().toLowerCase();
                FormWrapper c = null;
                try {
                    int pos = Util.getPosTag(pathElem); //pour être sur que
                    if (isFilled()) {
                        if (!isMultipleContainer) return; //pas d'import multiple possible
                        addFormContent();
                        c = (FormWrapper) vComponents.lastElement();
                    } else
                        c = (FormWrapper) vComponents.firstElement();

                    c.fromXML(pathElem, child, false);
                } catch (IllegalTagException ite) {
                }

            }
        }
    }

    //HTML
    String toHTMLData(String key) {
        String html = "";
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res =(c instanceof FormContainer)?
                    c.toHTMLData(key):c.toHTML(key);
            if (res != null)
                html += (html.equals("") ? "" :
                    ("fr".equals(Util.resBundleLabel.getLocale().getLanguage())?"OU":"OR") +
                    "<br>") + res;
        }

        if (html.equals(""))
            html = null;

        return html;
    }
}

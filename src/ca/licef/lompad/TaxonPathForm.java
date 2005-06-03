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

import java.util.Enumeration;

class TaxonPathForm extends FormContainer {
    public TaxonPathForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper("", true);
        wrapper.addToggle();

        LangstringForm source = new LangstringForm("9.2.1", false, true, true);
        source.setIcon(Util.redIcon);
        source.addFormContent();
        wrapper.addComponent(source);

        wrapper.addSpace(5);

        TaxonForm taxon = new TaxonForm(null, false, true);
        taxon.addFormContent();
        wrapper.addComponent(taxon);

        addComponent(wrapper);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toXML(key, true);
            if (res != null)
                xml += "<" + Util.getTag(key) + ">\n" + res + "</" + Util.getTag(key) + ">\n";
        }

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        FormWrapper c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (FormWrapper) vComponents.lastElement();
        } else
            c = (FormWrapper) vComponents.firstElement();

        c.fromXML(path, e, true);
    }

    //HTML
    String toHTML(String key) {
        String html = "";
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toHTML(key, true);
            if (res != null) html += (html.equals("")?"":"<br>") + res;
        }

        if (!html.equals(""))
            html = "<TR><TD WIDTH=\"140\" VALIGN=\"TOP\"><B>" + Util.getLabel(key)+ "</B></TD>" +
                    "<TD VALIGN=\"TOP\">" + html + "</TD></TR>";
        else
            html = null;

        return html;
    }
}

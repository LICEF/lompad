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

import java.util.Enumeration;

class MultiLangstringForm extends FormContainer {
    boolean isOneLine;
    String titleLocal;
    boolean isLineLocal;

    public MultiLangstringForm(String title, boolean isLine, boolean isMultiple, boolean isOneLine, String titleLocal, boolean isLineLocal) {
        super(title, isLine, isMultiple);
        this.isOneLine = isOneLine;
        this.titleLocal = titleLocal;
        this.isLineLocal = isLineLocal;
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper(titleLocal, isLineLocal);

        LangstringForm langstringForm = new LangstringForm(null, false, true, isOneLine);
        langstringForm.addFormContent();
        wrapper.addComponent(langstringForm);

        addComponent(wrapper);
    }

    //XML
    String toXML(String key) {
        String xml = "";
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toXML(key, false);
            if (res != null) xml += res;
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

        c.fromXML(path, e, false);
    }

    //HTML
    String toHTML(String key) {
        String html = "";
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toHTML(key, false);
            if (res != null)
                html += res;
        }

        if (!html.equals(""))
            html = "<TR><TD WIDTH=\"160\" VALIGN=\"TOP\"><B>" + Util.getLabel(key) + "</B></TD>" +
                    "<TD VALIGN=\"TOP\">" + html + "</TD></TR>";
        else
            html = null;

        return html;
    }
}

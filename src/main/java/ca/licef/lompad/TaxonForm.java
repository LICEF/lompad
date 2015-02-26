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
import java.util.List;

class TaxonForm extends FormContainer {
    public TaxonForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        FormWrapper wrapper = new FormWrapper("9.2.2", true);
        wrapper.setIcon(Util.redIcon);
        wrapper.addToggle();

        TaxonElementForm taxonElementForm = new TaxonElementForm();
        taxonElementForm.addFormContent();
        wrapper.addComponent(taxonElementForm);

        addComponent(wrapper);
    }

    public void setTaxon(Object[] taxon) {
        TaxonElementForm c = null;
        if (isFilled()) {
            addFormContent();
            c = (TaxonElementForm)
                    (((FormWrapper) vComponents.lastElement()).vComponents.firstElement());
        } else
            c = (TaxonElementForm)
                    (((FormWrapper) vComponents.firstElement()).vComponents.firstElement());
        c.setValues(taxon);
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

    void fromXML(String path, Element e, List<String> observations) {
        FormWrapper c = null;
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (FormWrapper) vComponents.lastElement();
        } else
            c = (FormWrapper) vComponents.firstElement();

        c.fromXML(path, e, false, observations);
    }

    //HTML
    String toHTMLData(String key) {
        String html = "";
        int i = 0;
        int j = -1;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements(); i++) {
            FormComponent c = (FormComponent) e.nextElement();
            String res =(c instanceof FormContainer)?
                    c.toHTMLData(key):c.toHTML(key);
            if (res != null) {
                html += doTab((i == 0) ? 0 : 13 + j*10) + res;
                j++;
            }
        }
        if (html.equals(""))
            html = null;

        return html;
    }

    String doTab(int n) {
        String res = "";
        for (int i = 0; i < n; i++)
            res += "&nbsp;";
        return res;
    }
}

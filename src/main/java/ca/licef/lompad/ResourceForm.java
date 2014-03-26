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

import java.util.Enumeration;

class ResourceForm extends FormContainer {
    public ResourceForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    void addFormContent() {
        IdentifierForm identifier = new IdentifierForm("7.2.1", true, true);
        identifier.setIcon(Util.yellowIcon);
        identifier.addToggle();
        identifier.addFormContent();
        addComponent(identifier);

        MultiLangstringForm description = new MultiLangstringForm("7.2.2", true, true, false, "", true);
        description.setIcon(Util.greenIcon);
        description.addToggle();
        description.addFormContent();
        addComponent(description);
    }

    //HTML
    String toHTML(String key) {
        String html = "";
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = c.toHTMLData(key);
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

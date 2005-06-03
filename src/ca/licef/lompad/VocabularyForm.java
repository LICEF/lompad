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

import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

class VocabularyForm extends FormContainer {
    Object[] values = null;
    boolean isLomVocabulary = true;
    boolean isEditable = false;
    Hashtable tableImportXML;

    public VocabularyForm(String title, boolean isLine, boolean isMultipleContainer, Object[] values, Object[] exclusiveValues, boolean isLomVocabulary) {
        super(title, isLine, isMultipleContainer);
        this.values = values;
        this.isLomVocabulary = isLomVocabulary;
        mediator = new ControlValueMediator(this, values, exclusiveValues);
    }

    void setEditable(boolean b) {
        isEditable = b;
    }

    void addFormContent() {
        VocabularyComponent c = new VocabularyComponent((ControlValueMediator) mediator, isLomVocabulary, isEditable);
        c.setMinimumSize(new Dimension(10, 25));
        c.setPreferredSize(new Dimension(10, 25));
        c.setMaximumSize(new Dimension(2000, 25));
        addComponent(c);

        mediator.buttonAddComponentPerformed(c);
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

    void fromXML(String path, Element e) {
        if (e.getFirstChild() == null) return;

        boolean firstField = false;
        VocabularyComponent c = (VocabularyComponent) vComponents.firstElement();
        if (isFilled()) {
            if (!isMultipleContainer) return; //pas d'import multiple possible
            addFormContent();
            c = (VocabularyComponent) vComponents.lastElement();
        } else {
            if (isLomVocabulary) {
                tableImportXML = new Hashtable();
                for (int i = 1; i < values.length; i++)
                    tableImportXML.put(new Integer(i), new Integer(i));
                firstField = true;
            }

            c = (VocabularyComponent) vComponents.firstElement();
        }
        c.fromXML(path, e, tableImportXML, firstField);
    }

    //HTML
    String toHTML(String key) {
        return toHTMLRow(key);
    }
}

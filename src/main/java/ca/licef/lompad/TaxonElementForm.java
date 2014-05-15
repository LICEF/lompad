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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import licef.CommonNamespaceContext;

class TaxonElementForm extends FormContainer {
    Object[] values = new Object[]{null, "fr", "en", "es"};

    public TaxonElementForm() {
        super(true);
        mediator = new LangstringMediator(this, values);
        offset = 2;
    }

    void addFormContent() {
        TaxonComponent c = new TaxonComponent((LangstringMediator) mediator, vComponents.size() == 0);
        c.setMinimumSize(new Dimension(10, 25));
        c.setPreferredSize(new Dimension(10, 25));
        c.setMaximumSize(new Dimension(2000, 25));
        addComponent(c);
    }

    public void setValues(Object[] taxon) {
        String id = (String) taxon[0];
        ArrayList titles = (ArrayList) taxon[1];

        //reorganisation with prefs's locale first.
        String lang = Preferences.getInstance().getLocale().getLanguage();
        if ("".equals(lang))
            lang = "en";
        int index = titles.indexOf(lang);
        if (index != -1) {//lang exists
            String entry = (String) titles.remove(index + 1);
            titles.remove(index);
            titles.add(0, lang);
            titles.add(1, entry);
        }

        for (Iterator it = titles.iterator(); it.hasNext();) {
            lang = (String) it.next();
            String entry = (String) it.next();
            TaxonComponent c = null;
            if (isFilled()) {
                addFormContent();
                c = (TaxonComponent) vComponents.lastElement();
            } else
                c = (TaxonComponent) vComponents.firstElement();
            c.setValues(id, entry, lang);
        }
    }

    //XML
    String toXML(String key) {
        String xml = "";
        String xml_Id = null;
        String xml_Entry = "";

        int i = 0;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements(); i++) {
            TaxonComponent c = (TaxonComponent) e.nextElement();
            if (i == 0)
                xml_Id = c.toXML_Id(key);

            String res = c.toXML_Entry();
            if (res != null)
                xml_Entry += res + "\n";
        }

        if (xml_Id != null)
            xml += xml_Id;

        if (!xml_Entry.equals(""))
            xml += "<" + Util.getTag(key + ".2") + ">" + xml_Entry +
                    "</" + Util.getTag(key + ".2") + ">\n";

        if (xml.equals(""))
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e, List<String> observations) {
        NodeList list = e.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                String pathElem = path + "/" + child.getTagName().toLowerCase();
                try {
                    TaxonComponent c = null;
                    int pos = Util.getPosTag(pathElem);
                    c = (TaxonComponent) vComponents.firstElement();
                    if (pos == 1) //Id
                        c.fromXML_Id(child);
                    if (pos == 2) //Entry
                    {
                        if (isFilled()) {
                            addFormContent();
                            c = (TaxonComponent) vComponents.lastElement();
                        }

                        NodeList list2 = child.getElementsByTagNameNS(CommonNamespaceContext.lomNSURI,"string");
                        for (int i2 = 0; i2 < list2.getLength(); i2++) {
                            Node node2 = list2.item(i2);
                            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                                Element child2 = (Element) node2;
                                if (c == null) {
                                    addFormContent();
                                    c = (TaxonComponent) vComponents.lastElement();
                                }
                                c.fromXML_Entry(child2);
                                c = null;
                            }
                        }
                    }
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
            String res = (c instanceof FormContainer) ?
                    c.toHTMLData(key) : c.toHTML(key);
            if (res != null) {
                if (!"".equals(html))
                    html += "; ";
                html += res;
            }
        }

        if (html.equals(""))
            html = null;
        else
            html += "<br>";

        return html;
    }
}

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.awt.*;
import java.util.*;
import java.io.ByteArrayInputStream;

class VocabularyForm extends FormContainer {
    Object[] values = null;
    Object[] exclusiveValues = null;
    boolean isLomVocabulary = true;
    boolean isEditable = false;
    Hashtable tableImportXML;

    public VocabularyForm(String title, boolean isLine, boolean isMultipleContainer, Object[] exclusiveValues, boolean isLomVocabulary) {
        super(title, isLine, isMultipleContainer);
        this.exclusiveValues = exclusiveValues;
        this.isLomVocabulary = isLomVocabulary;
        initVocabularyValues();
        mediator = new ControlValueMediator(this, values, exclusiveValues);
    }

    public VocabularyForm(String title, boolean isLine, boolean isMultipleContainer, Object[] values, Object[] exclusiveValues, boolean isLomVocabulary) {
        super(title, isLine, isMultipleContainer);
        this.values = values;
        this.exclusiveValues = exclusiveValues;
        this.isLomVocabulary = isLomVocabulary;
        mediator = new ControlValueMediator(this, values, exclusiveValues);
    }

    void initVocabularyValues() {
        ArrayList listVocab = new ArrayList();
        listVocab.add(null); //first value
        fillVocabularies(Util.resBundleVocabulary, title, listVocab);

        //external vocabularies
        String selectedProfile = JPanelForm.instance.getCurrentSelectedProfile();
        if (!selectedProfile.equals("IEEE")) {
            try {
                if (Util.externalProfile != null)
                    fillVocabularies(Util.resBundleProfileVocabulary, "x" + title, listVocab);
            } catch (Exception e) {
            }
        }
        values = listVocab.toArray();
    }

    void setEditable(boolean b) {
        isEditable = b;
    }

    String currentXML;
    public void preUpdateVocabularies() {
        currentXML = toXML(title);
    }

    public void updateVocabularies() {
        initVocabularyValues();
        mediator = new ControlValueMediator(this, values, exclusiveValues);

        //clean previous values
        vComponents.clear();
        jPanelGaucheContainer.removeAll();

        //reinit
        addFormContent();
        if (currentXML != null) {
            currentXML = "<vocabularies>" + currentXML + "</vocabularies>";
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setCoalescing(true); //convert CDATA node to Text node
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new ByteArrayInputStream(currentXML.getBytes()));

                NodeList list = document.getDocumentElement().getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;
                        fromXML(null, e);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    void addFormContent() {
        FormComponent c;
        if (isControlValueMediatorNeeded)
            c = new VocabularyComponent((ControlValueMediator) mediator, isLomVocabulary, isEditable);
        else
            c = new ExternalVocabularyComponent();

        c.setMinimumSize(new Dimension(10, 25));
        c.setPreferredSize(new Dimension(10, 25));
        c.setMaximumSize(new Dimension(2000, 25));
        addComponent(c);
        if (isControlValueMediatorNeeded)
            mediator.buttonAddComponentPerformed(c);
    }

    void removeFirstComponent() {
        FormComponent c = (FormComponent)jPanelGaucheContainer.getComponent(0);
        jPanelGaucheContainer.remove(c);
        if (isMultipleContainer)
            c = (FormComponent) ((FormContainer)c).jPanelGaucheContainer.getComponent(0);
        vComponents.removeElement(c);
    }

    void fillVocabularies(ResourceBundle resBundle, String prefixKey, ArrayList listVocab) {
        int i = 1;
        //is key exists in properties file ?
        while (true) {
            try {
                String key = prefixKey + "-" + i;
                resBundle.getString(key);
                listVocab.add(key);
                i++;
            } catch (MissingResourceException e) {
                break;
            }
        }
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

    boolean isControlValueMediatorNeeded = true;

    void fromXML(String path, Element e) {
            if (e.getFirstChild() == null) return;

            boolean firstField = false;
            FormComponent c = (FormComponent) vComponents.firstElement();
            isControlValueMediatorNeeded = isControlValueMediatorNeeded(e);

            if (isFilled()) {
                if (!isMultipleContainer) return; //pas d'import multiple possible
                addFormContent();
                c = (FormComponent) vComponents.lastElement();
            } else {
                if (isLomVocabulary) {
                    tableImportXML = new Hashtable();
                    for (int i = 1; i < values.length; i++)
                        tableImportXML.put(new Integer(i), new Integer(i));

                    if (!isControlValueMediatorNeeded) {
                        //permut first component with an extenal vocabulary component
                        removeFirstComponent();
                        c = new ExternalVocabularyComponent();
                        c.setMinimumSize(new Dimension(10, 25));
                        c.setPreferredSize(new Dimension(10, 25));
                        c.setMaximumSize(new Dimension(2000, 25));
                        addComponent(c);
                        if (isMultipleContainer)
                            setEnabledButtonAdd(true);
                    }

                    firstField = true;
                }

                c = (FormComponent) vComponents.firstElement();
            }

            if (isControlValueMediatorNeeded)
                ((VocabularyComponent)c).fromXML(path, e, tableImportXML, firstField);
            else
                ((ExternalVocabularyComponent)c).fromXML(path, e, firstField);

        isControlValueMediatorNeeded = true;
    }

    private boolean isControlValueMediatorNeeded(Element e) {
        if (!isLomVocabulary)
            return true;
        NodeList listSrc = e.getElementsByTagName("source");
        Element childSrc = (Element) listSrc.item(0);
        String source = childSrc.getFirstChild().getNodeValue();
        if (source.equals("LOMv1.0"))
            return true;
        //external source supported ?
        String profile = Util.getExternalProfileFromVocabularySource(source);
        String selectedProfile = JPanelForm.instance.getCurrentSelectedProfile();
        return (profile != null && profile.equals(selectedProfile));
    }

    //HTML
    String toHTML(String key) {
        return toHTMLRow(key);
    }
}

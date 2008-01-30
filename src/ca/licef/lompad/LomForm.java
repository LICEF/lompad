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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

class LomForm extends JPanel {
    static Color bgColor;

    LocalizeJTabbedPane jTabbedPaneLom = new LocalizeJTabbedPane();
    JPanel jPanelOngletGeneral = new JPanel();
    JScrollPane jScrollPaneGeneral = new JScrollPane();
    JPanel jPanelGeneral = new JPanel();
    JPanel jPanelOngletLifeCycle = new JPanel();
    JScrollPane jScrollPaneLifeCycle = new JScrollPane();
    JPanel jPanelLifeCycle = new JPanel();
    JPanel jPanelOngletMetaMetadata = new JPanel();
    JScrollPane jScrollPaneMetaMetadata = new JScrollPane();
    JPanel jPanelMetaMetadata = new JPanel();
    JPanel jPanelOngletTechnical = new JPanel();
    JScrollPane jScrollPaneTechnical = new JScrollPane();
    JPanel jPanelTechnical = new JPanel();
    JPanel jPanelOngletEducational = new JPanel();
    JPanel jPanelOngletRights = new JPanel();
    JScrollPane jScrollPaneRights = new JScrollPane();
    JPanel jPanelRights = new JPanel();
    JPanel jPanelOngletRelation = new JPanel();
    JPanel jPanelOngletAnnotation = new JPanel();
    JPanel jPanelOngletClassification = new JPanel();

    protected Vector vComponents;

    String lastLom;
    String newLom;

    public LomForm() {
        Util.initImageIcon(getClass());

        bgColor = getBackground();
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        add(BorderLayout.CENTER, jTabbedPaneLom);

        jTabbedPaneLom.setOpaque(false);
        jTabbedPaneLom.setFocusable(false);
        //jTabbedPaneLom.setTabPlacement(JTabbedPane.LEFT);
        jTabbedPaneLom.setFont(new Font("Dialog", Font.PLAIN, 12));

        jPanelOngletGeneral.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletGeneral);
        jPanelOngletGeneral.add(BorderLayout.CENTER, jScrollPaneGeneral);
        jPanelGeneral.setAlignmentX(0.0F);
        jPanelGeneral.setLayout(new BoxLayout(jPanelGeneral, BoxLayout.Y_AXIS));
        jScrollPaneGeneral.getViewport().add(jPanelGeneral);
        jPanelGeneral.setBackground(bgColor);

        jPanelOngletLifeCycle.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletLifeCycle);
        jPanelOngletLifeCycle.add(BorderLayout.CENTER, jScrollPaneLifeCycle);
        jPanelLifeCycle.setAlignmentX(0.0F);
        jPanelLifeCycle.setLayout(new BoxLayout(jPanelLifeCycle, BoxLayout.Y_AXIS));
        jScrollPaneLifeCycle.getViewport().add(jPanelLifeCycle);
        jPanelLifeCycle.setBackground(bgColor);

        jPanelOngletMetaMetadata.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletMetaMetadata);
        jPanelOngletMetaMetadata.add(BorderLayout.CENTER, jScrollPaneMetaMetadata);
        jPanelMetaMetadata.setAlignmentX(0.0F);
        jPanelMetaMetadata.setLayout(new BoxLayout(jPanelMetaMetadata, BoxLayout.Y_AXIS));
        jScrollPaneMetaMetadata.getViewport().add(jPanelMetaMetadata);
        jPanelMetaMetadata.setBackground(bgColor);

        jPanelOngletTechnical.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletTechnical);
        jPanelOngletTechnical.add(BorderLayout.CENTER, jScrollPaneTechnical);
        jPanelTechnical.setAlignmentX(0.0F);
        jPanelTechnical.setLayout(new BoxLayout(jPanelTechnical, BoxLayout.Y_AXIS));
        jScrollPaneTechnical.getViewport().add(jPanelTechnical);
        jPanelTechnical.setBackground(bgColor);

        jPanelOngletEducational.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletEducational);
        jPanelOngletEducational.setBackground(bgColor);

        jPanelOngletRights.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletRights);
        jPanelOngletRights.add(BorderLayout.CENTER, jScrollPaneRights);
        jPanelRights.setAlignmentX(0.0F);
        jPanelRights.setLayout(new BoxLayout(jPanelRights, BoxLayout.Y_AXIS));
        jScrollPaneRights.getViewport().add(jPanelRights);
        jPanelRights.setBackground(bgColor);

        jPanelOngletRelation.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletRelation);
        jPanelOngletRelation.setBackground(bgColor);

        jPanelOngletAnnotation.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletAnnotation);
        jPanelOngletAnnotation.setBackground(bgColor);

        jPanelOngletClassification.setLayout(new BorderLayout(0, 0));
        jTabbedPaneLom.add(jPanelOngletClassification);
        jPanelOngletClassification.setBackground(bgColor);

        jTabbedPaneLom.setSelectedIndex(0);

        for (int i = 0; i < 9; i++) {
            jTabbedPaneLom.setTitleAt(i, "");
            jTabbedPaneLom.setToolTipTextAt(i, "");
        }
    }

    void init() {
        vComponents = new Vector();

        jPanelGeneral.removeAll();
        initGeneral();
        jPanelLifeCycle.removeAll();
        initLifeCycle();
        jPanelMetaMetadata.removeAll();
        initMetaMetadata();
        jPanelTechnical.removeAll();
        initTechnical();
        jPanelOngletEducational.removeAll();
        initEducational();
        jPanelRights.removeAll();
        initRights();
        jPanelOngletRelation.removeAll();
        initRelation();
        jPanelOngletAnnotation.removeAll();
        initAnnotation();
        jPanelOngletClassification.removeAll();
        initClassification();

        initiateHasChanged();
    }

    void update() {
        jTabbedPaneLom.updateUI();
    }

    void initGeneral() {
        GeneralForm general = new GeneralForm("", false);
        general.addFormContent();
        jPanelGeneral.add(general);
        vComponents.addElement(general);
    }

    void initLifeCycle() {
        LifeCycleForm lifeCycle = new LifeCycleForm("", false);
        lifeCycle.addFormContent();
        jPanelLifeCycle.add(lifeCycle);
        vComponents.addElement(lifeCycle);
    }

    void initMetaMetadata() {
        MetaMetadataForm metaMetadata = new MetaMetadataForm("", false);
        metaMetadata.addFormContent();
        jPanelMetaMetadata.add(metaMetadata);
        vComponents.addElement(metaMetadata);
    }

    void initTechnical() {
        TechnicalForm technical = new TechnicalForm("", false);
        technical.addFormContent();
        jPanelTechnical.add(technical);
        vComponents.addElement(technical);
    }

    void initEducational() {
        MultiFormContainer multiEducational = new MultiFormContainer(EducationalForm.class.getName(), "educational");
        multiEducational.addFormContent();
        jPanelOngletEducational.add(multiEducational);
        vComponents.addElement(multiEducational);
    }

    void initRights() {
        RightsForm rights = new RightsForm("", false);
        rights.addFormContent();
        jPanelRights.add(rights);
        vComponents.addElement(rights);
    }

    void initRelation() {
        MultiFormContainer multiRelation = new MultiFormContainer(RelationForm.class.getName(), "relation");
        multiRelation.addFormContent();
        jPanelOngletRelation.add(multiRelation);
        vComponents.addElement(multiRelation);
    }

    void initAnnotation() {
        MultiFormContainer multiAnnotation = new MultiFormContainer(AnnotationForm.class.getName(), "annotation");
        multiAnnotation.addFormContent();
        jPanelOngletAnnotation.add(multiAnnotation);
        vComponents.addElement(multiAnnotation);
    }

    void initClassification() {
        MultiFormContainer multiClassification = new MultiFormContainer(ClassificationForm.class.getName(), "classification");
        multiClassification.addFormContent();
        jPanelOngletClassification.add(multiClassification);
        vComponents.addElement(multiClassification);
    }

    void setFormVisible(String key, boolean isVisible) {
        int index = key.indexOf(".");
        if (index == -1) //categorie
        {
            int val = Integer.parseInt(key);
            jTabbedPaneLom.setEnabledAt(val - 1, isVisible);
        } else //sous element
        {
            int pos = Integer.parseInt(key.substring(0, index));
            Object c = vComponents.elementAt(pos - 1);
            if (c instanceof FormContainer)
                ((FormContainer) c).setFormVisible(key.substring(index + 1), isVisible);
            if (c instanceof MultiFormContainer)
                ((MultiFormContainer) c).setFormVisible(key.substring(index + 1), isVisible);
        }

        if (!jTabbedPaneLom.isEnabledAt(jTabbedPaneLom.getSelectedIndex()))
            jTabbedPaneLom.setSelectedIndex(0);
    }

    boolean isComplete(String key) {
        boolean res = false;
        int index = key.indexOf(".");
        int pos = Integer.parseInt(key.substring(0, index));
        Object c = vComponents.elementAt(pos - 1);
        if (c instanceof FormContainer)
            res = ((FormContainer) c).isComplete(key.substring(index + 1));
        if (c instanceof MultiFormContainer)
            res = ((MultiFormContainer) c).isComplete(key.substring(index + 1));
        return res;
    }

    public void setEnabled(boolean b) {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            Object c = e.nextElement();
            if (c instanceof FormContainer)
                ((FormContainer) c).setEnabled(b);
            if (c instanceof MultiFormContainer)
                ((MultiFormContainer) c).setEnabled(b);
        }
    }

    public void preUpdateVocabularies() {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            Object c = e.nextElement();
            if (c instanceof FormContainer)
                ((FormContainer) c).preUpdateVocabularies();
            if (c instanceof MultiFormContainer)
                ((MultiFormContainer) c).preUpdateVocabularies();
        }
    }

    public void updateVocabularies() {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            Object c = e.nextElement();
            if (c instanceof FormContainer)
                ((FormContainer) c).updateVocabularies();
            if (c instanceof MultiFormContainer)
                ((MultiFormContainer) c).updateVocabularies();
        }
    }

    void initiateHasChanged() {
        toXML();
        lastLom = newLom;
    }

    boolean hasChanged() {
        toXML();
        return !newLom.equals(lastLom);
    }

    void graphicalUpdate() {
        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            Object c = e.nextElement();
            if (c instanceof FormContainer)
                ((FormContainer) c).graphicalUpdate();
            if (c instanceof MultiFormContainer)
                ((MultiFormContainer) c).graphicalUpdate();
        }
    }

    //XML
    public String toXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
        xml += "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchemainstance\" " +
                "xsi:schemaLocation=\"http://ltsc.ieee.org/xsd/LOM " +
                "http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd\">\n";

        int i = 1;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements(); i++) {
            Object c = e.nextElement();
            String res = null;
            if (c instanceof FormContainer)
                res = ((FormContainer) c).toXML(i + "");
            if (c instanceof MultiFormContainer)
                res = ((MultiFormContainer) c).toXML(i + "");

            if (res != null)
                xml += res;
        }

        xml += "</lom>\n";

        newLom = xml;

        return xml;
    }

    void fromXML(InputStream is) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setCoalescing(true); //convert CDATA node to Text node
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);

        NodeList list = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                try {
                    String tagName = e.getTagName().toLowerCase();
                    int pos = Util.getPosTag(tagName);
                    Object c = vComponents.elementAt(pos - 1);
                    if (c instanceof FormContainer)
                        ((FormContainer) c).fromXML(tagName, e);
                    if (c instanceof MultiFormContainer)
                        ((MultiFormContainer) c).fromXML(tagName, e);
                } catch (IllegalTagException ite) {
                }
            }
        }
    }

    //HTML
    public String toHTML() {
        String html = "<html><head>";

        html += "<style TYPE=\"text/css\">"+
                "<!-- p, td, ul, li, ol, textarea {" +
                "font-family: arial, sans-serif;" +
                "font-weight: normal;" +
                "color: #000000;" +
                "font-size: 12px; --></style>";

        html += "</head><body BGCOLOR=\"white\"><DIV ALIGN=\"CENTER\">";
        int i = 1;
        for (Enumeration e = vComponents.elements(); e.hasMoreElements(); i++) {
            Object c = e.nextElement();
            String res = null;
            if (c instanceof FormContainer)
                res = ((FormContainer) c).toHTML(i + "");
            if (c instanceof MultiFormContainer)
                res = ((MultiFormContainer) c).toHTML(i + "");

            if (res != null)
                html += res;
        }

        html += "</DIV></body></html>\n";

        return html;
    }
}

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

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

class TaxonPathForm extends FormContainer {
    public TaxonPathForm(String title, boolean isLine, boolean isMultiple) {
        super(title, isLine, isMultiple);
    }

    public void clearFormIcon() {
        // Always keep the icon for this special element.
        // It's hard-coded for Normetic.  Should be cleaner eventually.
    }

    void addFormContent() {
        FormWrapperTP wrapper = new FormWrapperTP("", true);
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

    class FormWrapperTP extends FormWrapper {
        JButton jButtonWizard;
        Component horizontalStrut;

        public FormWrapperTP(String title, boolean isLine) {
            super(title, isLine);
            jButtonWizard = new JButton(Util.wizardIcon);
            jButtonWizard.setFont(new Font("Dialog", Font.PLAIN, 12));
            jButtonWizard.setFocusPainted(false);
            jButtonWizard.setBorderPainted(false);
            jButtonWizard.setPreferredSize(new Dimension(23, 23));
            horizontalStrut = Box.createHorizontalStrut(5);
            jPanelDroite.add(jButtonWizard);
            jButtonWizard.addActionListener(new SymAction());
            jButtonWizard.addMouseListener(new SymMouse());
            add(horizontalStrut);
        }

        public void updateContentVisible() {
            super.updateContentVisible();
            jButtonWizard.setVisible(jToggleButtonHandle.isSelected());
        }

        public void setEnabled(boolean b) {
            super.setEnabled(b);
            horizontalStrut.setVisible(b);
            jButtonWizard.setVisible(b);
        }

        void setValues(String src, Object[] taxonPath) {
            LangstringForm source = (LangstringForm) vComponents.firstElement();
            String lang = Util.locale.getLanguage();
            if ("".equals(lang))
                lang = "en";
            source.setValues(src, lang);
            TaxonForm taxonForm = (TaxonForm) vComponents.lastElement();
            for (int i = 0; i < taxonPath.length; i++) {
                Object[] taxon = (Object[]) taxonPath[i];
                taxonForm.setTaxon(taxon);
            }
        }

        class SymAction implements java.awt.event.ActionListener {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                Object object = event.getSource();
                if (object.equals(jButtonWizard))
                    jButtonWizard_actionPerformed(event);
            }
        }

        private Object[] selectClassificationPath() {
            JDialogTaxonPathSelector jDialog = new JDialogTaxonPathSelector(Util.getTopJFrame(this));
            jDialog.setVisible(true);
            Object taxonPath = null;
            String source = null;
            if (jDialog.bOk) {
                source = jDialog.source;
                taxonPath = jDialog.taxonPath;
            }
            jDialog.dispose();
            return new Object[]{source, taxonPath};
        }

        void jButtonWizard_actionPerformed(java.awt.event.ActionEvent event) {
            Object[] res = selectClassificationPath();
            if (res[0] != null) {
                clear();
                setValues((String)res[0], (Object[])res[1]);
            }
        }
    };

    void addValues(String src, Object[] taxonPath) {
        FormWrapperTP c = null;
        if (isFilled()) {
            addFormContent();
            c = (FormWrapperTP) vComponents.lastElement();
        } else
            c = (FormWrapperTP) vComponents.firstElement();
        c.setValues(src, taxonPath);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        for (Enumeration e = vComponents.elements(); e.hasMoreElements();) {
            FormComponent c = (FormComponent) e.nextElement();
            String res = ((FormWrapper) c).toXML(key, true);
            if (res != null)
                xml += "<" + Util.getTag(key) + ">" + res + "</" + Util.getTag(key) + ">\n";
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

        c.fromXML(path, e, true, observations);
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
            html = "<TR><TD WIDTH=\"160\" VALIGN=\"TOP\"><B>" + Util.getLabel(key) + "</B></TD>" +
                    "<TD VALIGN=\"TOP\">" + html + "</TD></TR>";
        else
            html = null;

        return html;
    }
}

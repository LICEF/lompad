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

import javax.swing.*;
import java.awt.*;

abstract class FormComponent extends JComponent {
    JPanel jPanelContent;
    JPanel jPanelGauche;
    JPanel jPanelDroite;
    JPanel jPanelControl;

    Component space;
    FormMediator mediator;

    FormComponent() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        jPanelContent = new JPanel();
        jPanelGauche = new JPanel();
        jPanelDroite = new JPanel();
        jPanelControl = new JPanel();

        jPanelContent.setOpaque(false);
        jPanelContent.setLayout(new BoxLayout(jPanelContent, BoxLayout.X_AXIS));

        jPanelGauche.setOpaque(false);
        jPanelGauche.setLayout(new BorderLayout(0, 0));

        jPanelDroite.setOpaque(false);
        jPanelDroite.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 2));

        jPanelControl.setOpaque(false);
        jPanelControl.setLayout(new BoxLayout(jPanelControl, BoxLayout.X_AXIS));
        jPanelDroite.add(jPanelControl);

        jPanelContent.add(jPanelGauche);
        jPanelContent.add(jPanelDroite);
        add(jPanelContent);
    }

    FormComponent(FormMediator mediator) {
        this();
        this.mediator = mediator;
    }

    abstract boolean isFilled();

    public void preUpdateVocabularies() {
    }

    public void updateVocabularies() {
    }

    Component getSpace() {
        return space;
    }

    void setSpace(Component space) {
        this.space = space;
    }

    void updateAfterAdded() {
    }

    void graphicalUpdate() {
    }

    //XML
    String toXML(String key) {
        return "feuille\n";
    }

    void fromXML(String path, Element e) {
        System.out.println("feuille");
    }

    //HTML
    String toHTML(String key) {
        return "feuille";
    }

    String toHTMLData(String key) {
        return "feuille";
    }
}

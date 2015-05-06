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
import info.clearthought.layout.TableLayout; 

class EntityComponent extends FormComponent {

    String vCard = null;

    JPanel jPanelEntity;

    LocalizeJLabel jLabelGivenName;
    LocalizeJLabel jLabelFamilyName;
    LocalizeJLabel jLabelEMail;
    LocalizeJLabel jLabelOrg;

    JTextFieldPopup jTextFieldGivenName;
    JTextFieldPopup jTextFieldFamilyName;
    JTextFieldPopup jTextFieldEMail;
    JTextFieldPopup jTextFieldOrg;

    JPanel jPanelVCard;
    JButton jButtonVCard;

    public EntityComponent(boolean isFirst) {
        super(null);

        jPanelEntity = new JPanel();
        jPanelEntity.setOpaque(false);
        jTextFieldGivenName = new JTextFieldPopup();
        jTextFieldFamilyName = new JTextFieldPopup();
        jTextFieldEMail = new JTextFieldPopup();
        jTextFieldOrg = new JTextFieldPopup();

        jButtonVCard = new JButton(Util.vcardIcon);
        jButtonVCard.setBorderPainted(false);
        jButtonVCard.setFocusPainted(false);
        jButtonVCard.setPreferredSize(new Dimension(28, 23));

        jPanelVCard = new JPanel();
        jPanelVCard.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        jPanelVCard.add(jButtonVCard);

        double xBorder = 2;
        double yBorder = 0;
        double shortGap = 2;
        double largeGap = 10;
        double sectionGap = 20;
        double sizeForFirst[][] = { 
            { xBorder, 0.23, shortGap, 0.23, shortGap, 0.23, shortGap, 0.23, shortGap, 0.06 },
            { yBorder, 0.5, shortGap, 0.5, yBorder } 
        };
        double sizeForOther[][] = {
            { xBorder, 0.23, shortGap, 0.23, shortGap, 0.23, shortGap, 0.23, shortGap, 0.06 },
            { yBorder, TableLayout.FILL, yBorder } 
        };
        jPanelEntity.setLayout( new TableLayout( isFirst ? sizeForFirst : sizeForOther ) );

        if( isFirst ) {
            java.awt.Font labelFont = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
            jLabelGivenName = new LocalizeJLabel("givenName");
            jLabelGivenName.setFont(labelFont);
            jLabelFamilyName = new LocalizeJLabel("familyName");
            jLabelFamilyName.setFont(labelFont);
            jLabelEMail = new LocalizeJLabel("email");
            jLabelEMail.setFont(labelFont);
            jLabelOrg = new LocalizeJLabel("org");
            jLabelOrg.setFont(labelFont);

            jPanelEntity.add( jLabelGivenName,          "1, 1" );
            jPanelEntity.add( jLabelFamilyName,         "3, 1" );
            jPanelEntity.add( jLabelEMail,              "5, 1" );
            jPanelEntity.add( jLabelOrg,                "7, 1" );

            jPanelEntity.add( jTextFieldGivenName,      "1, 3" );
            jPanelEntity.add( jTextFieldFamilyName,     "3, 3" );
            jPanelEntity.add( jTextFieldEMail,          "5, 3" );
            jPanelEntity.add( jTextFieldOrg,            "7, 3" );
            jPanelEntity.add( jPanelVCard,              "9, 3" );
        }
        else {
            jPanelEntity.add( jTextFieldGivenName,      "1, 1" );
            jPanelEntity.add( jTextFieldFamilyName,     "3, 1" );
            jPanelEntity.add( jTextFieldEMail,          "5, 1" );
            jPanelEntity.add( jTextFieldOrg,            "7, 1" );
            jPanelEntity.add( jPanelVCard,              "9, 1" );
        }
        jPanelGauche.add(jPanelEntity);

        SymAction lSymAction = new SymAction();
        jButtonVCard.addActionListener(lSymAction);
        SymMouse aSymMouse = new SymMouse();
        jButtonVCard.addMouseListener(aSymMouse);
    }

    boolean isFilled() {
        return !(jTextFieldGivenName.getText().trim().equals("") &&
                jTextFieldFamilyName.getText().trim().equals("") &&
                jTextFieldEMail.getText().trim().equals("") &&
                jTextFieldOrg.getText().trim().equals(""));
    }

    public void setEnabled(boolean b) {
        jTextFieldGivenName.setEditable(b);
        jTextFieldGivenName.setBackground(Color.white);
        jTextFieldFamilyName.setEditable(b);
        jTextFieldFamilyName.setBackground(Color.white);
        jTextFieldEMail.setEditable(b);
        jTextFieldEMail.setBackground(Color.white);
        jTextFieldOrg.setEditable(b);
        jTextFieldOrg.setBackground(Color.white);
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jButtonVCard)
                editVCard();
        }
    }

    private void editVCard() {
        if (this.vCard == null || "".equals(this.vCard))
            generateVCard();
        else {
            try {
                updateVCard();
            } catch (Exception e) {
                //ignore exception, if user put error in vcard editor, it's his responsability
            }
        }

        JDialogVCardEditor jDialog =
                new JDialogVCardEditor(Util.getTopJFrame(this), this.vCard);
        jDialog.setVisible(true);
        if (jDialog.bOk) {
            this.vCard = jDialog.getVCardValue().trim();
            setVCardValues();
        }

        jDialog.dispose();
    }

    class SymMouse extends java.awt.event.MouseAdapter {
        public void mouseEntered(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object instanceof JButton)
                jButton_mouseInOut((JButton) object, true);
        }

        public void mouseExited(java.awt.event.MouseEvent event) {
            Object object = event.getSource();
            if (object instanceof JButton)
                jButton_mouseInOut((JButton) object, false);
        }
    }

    void jButton_mouseInOut(JButton jButton, boolean in) {
        jButton.setBorderPainted(in);
    }

    void setVCardValues() {
        if (this.vCard == null)
            return;

        String vCard = this.vCard;

        String fnField = null;
        String nField = null;

        String familyName = null;
        String givenName = null;
        String email = null;
        String org = null;

        // Handle easy fields in the first pass.
        for( ;; ) {
            int index = vCard.indexOf("\n");
            if (index == -1)
                break;

            String token = vCard.substring(0, index);
            if (token.startsWith("N:"))
                nField = token.substring( token.indexOf( ":" ) + 1 );
            if (token.startsWith("FN:"))
                fnField = token.substring( token.indexOf( ":" ) + 1 );
            if (token.startsWith("EMAIL"))
                email = token.substring(token.indexOf(":") + 1);
            if (token.startsWith("ORG:"))
                org = token.substring("ORG:".length());

            vCard = vCard.substring(index + 1);
            if (vCard.startsWith("END:"))
                break;
        }

        // Handle nField and fnField in the second pass.
        if( nField != null && !"".equals( nField ) ) {
            String[] vals = nField.split(";");
            if (vals.length >= 1)
                familyName = vals[0];
            if (vals.length >= 2)
                givenName = vals[1];
        }

        // In theory, the name fields have
        // already been set by the N: field so that
        // the FN: field should not be taken into account.
        // However, in case that the N: field has not been
        // specified (even though it's mandatory) or partially
        // specified, we try to use the FN: field instead.
        // As it's impossible to parse this field accurately,
        // we use an heuristic approach.
        if( ( givenName == null || "".equals( givenName ) ) &&
                ( familyName == null || "".equals( familyName ) )  &&
                ( fnField != null && !"".equals( fnField ) ) ) {
            String[] vals = fnField.split(" ");
            if (vals.length == 1)
                familyName = vals[0];
            else if (vals.length == 2) {
                givenName = vals[0];
                familyName = vals[1];
            }
        }

        jTextFieldGivenName.setText( givenName );
        jTextFieldGivenName.setCaretPosition( 0 );

        jTextFieldFamilyName.setText( familyName );
        jTextFieldFamilyName.setCaretPosition( 0 );

        jTextFieldEMail.setText( email );
        jTextFieldEMail.setCaretPosition( 0 );

        jTextFieldOrg.setText( org );
        jTextFieldOrg.setCaretPosition( 0 );
    }

    void generateVCard() {
        String familyName = jTextFieldFamilyName.getText().trim();
        String givenName = jTextFieldGivenName.getText().trim();
        String email = jTextFieldEMail.getText().trim();
        String org = jTextFieldOrg.getText().trim();

        if( ( familyName + givenName + email + org ).length() == 0 )
            return;

        String vCard = "N:" + familyName + ";" + givenName + ";;;" + "\n";
        vCard += "FN:" + givenName + (("".equals(givenName)) ? "" : " ") + familyName + "\n";

        if (!"".equals(email))
            vCard += "EMAIL;TYPE=INTERNET:" + email + "\n";
        if (!"".equals(org))
            vCard += "ORG:" + org + "\n";

        if ("".equals(vCard))
            this.vCard = null;
        else
            this.vCard = "BEGIN:VCARD\nVERSION:3.0\n" + vCard + "END:VCARD";
    }

    void updateVCard() {
        updateNandFN();
        updateEMAIL();
        updateORG();
    }

    void updateNandFN() {
        String familyName = jTextFieldFamilyName.getText().trim();
        String givenName = jTextFieldGivenName.getText().trim();
        int start = this.vCard.indexOf("\nN:");
        int end;
        if (start != -1) {
            end = this.vCard.indexOf("\n", start + 1);
            String previousValue = this.vCard.substring(start + 3, end);
            String[] vals = previousValue.split(";");
            boolean hasChanged;
            if (vals.length >= 1)
                hasChanged = (!familyName.equals(vals[0]));
            else
                hasChanged = !"".equals(familyName);
            if (!hasChanged) {
                if (vals.length >= 2)
                    hasChanged = (!givenName.equals(vals[1]));
                else
                    hasChanged = !"".equals(givenName);
            }
            if (hasChanged) {
                //replacing N:
                String additionalName = "";
                String prefix = "";
                String suffix = "";
                if (vals.length >= 3)
                    additionalName = vals[2];
                if (vals.length >= 4)
                    prefix = vals[3];
                if (vals.length >= 5)
                    suffix = vals[4];
                this.vCard = this.vCard.substring(0, start) +
                        "\nN:" + familyName + ";" + givenName + ";" + additionalName + ";" + prefix + ";" + suffix +
                        this.vCard.substring(end);
                //replacing FN:
                start = this.vCard.indexOf("\nFN:");
                if (start != -1) {
                    end = this.vCard.indexOf("\n", start + 1);
                    this.vCard = this.vCard.substring(0, start) +
                            "\nFN:" + givenName + (("".equals(givenName)) ? "" : " ") + familyName +
                            this.vCard.substring(end);
                } else {
                    start = this.vCard.indexOf("\nEND:VCARD");
                    this.vCard = this.vCard.substring(0, start) +
                            "\nFN:" + givenName + (("".equals(givenName)) ? "" : " ") + familyName +
                            this.vCard.substring(start);
                }
            }
        } else {
            //insert N:
            start = this.vCard.indexOf("\nEND:VCARD");
            this.vCard = this.vCard.substring(0, start) +
                    "\nN:" + familyName + ";" + givenName + ";;;" +
                    this.vCard.substring(start);
            //replacing FN:
            start = this.vCard.indexOf("\nFN:");
            if (start != -1) {
                end = this.vCard.indexOf("\n", start + 1);
                this.vCard = this.vCard.substring(0, start) +
                        "\nFN:" + givenName + (("".equals(givenName)) ? "" : " ") + familyName +
                        this.vCard.substring(end);
            } else {
                start = this.vCard.indexOf("\nEND:VCARD");
                this.vCard = this.vCard.substring(0, start) +
                        "\nFN:" + givenName + (("".equals(givenName)) ? "" : " ") + familyName +
                        this.vCard.substring(start);
            }
        }
    }

    void updateEMAIL() {
        String email = jTextFieldEMail.getText().trim();
        int start;
        int end;
        //EMAIL:
        if ("".equals(email)) {
            start = this.vCard.indexOf("\nEMAIL");
            //remove if previous value exists
            if (start != -1) {
                end = this.vCard.indexOf("\n", start + 1);
                this.vCard = this.vCard.substring(0, start) +
                        this.vCard.substring(end);
            }
        } else {
            start = this.vCard.indexOf("\nEMAIL");
            if (start != -1) {
                //possible update if value changed
                end = this.vCard.indexOf("\n", start + 1);
                String pref = ":";
                int typeIndex = this.vCard.indexOf(";TYPE", start + 6);
                String previousValue;
                if (typeIndex != -1) {
                    int valIndex = this.vCard.indexOf(":", typeIndex + 1);
                    pref = this.vCard.substring(typeIndex, valIndex + 1);
                    previousValue = this.vCard.substring(valIndex + 1, end);
                } else
                    previousValue = this.vCard.substring(start + 7, end);
                boolean hasChanged = (!email.equals(previousValue));
                if (hasChanged)
                    this.vCard = this.vCard.substring(0, start) +
                            "\nEMAIL" + pref + email +
                            this.vCard.substring(end);
            } else {
                //insert EMAIL:
                start = this.vCard.indexOf("\nEND:VCARD");
                this.vCard = this.vCard.substring(0, start) +
                        "\nEMAIL;TYPE=INTERNET:" + email +
                        this.vCard.substring(start);
            }
        }
    }

    void updateORG() {
        String org = jTextFieldOrg.getText().trim();
        int start;
        int end;
        if ("".equals(org)) {
            start = this.vCard.indexOf("\nORG:");
            //remove if previous value exists
            if (start != -1) {
                end = this.vCard.indexOf("\n", start + 1);
                this.vCard = this.vCard.substring(0, start) +
                        this.vCard.substring(end);
            }
        } else {
            start = this.vCard.indexOf("\nORG:");
            if (start != -1) {
                //possible update if value changed
                end = this.vCard.indexOf("\n", start + 1);
                String previousValue = this.vCard.substring(start + 5, end);
                boolean hasChanged = (!org.equals(previousValue));
                if (hasChanged)
                    this.vCard = this.vCard.substring(0, start) +
                            "\nORG:" + org +
                            this.vCard.substring(end);
            } else {
                //insert ORG:
                start = this.vCard.indexOf("\nEND:VCARD");
                this.vCard = this.vCard.substring(0, start) +
                        "\nORG:" + org +
                        this.vCard.substring(start);
            }
        }
    }

    //XML
    String toXML(String key) {
        if (this.vCard == null || "".equals(this.vCard))
            generateVCard();
        else {
            try {
                updateVCard();
            } catch (Exception e) {
                //ignore exception, if user put error in vcard editor, it's his responsability
            }
        }

        if (this.vCard == null || "".equals(this.vCard))
            return null;
        else
            return "<![CDATA[" + this.vCard + "]]>";
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() == null) return;

        String vCard = e.getFirstChild().getNodeValue();
        this.vCard = vCard.trim();

        setVCardValues();
    }

    //HTML
    String toHTML(String key) {
        String familyName = Util.convertSpecialCharactersForXML(jTextFieldFamilyName.getText().trim());
        String givenName = Util.convertSpecialCharactersForXML(jTextFieldGivenName.getText().trim());
        String email = Util.convertSpecialCharactersForXML(jTextFieldEMail.getText().trim());
        String org = Util.convertSpecialCharactersForXML(jTextFieldOrg.getText().trim());

        String html = "";
        if (!familyName.equals("") || !givenName.equals("")) {
            if(!givenName.equals(""))
                html += (html.equals("")?"":" ") + givenName;
            if(!familyName.equals(""))
                html += (html.equals("")?"":" ") + familyName;
        }
        if (!email.equals(""))
            html += (html.equals("")?"":" (") + "<a href=\"mailto:" + email + "\">" + email + "</a>)";
        if (!org.equals(""))
            html += (html.equals("")?"":"; ") + org;

        if (html.equals(""))
            html = null;
        else
            html += "<br>";

        return html;
    }
}

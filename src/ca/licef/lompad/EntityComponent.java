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
    JPanel jPanelEntity;

    JPanel jPanelNameElements;
    JPanel jPanelPrefix;
    JPanel jPanelNames;
    JPanel jPanelSuffix;

    JPanel jPanelOtherInfo;

    LocalizeJLabel jLabelPrefix;
    LocalizeJLabel jLabelGivenName;
    LocalizeJLabel jLabelFamilyName;
    LocalizeJLabel jLabelAdditionalName;
    LocalizeJLabel jLabelSuffix;
    LocalizeJLabel jLabelEMail;
    LocalizeJLabel jLabelOrg;

    JTextFieldPopup jTextFieldPrefix;
    JTextFieldPopup jTextFieldGivenName;
    JTextFieldPopup jTextFieldFamilyName;
    JTextFieldPopup jTextFieldAdditionalName;
    JTextFieldPopup jTextFieldSuffix;
    JTextFieldPopup jTextFieldEMail;
    JTextFieldPopup jTextFieldOrg;

    public EntityComponent(boolean isFirst) {
        super(null);

        jPanelEntity = new JPanel();
        jPanelEntity.setOpaque(false);
        jTextFieldPrefix = new JTextFieldPopup();
        jTextFieldGivenName = new JTextFieldPopup();
        jTextFieldFamilyName = new JTextFieldPopup();
        jTextFieldAdditionalName = new JTextFieldPopup();
        jTextFieldSuffix = new JTextFieldPopup();
        jTextFieldEMail = new JTextFieldPopup();
        jTextFieldOrg = new JTextFieldPopup();

        double xBorder = 2;
        double yBorder = 0;
        double shortGap = 2;
        double largeGap = 4;
        double sectionGap = 20;
        double sizeForFirst[][] = { 
            { xBorder, 0.055, largeGap, 0.165, shortGap, 0.165, shortGap, 0.165, largeGap, 0.055, sectionGap, TableLayout.FILL, shortGap, 0.22, xBorder },
            { yBorder, 0.5, shortGap, 0.5, yBorder } 
        };
        double sizeForOther[][] = {
            { xBorder, 0.055, largeGap, 0.165, shortGap, 0.165, shortGap, 0.165, largeGap, 0.055, sectionGap, TableLayout.FILL, shortGap, 0.22, xBorder },
            { yBorder, TableLayout.FILL, yBorder } 
        };
        jPanelEntity.setLayout( new TableLayout( isFirst ? sizeForFirst : sizeForOther ) );

        if( isFirst ) {
            java.awt.Font labelFont = new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12);
            jLabelPrefix = new LocalizeJLabel("prefix");
            jLabelPrefix.setFont(labelFont);
            jLabelGivenName = new LocalizeJLabel("givenName");
            jLabelGivenName.setFont(labelFont);
            jLabelFamilyName = new LocalizeJLabel("familyName");
            jLabelFamilyName.setFont(labelFont);
            jLabelAdditionalName = new LocalizeJLabel("additionalName");
            jLabelAdditionalName.setFont(labelFont);
            jLabelSuffix = new LocalizeJLabel("suffix");
            jLabelSuffix.setFont(labelFont);
            jLabelEMail = new LocalizeJLabel("email");
            jLabelEMail.setFont(labelFont);
            jLabelOrg = new LocalizeJLabel("org");
            jLabelOrg.setFont(labelFont);

            jPanelEntity.add( jLabelPrefix,             "1, 1" );
            jPanelEntity.add( jLabelGivenName,          "3, 1" );
            jPanelEntity.add( jLabelFamilyName,         "5, 1" );
            jPanelEntity.add( jLabelAdditionalName,     "7, 1" );
            jPanelEntity.add( jLabelSuffix,             "9, 1" );
            jPanelEntity.add( jLabelEMail,              "11, 1" );
            jPanelEntity.add( jLabelOrg,                "13, 1" );

            jPanelEntity.add( jTextFieldPrefix,         "1, 3" );
            jPanelEntity.add( jTextFieldGivenName,      "3, 3" );
            jPanelEntity.add( jTextFieldFamilyName,     "5, 3" );
            jPanelEntity.add( jTextFieldAdditionalName, "7, 3" );
            jPanelEntity.add( jTextFieldSuffix,         "9, 3" );
            jPanelEntity.add( jTextFieldEMail,          "11, 3" );
            jPanelEntity.add( jTextFieldOrg,            "13, 3" );
        }
        else {
            jPanelEntity.add( jTextFieldPrefix,         "1, 1" );
            jPanelEntity.add( jTextFieldGivenName,      "3, 1" );
            jPanelEntity.add( jTextFieldFamilyName,     "5, 1" );
            jPanelEntity.add( jTextFieldAdditionalName, "7, 1" );
            jPanelEntity.add( jTextFieldSuffix,         "9, 1" );
            jPanelEntity.add( jTextFieldEMail,          "11, 1" );
            jPanelEntity.add( jTextFieldOrg,            "13, 1" );
        }
        jPanelGauche.add(jPanelEntity);
    }

    boolean isFilled() {
        return !(jTextFieldGivenName.getText().trim().equals("") &&
                jTextFieldFamilyName.getText().trim().equals("") &&
                jTextFieldEMail.getText().trim().equals("") &&
                jTextFieldOrg.getText().trim().equals(""));
    }

    public void setEnabled(boolean b) {
        jTextFieldPrefix.setEditable(b);
        jTextFieldPrefix.setBackground(Color.white);
        jTextFieldGivenName.setEditable(b);
        jTextFieldGivenName.setBackground(Color.white);
        jTextFieldFamilyName.setEditable(b);
        jTextFieldFamilyName.setBackground(Color.white);
        jTextFieldAdditionalName.setEditable(b);
        jTextFieldAdditionalName.setBackground(Color.white);
        jTextFieldSuffix.setEditable(b);
        jTextFieldSuffix.setBackground(Color.white);
        jTextFieldEMail.setEditable(b);
        jTextFieldEMail.setBackground(Color.white);
        jTextFieldOrg.setEditable(b);
        jTextFieldOrg.setBackground(Color.white);
    }

    //XML
    String toXML(String key) {
        // No need to call convertSpecialCharactersForXML() here because the data is enclosed between CDATA delimiters.
        String familyName = jTextFieldFamilyName.getText().trim();
        String givenName = jTextFieldGivenName.getText().trim();
        String additionalName = jTextFieldAdditionalName.getText().trim();
        String prefix = jTextFieldPrefix.getText().trim();
        String suffix = jTextFieldSuffix.getText().trim();
        String email = jTextFieldEMail.getText().trim();
        String org = jTextFieldOrg.getText().trim();

        if( ( familyName + givenName + additionalName + prefix + suffix + email + org ).length() == 0 )
            return( null );

        String vCard = "";

        StringBuilder nValue = new StringBuilder();
        nValue.append( familyName ).append( ";" );
        nValue.append( givenName ).append( ";" );
        nValue.append( additionalName ).append( ";" );
        nValue.append( prefix ).append( ";" );
        nValue.append( suffix ).append( ";" );
        // Remove trailing semicolons.
        while( nValue.length() > 0 && nValue.charAt( nValue.length() - 1 ) == ';' ) 
            nValue.deleteCharAt( nValue.length() - 1 );
        vCard += "N:" + nValue.toString() + "\n";

        StringBuilder fnValue = new StringBuilder();
        fnValue.append( prefix );
        if( prefix.length() > 0 )
            fnValue.append( " " );
        fnValue.append( givenName );
        if( givenName.length() > 0 )
            fnValue.append( " " );
        fnValue.append( additionalName );
        if( additionalName.length() > 0 )
            fnValue.append( " " );
        fnValue.append( familyName );
        if( familyName.length() > 0 )
            fnValue.append( " " );
        fnValue.append( suffix );
        vCard += "FN:" + fnValue.toString() + "\n";

        if (!jTextFieldEMail.getText().trim().equals(""))
            vCard += "EMAIL;TYPE=INTERNET:" + email + "\n";
        if (!jTextFieldOrg.getText().trim().equals(""))
            vCard += "ORG:" + org + "\n";

        vCard = "<![CDATA[BEGIN:VCARD\nVERSION:3.0\n" + vCard + "END:VCARD]]>";
        return vCard;
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() == null) return;

        String vCard = e.getFirstChild().getNodeValue();

        String fnField = null;
        String nField = null;

        String familyName = null;
        String givenName = null;
        String additionalName = null;
        String prefix = null;
        String suffix = null;
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
            int indexOfSemicolon1 = nField.indexOf( ";" );
            if( indexOfSemicolon1 == -1 )
                familyName = nField;
            else {
                familyName = nField.substring( 0, indexOfSemicolon1 );

                int indexOfSemicolon2 = nField.indexOf( ";", indexOfSemicolon1 + 1 );
                if( indexOfSemicolon2 == -1 )
                    givenName = nField.substring( indexOfSemicolon1 + 1 );
                else {
                    givenName = nField.substring( indexOfSemicolon1 + 1, indexOfSemicolon2 );

                    int indexOfSemicolon3 = nField.indexOf( ";", indexOfSemicolon2 + 1 );
                    if( indexOfSemicolon3 == -1 )
                        additionalName = nField.substring( indexOfSemicolon2 + 1 );
                    else {
                        additionalName = nField.substring( indexOfSemicolon2 + 1, indexOfSemicolon3 );

                        int indexOfSemicolon4 = nField.indexOf( ";", indexOfSemicolon3 + 1 );
                        if( indexOfSemicolon4 == -1 )
                            prefix = nField.substring( indexOfSemicolon3 + 1 );
                        else  {
                            prefix = nField.substring( indexOfSemicolon3 + 1, indexOfSemicolon4 );
                            suffix = nField.substring( indexOfSemicolon4 + 1 ); 
                        }
                    }
                }
            }
        }

        // In theory, the name fields have 
        // already been set by the N: field so that 
        // the FN: field should not be taken into account.
        // However, in case that the N: field has not been 
        // specified (even though it's mandatory) or partially
        // specified, we try to use the FN: field instead.
        // As it's impossible to parse this field accurately, 
        // we use an heuristic approach.
        if( fnField != null && !"".equals( fnField ) ) {
            int indexOfSpace1 = fnField.indexOf( " " );
            if( indexOfSpace1 == -1 ) {
                if( familyName == null || "".equals( familyName ) )
                    familyName = fnField.substring( fnField.indexOf( ":" ) + 1 );
            }
            else {
                if( ( givenName == null || "".equals( givenName ) ) &&
                    ( familyName == null || "".equals( familyName ) ) ) {
                    givenName = fnField.substring( fnField.indexOf( ":" ) + 1, indexOfSpace1 );
                    familyName = fnField.substring( indexOfSpace1 + 1 );
                }
            }
        }

        if( prefix != null ) {
            jTextFieldPrefix.setText( prefix );
            jTextFieldPrefix.setCaretPosition( 0 );
        }
        if( givenName != null ) {
            jTextFieldGivenName.setText( givenName );
            jTextFieldGivenName.setCaretPosition( 0 );
        }
        if( familyName != null ) {
            jTextFieldFamilyName.setText( familyName );
            jTextFieldFamilyName.setCaretPosition( 0 );
        }
        if( additionalName != null ) {
            jTextFieldAdditionalName.setText( additionalName );
            jTextFieldAdditionalName.setCaretPosition( 0 );
        }
        if( suffix != null ) {
            jTextFieldSuffix.setText( suffix );
            jTextFieldSuffix.setCaretPosition( 0 );
        }
        if( email != null ) {
            jTextFieldEMail.setText( email );
            jTextFieldEMail.setCaretPosition( 0 );
        }
        if( org != null ) {
            jTextFieldOrg.setText( org);
            jTextFieldOrg.setCaretPosition( 0 );
        }
    }

    //HTML
    String toHTML(String key) {
        String familyName = Util.convertSpecialCharactersForXML(jTextFieldFamilyName.getText().trim());
        String givenName = Util.convertSpecialCharactersForXML(jTextFieldGivenName.getText().trim());
        String additionalName = Util.convertSpecialCharactersForXML(jTextFieldAdditionalName.getText().trim());
        String prefix = Util.convertSpecialCharactersForXML(jTextFieldPrefix.getText().trim());
        String suffix = Util.convertSpecialCharactersForXML(jTextFieldSuffix.getText().trim());
        String email = Util.convertSpecialCharactersForXML(jTextFieldEMail.getText().trim());
        String org = Util.convertSpecialCharactersForXML(jTextFieldOrg.getText().trim());

        String html = "";
        if (!familyName.equals("") || !givenName.equals("") || !additionalName.equals("")) {
            html += prefix;
            if(!givenName.equals(""))
                html += (html.equals("")?"":" ") + givenName;
            if(!additionalName.equals(""))
                html += (html.equals("")?"":" ") + additionalName;
            if(!familyName.equals(""))
                html += (html.equals("")?"":" ") + familyName;
            if(!suffix.equals(""))
                html += (html.equals("")?"":" ") + suffix;
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

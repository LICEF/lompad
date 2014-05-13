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
import java.util.List;

class DurationComponent extends FormComponent {
    JPanel jPanelDuration;
    NumericTextField jTextFieldYear;
    NumericTextField jTextFieldMonth;
    NumericTextField jTextFieldDay;
    NumericTextField jTextFieldHour;
    NumericTextField jTextFieldMin;
    NumericTextField jTextFieldSec;
    NumericTextField jTextFieldFracSec;
    LocalizeJLabel jLabelYear;
    LocalizeJLabel jLabelMonth;
    LocalizeJLabel jLabelDay;
    LocalizeJLabel jLabelHour;
    LocalizeJLabel jLabelMin;
    LocalizeJLabel jLabelSec;
    JLabel jLabelFracSec;

    public DurationComponent() {
        super(null);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(new javax.swing.border.EmptyBorder(jScrollPane.getInsets()));
        jScrollPane.setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
        jPanelDuration = new JPanel();
        jPanelDuration.setLayout(new BoxLayout(jPanelDuration, BoxLayout.X_AXIS));
        jPanelDuration.setAlignmentY(0.0F);
        jPanelDuration.setOpaque(false);

        jScrollPane.getViewport().add(jPanelDuration);
        jPanelGauche.add(jScrollPane);

        Font fontLabel = new Font("Dialog", Font.PLAIN, 11);

        jTextFieldYear = new NumericTextField(-1, "1", null);
        jTextFieldYear.setMinimumSize(new Dimension(30, 20));
        jTextFieldYear.setPreferredSize(new Dimension(30, 20));
        jTextFieldYear.setMaximumSize(new Dimension(30, 20));
        jTextFieldMonth = new NumericTextField(-1, "1", null);
        jTextFieldMonth.setMinimumSize(new Dimension(30, 20));
        jTextFieldMonth.setPreferredSize(new Dimension(30, 20));
        jTextFieldMonth.setMaximumSize(new Dimension(30, 20));
        jTextFieldDay = new NumericTextField(-1, "1", null);
        jTextFieldDay.setMinimumSize(new Dimension(30, 20));
        jTextFieldDay.setPreferredSize(new Dimension(30, 20));
        jTextFieldDay.setMaximumSize(new Dimension(30, 20));
        jTextFieldHour = new NumericTextField(-1, "1", null);
        jTextFieldHour.setMinimumSize(new Dimension(30, 20));
        jTextFieldHour.setPreferredSize(new Dimension(30, 20));
        jTextFieldHour.setMaximumSize(new Dimension(30, 20));
        jTextFieldMin = new NumericTextField(-1, "1", null);
        jTextFieldMin.setMinimumSize(new Dimension(30, 20));
        jTextFieldMin.setPreferredSize(new Dimension(30, 20));
        jTextFieldMin.setMaximumSize(new Dimension(30, 20));
        jTextFieldSec = new NumericTextField();
        jTextFieldSec.setMinimumSize(new Dimension(30, 20));
        jTextFieldSec.setPreferredSize(new Dimension(30, 20));
        jTextFieldSec.setMaximumSize(new Dimension(30, 20));
        jTextFieldFracSec = new NumericTextField();
        jTextFieldFracSec.setMinimumSize(new Dimension(30, 20));
        jTextFieldFracSec.setPreferredSize(new Dimension(30, 20));
        jTextFieldFracSec.setMaximumSize(new Dimension(30, 20));
        jLabelYear = new LocalizeJLabel("years");
        jLabelYear.setFont(fontLabel);
        jLabelMonth = new LocalizeJLabel("months");
        jLabelMonth.setFont(fontLabel);
        jLabelDay = new LocalizeJLabel("days");
        jLabelDay.setFont(fontLabel);
        jLabelHour = new LocalizeJLabel("hours");
        jLabelHour.setFont(fontLabel);
        jLabelMin = new LocalizeJLabel("mins");
        jLabelMin.setFont(fontLabel);
        jLabelSec = new LocalizeJLabel("secs");
        jLabelSec.setFont(fontLabel);
        jLabelFracSec = new JLabel(".");

        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jLabelYear);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jTextFieldYear);
        jPanelDuration.add(Box.createHorizontalStrut(10));
        jPanelDuration.add(jLabelMonth);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jTextFieldMonth);
        jPanelDuration.add(Box.createHorizontalStrut(10));
        jPanelDuration.add(jLabelDay);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jTextFieldDay);
        jPanelDuration.add(Box.createHorizontalStrut(10));
        jPanelDuration.add(jLabelHour);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jTextFieldHour);
        jPanelDuration.add(Box.createHorizontalStrut(10));
        jPanelDuration.add(jLabelMin);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jTextFieldMin);
        jPanelDuration.add(Box.createHorizontalStrut(10));
        jPanelDuration.add(jLabelSec);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jTextFieldSec);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jLabelFracSec);
        jPanelDuration.add(Box.createHorizontalStrut(5));
        jPanelDuration.add(jTextFieldFracSec);
    }

    boolean isFilled() {
        return jTextFieldYear.isFilled() || jTextFieldMonth.isFilled() ||
                jTextFieldDay.isFilled() || jTextFieldHour.isFilled() ||
                jTextFieldMin.isFilled() || jTextFieldSec.isFilled() ||
                jTextFieldFracSec.isFilled();
    }

    public void setEnabled(boolean b) {
        jTextFieldYear.setEditable(b);
        jTextFieldYear.setBackground(Color.white);
        jTextFieldMonth.setEditable(b);
        jTextFieldMonth.setBackground(Color.white);
        jTextFieldDay.setEditable(b);
        jTextFieldDay.setBackground(Color.white);
        jTextFieldHour.setEditable(b);
        jTextFieldHour.setBackground(Color.white);
        jTextFieldMin.setEditable(b);
        jTextFieldMin.setBackground(Color.white);
        jTextFieldSec.setEditable(b);
        jTextFieldSec.setBackground(Color.white);
        jTextFieldFracSec.setEditable(b);
        jTextFieldFracSec.setBackground(Color.white);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        String xmlDate = "";
        if (jTextFieldYear.getText().length() > 0)
            xmlDate += jTextFieldYear.getText() + "Y";
        if (jTextFieldMonth.getText().length() > 0)
            xmlDate += jTextFieldMonth.getText() + "M";
        if (jTextFieldDay.getText().length() > 0)
            xmlDate += jTextFieldDay.getText() + "D";

        String xmlTime = "";
        if (jTextFieldHour.getText().length() > 0)
            xmlTime += jTextFieldHour.getText() + "H";
        if (jTextFieldMin.getText().length() > 0)
            xmlTime += jTextFieldMin.getText() + "M";
        String sec = "";
        if (jTextFieldSec.getText().length() > 0) {
            int secVal = Integer.parseInt(jTextFieldSec.getText());
            if (secVal > 0)
                sec += secVal;
        }
        if (jTextFieldFracSec.getText().length() > 0) {
            int fracSecVal = Integer.parseInt(jTextFieldFracSec.getText());
            if (fracSecVal > 0)
                sec = (sec.equals("") ? "0" : sec) + "." + jTextFieldFracSec.getText();
        }
        if (!sec.equals("")) xmlTime += sec + "S";

        if (!xmlTime.equals("")) xmlTime = "T" + xmlTime;

        xml += xmlDate;
        xml += xmlTime;

        if (!xml.equals(""))
            xml = "<" + Util.getTag(key) + ">P" + xml +
                    "</" + Util.getTag(key) + ">\n";
        else
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e, List<String> observations) {
        if (e.getFirstChild() == null) return;

        String value = e.getFirstChild().getNodeValue();
        if (value.charAt(0) != 'P') return;
        value = value.substring(1);

        String lapseDate = "";
        String lapseTime = "";
        int index = value.indexOf("T");
        if (index == -1)
            lapseDate = value;
        else {
            lapseDate = value.substring(0, index);
            lapseTime = value.substring(index + 1);
        }

        if (!lapseDate.equals("")) {
            index = lapseDate.indexOf("Y");
            if (index != -1) {
                jTextFieldYear.setText(lapseDate.substring(0, index));
                jTextFieldYear.setCaretPosition(0);
                lapseDate = lapseDate.substring(index + 1);
            }
            index = lapseDate.indexOf("M");
            if (index != -1) {
                jTextFieldMonth.setText(lapseDate.substring(0, index));
                jTextFieldMonth.setCaretPosition(0);
                lapseDate = lapseDate.substring(index + 1);
            }
            index = lapseDate.indexOf("D");
            if (index != -1) {
                jTextFieldDay.setText(lapseDate.substring(0, index));
                jTextFieldDay.setCaretPosition(0);
                lapseDate = lapseDate.substring(index + 1);
            }
        }

        if (!lapseTime.equals("")) {
            index = lapseTime.indexOf("H");
            if (index != -1) {
                jTextFieldHour.setText(lapseTime.substring(0, index));
                jTextFieldHour.setCaretPosition(0);
                lapseTime = lapseTime.substring(index + 1);
            }
            index = lapseTime.indexOf("M");
            if (index != -1) {
                jTextFieldMin.setText(lapseTime.substring(0, index));
                jTextFieldMin.setCaretPosition(0);
                lapseTime = lapseTime.substring(index + 1);
            }
            index = lapseTime.indexOf("S");
            if (index != -1) {
                lapseTime = lapseTime.substring(0, index);
                index = lapseTime.indexOf(".");
                if (index == -1)
                    jTextFieldSec.setText(lapseTime);
                else {
                    jTextFieldSec.setText(lapseTime.substring(0, index));
                    jTextFieldFracSec.setText(lapseTime.substring(index + 1));
                    jTextFieldFracSec.setCaretPosition(0);
                }
                jTextFieldSec.setCaretPosition(0);
            }
        }
    }

    //HTML
    String toHTML(String key) {
        String html = "";

        if (jTextFieldYear.getText().length() > 0)
            html += jTextFieldYear.getText() + " " + jLabelYear.getText();
        if (jTextFieldMonth.getText().length() > 0)
            html += (html.equals("")?"":" + ") + jTextFieldMonth.getText() + " " + jLabelMonth.getText();
        if (jTextFieldDay.getText().length() > 0)
            html += (html.equals("")?"":" + ") + jTextFieldDay.getText() + " " + jLabelDay.getText();
        if (jTextFieldHour.getText().length() > 0)
            html += (html.equals("")?"":" + ") + jTextFieldHour.getText() + " " + jLabelHour.getText();
        if (jTextFieldMin.getText().length() > 0)
            html += (html.equals("")?"":" + ") + jTextFieldMin.getText() + " " + jLabelMin.getText();
        String sec = "";
        if (jTextFieldSec.getText().length() > 0) {
            int secVal = Integer.parseInt(jTextFieldSec.getText());
            if (secVal > 0)
                sec += secVal;
        }
        if (jTextFieldFracSec.getText().length() > 0) {
            int fracSecVal = Integer.parseInt(jTextFieldFracSec.getText());
            if (fracSecVal > 0)
                sec = (sec.equals("") ? "0" : sec) + "." + jTextFieldFracSec.getText();
        }
        if (!sec.equals("")) html += (html.equals("")?"":" + ") + sec + " " + jLabelSec.getText();

        if (html.equals(""))
            html = null;
        else
            html = html + "<br>";

        return html;
    }
}

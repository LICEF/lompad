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
import java.util.Calendar;

class DatetimeComponent extends FormComponent {
    JButton jButtonWizard;

    JPanel jPanelGrid;
    JPanel jPanelDate;
    JPanel jPanelTime;
    NumericTextField jTextFieldYear;
    NumericTextField jTextFieldMonth;
    NumericTextField jTextFieldDay;
    NumericTextField jTextFieldHour;
    NumericTextField jTextFieldMin;
    NumericTextField jTextFieldSec;
    NumericTextField jTextFieldFracSec;
    LocalizeJLabel jLabelDate;
    LocalizeJLabel jLabelYear;
    LocalizeJLabel jLabelMonth;
    LocalizeJLabel jLabelDay;
    LocalizeJLabel jLabelTime;
    LocalizeJLabel jLabelHour;
    LocalizeJLabel jLabelMin;
    LocalizeJLabel jLabelSec;
    JLabel jLabelFracSec;

    ButtonGroup gTimeZone;
    JRadioButton jRadioButtonZ;
    JRadioButton jRadioButtonDecal;
    JComboBox jComboBoxSign;
    NumericTextField jTextFieldTZHour;
    NumericTextField jTextFieldTZMin;
    LocalizeJLabel jLabelTZ;
    LocalizeJLabel jLabelTZHour;
    LocalizeJLabel jLabelTZMin;

    public DatetimeComponent() {
        super(null);

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setBorder(new javax.swing.border.EmptyBorder(jScrollPane.getInsets()));
        jScrollPane.setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
        jPanelGrid = new JPanel();
        jPanelGrid.setOpaque(false);
        jPanelGrid.setLayout(new GridLayout(2, 1));
        jPanelGrid.setAlignmentY(0.0F);
        jScrollPane.getViewport().add(jPanelGrid);
        jPanelGauche.add(jScrollPane);

        jPanelDate = new JPanel();
        jPanelDate.setLayout(new BoxLayout(jPanelDate, BoxLayout.X_AXIS));
        jPanelDate.setOpaque(false);
        jPanelTime = new JPanel();
        jPanelTime.setLayout(new BoxLayout(jPanelTime, BoxLayout.X_AXIS));
        jPanelTime.setOpaque(false);
        jPanelGrid.add(jPanelDate);
        jPanelGrid.add(jPanelTime);

        Font fontLabel = new Font("Dialog", Font.PLAIN, 11);

        jButtonWizard = new JButton(Util.wizardIcon);
        jButtonWizard.setFont(new Font("Dialog", Font.PLAIN, 12));
        jButtonWizard.setFocusPainted(false);
        jButtonWizard.setBorderPainted(false);
        jButtonWizard.setMinimumSize(new Dimension(23, 22));
        jButtonWizard.setPreferredSize(new Dimension(23, 22));
        jButtonWizard.setMaximumSize(new Dimension(23, 22));

        jTextFieldYear = new NumericTextField(4, "0001", null);
        jTextFieldYear.setMinimumSize(new Dimension(35, 20));
        jTextFieldYear.setPreferredSize(new Dimension(35, 20));
        jTextFieldYear.setMaximumSize(new Dimension(35, 20));
        jTextFieldMonth = new NumericTextField(2, "01", "12");
        jTextFieldMonth.setMinimumSize(new Dimension(25, 20));
        jTextFieldMonth.setPreferredSize(new Dimension(25, 20));
        jTextFieldMonth.setMaximumSize(new Dimension(25, 20));
        jTextFieldDay = new NumericTextField(2, "01", "31");
        jTextFieldDay.setMinimumSize(new Dimension(25, 20));
        jTextFieldDay.setPreferredSize(new Dimension(25, 20));
        jTextFieldDay.setMaximumSize(new Dimension(25, 20));
        jTextFieldHour = new NumericTextField(2, "00", "23");
        jTextFieldHour.setMinimumSize(new Dimension(25, 20));
        jTextFieldHour.setPreferredSize(new Dimension(25, 20));
        jTextFieldHour.setMaximumSize(new Dimension(25, 20));
        jTextFieldMin = new NumericTextField(2, "00", "59");
        jTextFieldMin.setMinimumSize(new Dimension(25, 20));
        jTextFieldMin.setPreferredSize(new Dimension(25, 20));
        jTextFieldMin.setMaximumSize(new Dimension(25, 20));
        jTextFieldSec = new NumericTextField(2, "00", "59");
        jTextFieldSec.setMinimumSize(new Dimension(25, 20));
        jTextFieldSec.setPreferredSize(new Dimension(25, 20));
        jTextFieldSec.setMaximumSize(new Dimension(25, 20));
        jTextFieldFracSec = new NumericTextField();
        jTextFieldFracSec.setMinimumSize(new Dimension(25, 20));
        jTextFieldFracSec.setPreferredSize(new Dimension(25, 20));
        jTextFieldFracSec.setMaximumSize(new Dimension(25, 20));

        jRadioButtonZ = new JRadioButton("Z");
        jRadioButtonZ.setFont(fontLabel);
        jRadioButtonDecal = new JRadioButton();

        jComboBoxSign = new JComboBox();
        jComboBoxSign.setFont(fontLabel);
        jComboBoxSign.addItem("+");
        jComboBoxSign.addItem("-");
        jComboBoxSign.addItem("");
        jComboBoxSign.setSelectedIndex(2);
        jComboBoxSign.setMinimumSize(new Dimension(35, 20));
        jComboBoxSign.setPreferredSize(new Dimension(35, 20));
        jComboBoxSign.setMaximumSize(new Dimension(35, 20));

        jTextFieldTZHour = new NumericTextField(2, "00", "23");
        jTextFieldTZHour.setMinimumSize(new Dimension(25, 20));
        jTextFieldTZHour.setPreferredSize(new Dimension(25, 20));
        jTextFieldTZHour.setMaximumSize(new Dimension(25, 20));
        jTextFieldTZHour.setBackground(Color.white);
        jTextFieldTZMin = new NumericTextField(2, "00", "59");
        jTextFieldTZMin.setMinimumSize(new Dimension(25, 20));
        jTextFieldTZMin.setPreferredSize(new Dimension(25, 20));
        jTextFieldTZMin.setMaximumSize(new Dimension(25, 20));
        jTextFieldTZMin.setBackground(Color.white);

        jLabelDate = new LocalizeJLabel("date");
        jLabelDate.setFont(fontLabel);
        jLabelYear = new LocalizeJLabel("year");
        jLabelYear.setFont(fontLabel);
        jLabelMonth = new LocalizeJLabel("month");
        jLabelMonth.setFont(fontLabel);
        jLabelDay = new LocalizeJLabel("day");
        jLabelDay.setFont(fontLabel);
        jLabelTime = new LocalizeJLabel("time");
        jLabelTime.setFont(fontLabel);
        jLabelHour = new LocalizeJLabel("hour");
        jLabelHour.setFont(fontLabel);
        jLabelMin = new LocalizeJLabel("min");
        jLabelMin.setFont(fontLabel);
        jLabelSec = new LocalizeJLabel("sec");
        jLabelSec.setFont(fontLabel);
        jLabelFracSec = new JLabel(".");

        jLabelTZ = new LocalizeJLabel("timezone");
        jLabelTZ.setFont(fontLabel);
        jLabelTZHour = new LocalizeJLabel("hour");
        jLabelTZHour.setFont(fontLabel);
        jLabelTZMin = new LocalizeJLabel("min");
        jLabelTZMin.setFont(fontLabel);

        jPanelDate.add(Box.createHorizontalStrut(5));
        jPanelDate.add(jLabelDate);
        jPanelDate.add(Box.createHorizontalStrut(5));
        jPanelDate.add(jLabelYear);
        jPanelDate.add(Box.createHorizontalStrut(5));
        jPanelDate.add(jTextFieldYear);
        jPanelDate.add(Box.createHorizontalStrut(10));
        jPanelDate.add(jLabelMonth);
        jPanelDate.add(Box.createHorizontalStrut(5));
        jPanelDate.add(jTextFieldMonth);
        jPanelDate.add(Box.createHorizontalStrut(10));
        jPanelDate.add(jLabelDay);
        jPanelDate.add(Box.createHorizontalStrut(5));
        jPanelDate.add(jTextFieldDay);
        jPanelDate.add(Box.createHorizontalStrut(13));
        jPanelDate.add(jButtonWizard);

        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jLabelTime);
        jPanelTime.add(Box.createHorizontalStrut(calcOffset()));
        jPanelTime.add(jLabelHour);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jTextFieldHour);
        jPanelTime.add(Box.createHorizontalStrut(10));
        jPanelTime.add(jLabelMin);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jTextFieldMin);
        jPanelTime.add(Box.createHorizontalStrut(12));
        jPanelTime.add(jLabelSec);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jTextFieldSec);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jLabelFracSec);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jTextFieldFracSec);

        jPanelTime.add(Box.createHorizontalStrut(15));
        jPanelTime.add(jLabelTZ);
        jPanelTime.add(jRadioButtonZ);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jRadioButtonDecal);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jComboBoxSign);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jLabelTZHour);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jTextFieldTZHour);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jLabelTZMin);
        jPanelTime.add(Box.createHorizontalStrut(5));
        jPanelTime.add(jTextFieldTZMin);

        SymAction lSymAction = new SymAction();
        jButtonWizard.addActionListener(lSymAction);
        jRadioButtonZ.addActionListener(lSymAction);
        jRadioButtonDecal.addActionListener(lSymAction);

        SymMouse aSymMouse = new SymMouse();
        jButtonWizard.addMouseListener(aSymMouse);

        setEnabledTZ(false);
    }

    boolean isFilled() {
        return jTextFieldYear.isFilled();
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
        jRadioButtonZ.setEnabled(b);
        jRadioButtonDecal.setEnabled(b);
        setEnabledTZ(b);
        jButtonWizard.setVisible(b);
    }

    int calcOffset() {
        int off = 5;
        FontMetrics fm = getFontMetrics(jLabelDate.getFont());
        off += fm.stringWidth(jLabelDate.getText());
        off += 5;
        off += fm.stringWidth(jLabelYear.getText());
        off += 5;
        off += 35; //textfield year

        off -= 5;
        off -= fm.stringWidth(jLabelTime.getText());
        off -= fm.stringWidth(jLabelHour.getText());
        off -= 5;
        off -= 25; //textfield hour

        return off;
    }

    class SymAction implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jButtonWizard)
                jButtonWizard_actionPerformed(event);
            else if (object == jRadioButtonZ) {
                jRadioButtonDecal.setSelected(false);
                setEnabledTZ(false);
                if (bDecal)
                    changeDecal(true);
            } else if (object == jRadioButtonDecal) {
                jRadioButtonZ.setSelected(false);
                setEnabledTZ(jRadioButtonDecal.isSelected());
                changeDecal(!jRadioButtonDecal.isSelected());
            }
        }
    }

    void jButtonWizard_actionPerformed(java.awt.event.ActionEvent event) {
        JDialogDateSelector jDialog =
                new JDialogDateSelector(Util.getTopJFrame(this));
        jDialog.setVisible(true);
        if (jDialog.bOk) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(jDialog.currentDate);
            jTextFieldYear.setText(cal.get(Calendar.YEAR) + "");
            jTextFieldMonth.setText(Util.completeDigit(cal.get(Calendar.MONTH) + 1 + "", 2));
            jTextFieldDay.setText(Util.completeDigit(cal.get(Calendar.DATE) + "", 2));
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

    void setEnabledTZ(boolean b) {
        jComboBoxSign.setEnabled(b && jRadioButtonDecal.isSelected());
        jTextFieldTZHour.setEditable(b && jRadioButtonDecal.isSelected());
        jTextFieldTZMin.setEditable(b && jRadioButtonDecal.isSelected());
    }

    int pos = 0;
    boolean bDecal = false;
    void changeDecal(boolean b) {
        if (b) {
            pos = jComboBoxSign.getSelectedIndex();
            jComboBoxSign.addItem("");
            jComboBoxSign.setSelectedIndex(2);
            jComboBoxSign.updateUI();
            bDecal = false;
        } else {
            jComboBoxSign.removeItemAt(2);
            jComboBoxSign.setSelectedIndex(pos);
            jComboBoxSign.updateUI();
            bDecal = true;
        }
    }

    void graphicalUpdate() {
        jPanelTime.remove(2);
        jPanelTime.add(Box.createHorizontalStrut(calcOffset()), 2);
    }

    //XML
    String toXML(String key) {
        String xml = "";

        if (jTextFieldYear.isFilled()) {
            xml += jTextFieldYear.getText();
            if (jTextFieldMonth.isFilled()) {
                xml += "-" + jTextFieldMonth.getText();
                if (jTextFieldDay.isFilled()) {
                    xml += "-" + jTextFieldDay.getText();
                    String xmlTime = "";
                    if (jTextFieldHour.isFilled()) {
                        xmlTime += jTextFieldHour.getText();
                        if (jTextFieldMin.isFilled()) {
                            xmlTime += ":" + jTextFieldMin.getText();
                            if (jTextFieldSec.isFilled()) {
                                xmlTime += ":" + jTextFieldSec.getText();

                                if (!jTextFieldFracSec.getText().equals(""))
                                    xmlTime += "." + jTextFieldFracSec.getText();
                            }

                            if (jRadioButtonZ.isSelected())
                                xmlTime += "Z";

                            if (jRadioButtonDecal.isSelected() &&
                                    jTextFieldTZHour.isFilled() && jTextFieldTZMin.isFilled()) {
                                xmlTime += jComboBoxSign.getSelectedItem();
                                xmlTime += jTextFieldTZHour.getText() + ":" + jTextFieldTZMin.getText();

                            }
                        }
                    }
                    if (!"".equals(xmlTime))
                        xml += "T" + xmlTime;
                }
            }
        }

        if (!xml.equals(""))
            xml = "<" + Util.getTag(key) + ">" + xml +
                    "</" + Util.getTag(key) + ">\n";
        else
            xml = null;

        return xml;
    }

    void fromXML(String path, Element e) {
        if (e.getFirstChild() == null) return;

        String value = e.getFirstChild().getNodeValue();
        String date = "";
        String time = "";
        int indexTime = value.indexOf("T");
        if (indexTime == -1)
            date = value;
        else {
            date = value.substring(0, indexTime);
            time = value.substring(indexTime + 1);
        }

        //date
        if (!date.equals("")) {
            int indexSep = date.indexOf("-");
            if (indexSep != -1) {
                jTextFieldYear.setText(date.substring(0, indexSep));
                date = date.substring(indexSep + 1);
                indexSep = date.indexOf("-");
                if (indexSep != -1) {
                    jTextFieldMonth.setText(date.substring(0, indexSep));
                    jTextFieldDay.setText(date.substring(indexSep + 1));
                } else
                    jTextFieldMonth.setText(date);
            } else
                jTextFieldYear.setText(date);
        }

        //time
        if (!time.equals("")) {
            String timeValue;
            String timeZone = "";
            int indexTimeZone = time.indexOf("Z");
            if (indexTimeZone == -1)
                indexTimeZone = time.indexOf("+");
            if (indexTimeZone == -1)
                indexTimeZone = time.indexOf("-");

            if (indexTimeZone == -1) //pas de TZD
                timeValue = time;
            else {
                timeValue = time.substring(0, indexTimeZone);
                timeZone = time.substring(indexTimeZone);
            }

            //hh:mm:ss
            int indexSep = timeValue.indexOf(":");
            if (indexSep != -1) {
                jTextFieldHour.setText(timeValue.substring(0, indexSep));
                timeValue = timeValue.substring(indexSep + 1);
                indexSep = timeValue.indexOf(":");
                if (indexSep != -1) {
                    jTextFieldMin.setText(timeValue.substring(0, indexSep));
                    timeValue = timeValue.substring(indexSep + 1);
                    indexSep = timeValue.indexOf(".");
                    if (indexSep != -1) {
                        jTextFieldSec.setText(timeValue.substring(0, indexSep));
                        jTextFieldFracSec.setText(timeValue.substring(indexSep + 1));
                    } else
                        jTextFieldSec.setText(timeValue);
                } else
                    jTextFieldMin.setText(timeValue);
            } else
                jTextFieldHour.setText(timeValue);

            //timezone: Z or +hh:mm or -hh:mm
            if (!timeZone.equals("")) {
                if (timeZone.startsWith("Z")) {
                    jRadioButtonZ.setSelected(true);
                    changeDecal(true);
                } else {
                    jComboBoxSign.setSelectedItem(timeZone.substring(0, 1));
                    jRadioButtonDecal.setSelected(true);
                    setEnabledTZ(true);
                    changeDecal(false);
                    timeZone = timeZone.substring(1);
                    indexSep = timeZone.indexOf(":");
                    jTextFieldTZHour.setText(timeZone.substring(0, indexSep));
                    jTextFieldTZMin.setText(timeZone.substring(indexSep + 1));
                }
            }
        }
    }

    //HTML
    String toHTML(String key) {
        String html = "";

        if (jTextFieldYear.isFilled()) {
            html += jTextFieldYear.getText();
            if (jTextFieldMonth.isFilled()) {
                html += "-" + jTextFieldMonth.getText();
                if (jTextFieldDay.isFilled()) {
                    html += "-" + jTextFieldDay.getText();
                    String htmlTime = "";
                    if (jTextFieldHour.isFilled()) {
                        htmlTime += jTextFieldHour.getText();
                        if (jTextFieldMin.isFilled()) {
                            htmlTime += ":" + jTextFieldMin.getText();
                            if (jTextFieldSec.isFilled()) {
                                htmlTime += ":" + jTextFieldSec.getText();

                                if (!jTextFieldFracSec.getText().equals(""))
                                    htmlTime += "." + jTextFieldFracSec.getText();
                            }

                            if (jRadioButtonZ.isSelected())
                                htmlTime += "Z";

                            if (jRadioButtonDecal.isSelected() &&
                                    jTextFieldTZHour.isFilled() && jTextFieldTZMin.isFilled()) {
                                htmlTime += jComboBoxSign.getSelectedItem();
                                htmlTime += jTextFieldTZHour.getText() + ":" + jTextFieldTZMin.getText();

                            }
                        }
                    }
                    if (!"".equals(htmlTime))
                        html += " " + htmlTime;
                }
            }
        }

        if (html.equals(""))
            html = null;
        else
            html += "<br>";

        return html;
    }
}

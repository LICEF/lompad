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

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 2004-11-09
 */
class JDialogDateSelector extends JDialog {

    JPanel jPanelContent;
    JPanel jPanelButton;
    JButton jButtonOk;
    JButton jButtonCancel;

    JComboBox jComboBoxMonth;
    JSpinner jSpinnerYear;

    LightCalendar calendar;

    boolean bOk;

    Date currentDate;

    public JDialogDateSelector(Frame parent) {
        this(parent, new Date());
    }

    public JDialogDateSelector(Frame parent, Date initDate) {
        super(parent, "title", true);

        setSize(220, 270);

        currentDate = initDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(0, 5));

        jPanelContent = new JPanel() {
            public Insets getInsets() {
                return new Insets(5, 5, 5, 5);
            }
        };
        jPanelContent.setLayout(new BorderLayout(0, 5));
        cp.add(jPanelContent, BorderLayout.CENTER);

        JPanel jPanelTop = new JPanel();
        jPanelTop.setLayout(new BoxLayout(jPanelTop, BoxLayout.X_AXIS));
        jComboBoxMonth = new JComboBox();
        jComboBoxMonth.setFont(new Font("Dialog", Font.PLAIN, 12));
        jComboBoxMonth.setMinimumSize(new Dimension(23, 24));
        jComboBoxMonth.setPreferredSize(new Dimension(1000, 24));
        jComboBoxMonth.setMaximumSize(new Dimension(1000, 24));
        DateFormatSymbols dfs = new DateFormatSymbols(Util.locale);
        String[] months = dfs.getMonths();
        for (int i = 0; i < months.length - 1; i++)
            jComboBoxMonth.addItem(Util.capitalize(months[i]));
        int iMonth = cal.get( Calendar.MONTH );
        jComboBoxMonth.setSelectedIndex(iMonth);

        SpinnerDateModel model = new SpinnerDateModel();
        jSpinnerYear = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(jSpinnerYear, "yyyy");
        jSpinnerYear.setEditor(editor);
        editor.getTextField().setEditable(false);
        model.setValue(currentDate);
        jSpinnerYear.setMinimumSize(new Dimension(23, 24));
        jSpinnerYear.setPreferredSize(new Dimension(1000, 24));
        jSpinnerYear.setMaximumSize(new Dimension(1000, 24));

        jPanelTop.add(jComboBoxMonth);
        jPanelTop.add(Box.createHorizontalStrut(5));
        jPanelTop.add(jSpinnerYear);
        jPanelContent.add(jPanelTop, BorderLayout.NORTH);

        calendar = new LightCalendar(this, false);
        calendar.setDate( currentDate );
        jPanelContent.add(calendar, BorderLayout.CENTER);

        jButtonOk = new JButton("OK");
        jButtonOk.setFont(jComboBoxMonth.getFont());
        jButtonCancel = new JButton("cancel");
        jButtonCancel.setFont(jButtonOk.getFont());
        jPanelButton = new JPanel();
        jPanelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanelButton.add(jButtonOk);
        jPanelButton.add(jButtonCancel);
        cp.add(jPanelButton, BorderLayout.SOUTH);

        SymAction lSymAction = new SymAction();
        jComboBoxMonth.addActionListener(lSymAction);
        jButtonOk.addActionListener(lSymAction);
        jButtonCancel.addActionListener(lSymAction);

        SymChange lSymChange = new SymChange();
        jSpinnerYear.addChangeListener(lSymChange);

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogDateSelectorRes", Util.locale);
        setTitle(" " + resBundle.getString("title"));
        jButtonCancel.setText(resBundle.getString("cancel"));
    }

    public void setVisible(boolean b) {
        if (b) {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((d.width - getSize().width) / 2, (d.height - getSize().height) / 2);
        }
        super.setVisible(b);
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jComboBoxMonth)
                jComboBoxMonth_actionPerformed();
            else if (object == jButtonOk)
                jButtonOk_actionPerformed();
            else if (object == jButtonCancel)
                jButtonCancel_actionPerformed();
        }
    }

    void jComboBoxMonth_actionPerformed() {
        Calendar previousCal = Calendar.getInstance();
        previousCal.setTime( currentDate );

        Calendar newCal = Calendar.getInstance();
        newCal.set( Calendar.YEAR, previousCal.get( Calendar.YEAR ) );
        newCal.set( Calendar.MONTH, jComboBoxMonth.getSelectedIndex() );

        int newMonth = jComboBoxMonth.getSelectedIndex();
        if (previousCal.get( Calendar.DATE ) > 28 && newMonth == 1) //fevrier
            newCal.set( Calendar.DATE, 28 );
        else if (previousCal.get( Calendar.DATE ) == 31 &&
            (newMonth == 3 || newMonth == 5 || newMonth == 8 || newMonth == 10) ) //avr, juin, sept, nov
            newCal.set( Calendar.DATE, 30 );
        else
            newCal.set( Calendar.DATE, previousCal.get( Calendar.DATE ) );

        calendar.setDate( newCal.getTime() );
        calendar.selectionner( newCal.getTime() );
        currentDate = newCal.getTime();
    }

    void jButtonOk_actionPerformed() {
        bOk = true;
        setVisible(false);
    }

    void jButtonCancel_actionPerformed() {
        setVisible(false);
    }

    class SymChange implements javax.swing.event.ChangeListener {
        public void stateChanged(ChangeEvent event) {
            Object object = event.getSource();
            if (object == jSpinnerYear)
                jSpinnerYear_changePerformed();
        }
    }

    void jSpinnerYear_changePerformed() {
        Calendar previousCal = Calendar.getInstance();
        previousCal.setTime( currentDate );

        Calendar cTemp = Calendar.getInstance();
        cTemp.setTime((Date)jSpinnerYear.getModel().getValue());
        Calendar newCal = Calendar.getInstance();
        newCal.set( Calendar.YEAR, cTemp.get(Calendar.YEAR) );
        newCal.set( Calendar.MONTH,  previousCal.get( Calendar.MONTH ) );
        if (previousCal.get( Calendar.DATE ) > 28 &&
            previousCal.get( Calendar.MONTH ) == 1) //fevrier
            newCal.set( Calendar.DATE, 28 );
        else
            newCal.set( Calendar.DATE, previousCal.get( Calendar.DATE ) );

        calendar.setDate( newCal.getTime() );
        calendar.selectionner( newCal.getTime() );
        currentDate = newCal.getTime();
    }
}





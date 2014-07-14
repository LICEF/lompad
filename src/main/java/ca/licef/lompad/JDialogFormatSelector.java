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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.ResourceBundle;


/**
 * Created by IntelliJ IDEA.
 * User: amiara
 * Date: 2004-11-09
 */
class JDialogFormatSelector extends JDialog {

    JPanel jPanelContent;
    JPanel jPanelButton;
    JButton jButtonOk;
    JButton jButtonCancel;

    JRadioButton jRadioButtonNonDigital;
    JRadioButton jRadioButtonDigital;
    JComboBox jComboBoxCategory;
    private DefaultTableModel mimeSubtypeListModel;
    private JTable mimeSubtypeList;
    private String mimeSubtypeListLabel; 

    boolean bOk;

    String format;

    public JDialogFormatSelector(JFrame parent) {
        super(parent, "title", true);

        setSize(400, 300);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout(0, 5));

        jPanelContent = new JPanel() {
            public Insets getInsets() {
                return new Insets(5, 5, 5, 5);
            }
        };
        jPanelContent.setLayout(new BorderLayout(0, 5));
        cp.add(jPanelContent, BorderLayout.CENTER);

        jRadioButtonNonDigital = new JRadioButton("  non-digital");
        jRadioButtonNonDigital.setFocusPainted(false);
        jRadioButtonNonDigital.setFont(new Font("Dialog", Font.PLAIN, 12));
        jPanelContent.add(jRadioButtonNonDigital, BorderLayout.NORTH);

        JPanel jPanelDigital = new JPanel();
        jPanelDigital.setLayout(new BorderLayout(5, 0));
        JPanel jPanelDigitalWest = new JPanel();
        jPanelDigitalWest.setLayout(new BorderLayout());
        jRadioButtonDigital = new JRadioButton();
        jRadioButtonDigital.setSelected(true);
        jRadioButtonDigital.setFocusPainted(false);
        jPanelDigitalWest.add(jRadioButtonDigital, BorderLayout.NORTH);
        jPanelDigital.add(jPanelDigitalWest, BorderLayout.WEST);
        JPanel jPanelDigitalCenter = new JPanel();
        jPanelDigitalCenter.setLayout(new BorderLayout(0, 5));
        jComboBoxCategory = new JComboBox();
        jComboBoxCategory.setFont(jRadioButtonNonDigital.getFont());
        jPanelDigitalCenter.add(jComboBoxCategory, BorderLayout.NORTH);
        mimeSubtypeListModel = new DefaultTableModel();
        mimeSubtypeList = new JTable( mimeSubtypeListModel );
        mimeSubtypeList.setShowGrid( false );
        mimeSubtypeList.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        mimeSubtypeList.setFont(jRadioButtonNonDigital.getFont());
        mimeSubtypeList.getTableHeader().addMouseListener( 
            new MouseAdapter() {
                public void mouseReleased( MouseEvent e ) {
                    try {
                        Preferences.getInstance().setMimeSubtypeSortMode( ( Preferences.getInstance().getMimeSubtypeSortMode() + 1 ) % 3 );
                        updateMimeSubtypes();
                    }
                    catch( Exception ignore ) {
                    }
                }
            }
        );
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().add(mimeSubtypeList);
        jPanelDigitalCenter.add(jScrollPane, BorderLayout.CENTER);
        jPanelDigital.add(jPanelDigitalCenter, BorderLayout.CENTER);
        jPanelContent.add(jPanelDigital, BorderLayout.CENTER);

        ButtonGroup group = new ButtonGroup();
        group.add(jRadioButtonNonDigital);
        group.add(jRadioButtonDigital);

        jButtonOk = new JButton("OK");
        jButtonOk.setFont(jRadioButtonNonDigital.getFont());
        jButtonCancel = new JButton("cancel");
        jButtonCancel.setFont(jButtonOk.getFont());
        jPanelButton = new JPanel();
        jPanelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanelButton.add(jButtonOk);
        jPanelButton.add(jButtonCancel);
        cp.add(jPanelButton, BorderLayout.SOUTH);

        SymAction lSymAction = new SymAction();
        jRadioButtonNonDigital.addActionListener(lSymAction);
        jRadioButtonDigital.addActionListener(lSymAction);
        jComboBoxCategory.addActionListener(lSymAction);
        jButtonOk.addActionListener(lSymAction);
        jButtonCancel.addActionListener(lSymAction);

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogFormatSelectorRes", Preferences.getInstance().getLocale());
        setTitle(" " + resBundle.getString("title"));
        jButtonCancel.setText(resBundle.getString("cancel"));
        mimeSubtypeListLabel = resBundle.getString("subtypes");

        init();
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
            if (object == jComboBoxCategory)
                updateMimeSubtypes();
            else if (object == jRadioButtonNonDigital) {
                jComboBoxCategory.setEnabled(false);
                mimeSubtypeList.setEnabled(false);
            } else if (object == jRadioButtonDigital) {
                jComboBoxCategory.setEnabled(true);
                mimeSubtypeList.setEnabled(true);
            } else if (object == jButtonOk)
                jButtonOk_actionPerformed();
            else if (object == jButtonCancel)
                jButtonCancel_actionPerformed();
        }
    }

    void updateMimeSubtypes() {
        Object[] mimeTypeValues = Util.readFile(getClass(), jComboBoxCategory.getSelectedItem() + ".txt");
        switch( Preferences.getInstance().getMimeSubtypeSortMode() ) {
            case Preferences.MIME_SUBTYPE_SORT_MODE_ASC_SORTED : 
                Arrays.sort( mimeTypeValues ); 
                break;
            case Preferences.MIME_SUBTYPE_SORT_MODE_DESC_SORTED : 
                Arrays.sort( mimeTypeValues );
                Object[] reversed = new Object[ mimeTypeValues.length ];
                for( int i = 0; i < mimeTypeValues.length; i++ ) 
                    reversed[ i ] = mimeTypeValues[ mimeTypeValues.length - 1 - i ];
                mimeTypeValues = reversed;
                break;
        }
        Object[][] data = new Object[ mimeTypeValues.length ][];
        for( int i = 0; i < mimeTypeValues.length; i++ )
            data[ i ] = new Object[] { mimeTypeValues[ i ] };
        mimeSubtypeListModel.setDataVector( data, new String[] { mimeSubtypeListLabel } );
        DefaultTableCellRenderer leftAlignedHeaderRenderer = new DefaultTableCellRenderer();
        leftAlignedHeaderRenderer.setHorizontalAlignment( SwingConstants.LEFT );
        LookAndFeel.installColorsAndFont( leftAlignedHeaderRenderer, "TableHeader.background", "TableHeader.foreground", "TableHeader.font" );
        LookAndFeel.installBorder( mimeSubtypeList.getTableHeader(), "TableHeader.cellBorder" );
        mimeSubtypeList.getColumnModel().getColumn( 0 ).setHeaderRenderer( leftAlignedHeaderRenderer );
    }

    void jButtonOk_actionPerformed() {
        if (jRadioButtonNonDigital.isSelected())
            format = "non-digital";
        else {
            int selectedRow = mimeSubtypeList.getSelectedRow();
            if (selectedRow != -1)
                format = jComboBoxCategory.getSelectedItem() + "/" + mimeSubtypeListModel.getValueAt( selectedRow, 0 );
            else
                return;
        }
        bOk = true;
        setVisible(false);
    }

    void jButtonCancel_actionPerformed() {
        setVisible(false);
    }

    void init() {
        Object[] data = Util.readFile(getClass(), "formats.txt");
        for (int i = 0; i < data.length; i++)
            jComboBoxCategory.addItem(data[i]);
        updateMimeSubtypes();
    }
}





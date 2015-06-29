/*
 * Copyright (C) 2010  Frederic Bergeron (frederic.bergeron@licef.ca)
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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

class FileBrowser extends JPanel {

    public static final File TOP = new File( "TOP" );

    public FileBrowser() {
        setLayout( new BorderLayout( 4, 4 ) );

        textFieldLocation = new JTextField( "" );
        textFieldLocation.setEditable( false );

        buttonClose = new JButton( "X" );
        buttonClose.addActionListener( 
            new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    fireBrowserClosed();
                }
            }
        );

        panelLocation = new JPanel( new BorderLayout( 4, 4 ) );
        panelLocation.add( BorderLayout.CENTER, textFieldLocation );
        panelLocation.add( BorderLayout.EAST, buttonClose );

        listEntries = new JList() {
            private boolean processEvent(MouseEvent e) {
                int index = listEntries.locationToIndex(e.getPoint());
                return index > -1 && listEntries.getCellBounds(index, index).contains(e.getPoint());
            }

            @Override
            protected void processMouseEvent(MouseEvent e) {
                if (processEvent(e)) {
                    super.processMouseEvent(e);
                }
            }

            @Override
            protected void processMouseMotionEvent(MouseEvent e) {
                if (processEvent(e)) {
                    super.processMouseMotionEvent(e);
                }
            }
        };
        listEntries.setCellRenderer( new EntryRenderer() );
        listEntries.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        listEntries.addListSelectionListener( 
            new ListSelectionListener() {
                public void valueChanged( ListSelectionEvent e ) {
                    if( e.getValueIsAdjusting() == false ) 
                        changeValue();
                }
            }
        );
        scrollPaneEntries = new JScrollPane( listEntries );

        checkBoxShowHiddenFiles = new JCheckBox( "", Preferences.getInstance().isShowHiddenFiles() );
        checkBoxShowHiddenFiles.addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent e ) {
                try {
                    Preferences.getInstance().setShowHiddenFiles( checkBoxShowHiddenFiles.isSelected() );
                }
                catch( Exception e2 ) {
                    e2.printStackTrace();
                }
                update();
            }
        } );

        add( BorderLayout.CENTER, scrollPaneEntries );
        add( BorderLayout.NORTH, panelLocation );
        if( Util.isShowHiddenDirectoryOptionAvailable() )
            add( BorderLayout.SOUTH, checkBoxShowHiddenFiles );

        updateLocalization();

        addComponentListener( 
            new ComponentAdapter() {
                public void componentResized( ComponentEvent e ) {
                    int width = e.getComponent().getSize().width;
                    try {
                        Preferences.getInstance().setFileBrowserWidth( width );
                    }
                    catch( Exception e3 ) {
                        e3.printStackTrace();
                    }
                }
            }
        );
    }
    
    public void updateLocalization() {
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.FileBrowserRes", Preferences.getInstance().getLocale());
        checkBoxShowHiddenFiles.setText(resBundle.getString("showHiddenFiles"));
    }

    public void setFont( Font font ) {
        super.setFont( font );
        if( listEntries != null )
            listEntries.setFont( font );
        if( checkBoxShowHiddenFiles != null )
            checkBoxShowHiddenFiles.setFont( font );
    }

    public void update() {
        setCurrLocation( getCurrLocation() );
    }

    public void addFileBrowserListener( FileBrowserListener listener ) {
        listeners.addElement( listener );
    }

    public void removeFileBrowserListener( ListSelectionListener listener ) {
        listeners.removeElement( listener );
    }

    public void setCurrLocation( String currLoc ) {
        if( currLoc == null )
            return;

        File loc = new File( currLoc ); 
        boolean isTop = ( TOP.equals( loc )  );

        if( !isTop && !loc.exists() ) 
            loc = FileSystemView.getFileSystemView().getDefaultDirectory();

        this.currLoc = loc + "";

        File[] entries = null;
        if( isTop ) {
            textFieldLocation.setToolTipText( null );
            textFieldLocation.setText( null );
            textFieldLocation.setCaretPosition( 0 );
            
            entries = File.listRoots();
        }
        else {
            File dir = loc.isDirectory() ? loc : loc.getParentFile();

            textFieldLocation.setToolTipText( dir + "" );
            textFieldLocation.setText( dir + "" );
            textFieldLocation.setCaretPosition( 0 );

            entries = dir.listFiles( new DataFileFilter() );
        }
        DefaultListModel model = new DefaultListModel();
        if( !isTop )
            model.addElement( new File( ".." ) );
        if( entries != null ) {
            java.util.Arrays.sort( entries );
            for( int i = 0; i < entries.length; i++ ) {
                if( !isTop || entries[ i ].isDirectory() )
                    model.addElement( entries[ i ] );
            }
        }
        listEntries.setModel( model );

        if( !isTop && loc.isFile() ) {
            this.currFileLoc = loc + "";
            int selectionIndex = ((DefaultListModel)listEntries.getModel()).indexOf( loc );
            listEntries.getSelectionModel().setSelectionInterval( selectionIndex, selectionIndex );
        }
    }

    public String getCurrLocation() {
        return( currLoc );
    }

    public String getCurrFileLocation() {
        return( currFileLoc );
    }

    public void clearSelection() {
        listEntries.getSelectionModel().clearSelection();
        currFileLoc = null;
        if( currLoc != null ) {
            File tmpCurrLoc = new File( currLoc );
            if( tmpCurrLoc.isFile() )
                currLoc = tmpCurrLoc.getParentFile() + "";
        }
    }

    protected void fireFileSelected( File file ) {
        FileBrowserEvent e = new FileBrowserEvent( this, file );
        for( int i = listeners.size() - 1; i >=0; i-- ) {
            FileBrowserListener listener = (FileBrowserListener)listeners.elementAt( i );
            listener.fileSelected( e );
        }
    }

    protected void fireDirectorySelected( File file ) {
        FileBrowserEvent e = new FileBrowserEvent( this, file );
        for( int i = listeners.size() - 1; i >=0; i-- ) {
            FileBrowserListener listener = (FileBrowserListener)listeners.elementAt( i );
            listener.directorySelected( e );
        }
    }

    protected void fireBrowserClosed() {
        for( int i = listeners.size() - 1; i >=0; i-- ) {
            FileBrowserListener listener = (FileBrowserListener)listeners.elementAt( i );
            listener.browserClosed();
        }
    }

    private void changeValue() {
        Object value = listEntries.getSelectedValue();
        if( value != null ) {
            File file;
            File dir = new File( currLoc );
            if( dir.isFile() )
                dir = dir.getParentFile();
            if( "..".equals( value + "" ) )
                file = ( dir.getParentFile() == null ? TOP : dir.getParentFile() );
            else
                file = (File)value;

            if( file.isFile() )
                fireFileSelected( file );
            else {
                setCurrLocation( file.toString() );
                fireDirectorySelected( file );
            }
        }
    }

    private class EntryRenderer extends JLabel implements ListCellRenderer {

        public EntryRenderer() {

        }

        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            File file = (File)value;

            boolean isRoot = ( file.getName() == null || "".equals( file.getName() ));
            setText( isRoot ? file.getPath() : file.getName() );
            if( isRoot )
                setIcon( Util.rootIcon );
            else
                setIcon( file.isDirectory() ? Util.folderIcon : Util.fileIcon );

            if( isSelected ) {
                setBackground( list.getSelectionBackground() );
                setForeground( list.getSelectionForeground() );
            } else {
                setBackground( list.getBackground() );
                setForeground( list.getForeground() );
            }

            setEnabled( list.isEnabled() );
            setFont( list.getFont() );
            setOpaque( true );

            return( this );
        }

    }

    private JPanel      panelLocation;
    private JTextField  textFieldLocation;
    private JButton     buttonClose;
    
    private JList       listEntries;
    private JScrollPane scrollPaneEntries;
    private JCheckBox   checkBoxShowHiddenFiles;

    private String      currLoc;
    private String      currFileLoc;

    private Vector      listeners = new Vector();

}


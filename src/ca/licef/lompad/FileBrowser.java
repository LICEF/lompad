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

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class FileBrowser extends JPanel {

    public FileBrowser( String currDir ) {
        setLayout( new BorderLayout( 4, 4 ) );

        textFieldLocation = new JTextField( currDir );
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

        listEntries = new JList();
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
        add( BorderLayout.CENTER, scrollPaneEntries );
        add( BorderLayout.NORTH, panelLocation );

        setDirectory( currDir );
    }

    public void setFont( Font font ) {
        super.setFont( font );
        if( listEntries != null )
            listEntries.setFont( font );
    }

    public void addFileBrowserListener( FileBrowserListener listener ) {
        listeners.addElement( listener );
    }

    public void removeFileBrowserListener( ListSelectionListener listener ) {
        listeners.removeElement( listener );
    }

    public void setDirectory( String currDir ) {
        this.currDir = currDir;

        textFieldLocation.setToolTipText( currDir );
        textFieldLocation.setText( currDir );
        textFieldLocation.setCaretPosition( 0 );

        File dir = new File( currDir ); 
        File[] entries = dir.listFiles( new XMLFileFilter() );
        java.util.Arrays.sort( entries );
       
        DefaultListModel model = new DefaultListModel();
        model.addElement( new File( ".." ) );
        for( int i = 0; i < entries.length; i++ )
            model.addElement( entries[ i ] );

        listEntries.setModel( model );
    }

    public String getDirectory() {
        return( currDir );
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
            File dir = new File( currDir );
            if( "..".equals( value + "" ) )
                file = ( dir.getParentFile() == null ? dir : dir.getParentFile() );
            else
                file = (File)value;

            if( file.isDirectory() ) {
                setDirectory( file.toString() );
                fireDirectorySelected( file );
            }
            else
                fireFileSelected( file );
        }
    }

    private class EntryRenderer extends JLabel implements ListCellRenderer {

        public EntryRenderer() {

        }

        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            File file = (File)value;

            setText( file.getName() );
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

    private String      currDir;

    private Vector      listeners = new Vector();

}


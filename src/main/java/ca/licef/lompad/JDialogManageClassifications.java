/*
 * Copyright (C) 2005  Frederic Bergeron (frederic.bergeron@licef.ca)
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
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import licef.CommonNamespaceContext;

class JDialogManageClassifications extends JDialog {

    public JDialogManageClassifications(JFrame parent) {
        super(parent);
        Font defaultFont = new Font("Dialog", Font.PLAIN, 12);
        Border margin = BorderFactory.createEmptyBorder(5,5,5,5);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel jPanelMain = new JPanel( new BorderLayout() );
        JPanel jPanelCenter = new JPanel(new GridLayout(1,2,5,5));
        jPanelCenter.setBorder(margin);
        jListClassifs = new JList();
        jListClassifs.setFont(defaultFont);
        JScrollPane jScrollPaneClassifs = new JScrollPane( jListClassifs );
        JPanel jPanelClassifs = new JPanel( new BorderLayout(5,5) );
        jPanelClassifs.add( BorderLayout.CENTER, jScrollPaneClassifs );
        jTreeClassif = new ClassifTree();
        updateClassifTree( null );
        JScrollPane jScrollPaneClassifTree = new JScrollPane( jTreeClassif );
        jCheckBoxShowTaxumId = new JCheckBox( "", Preferences.getInstance().isShowTaxumId() );
        jCheckBoxShowTaxumId.setFont(defaultFont);
        jCheckBoxShowTaxumId.addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent e ) {
                try {
                    Preferences.getInstance().setShowTaxumId( jCheckBoxShowTaxumId.isSelected() );
                }
                catch( Exception e2 ) {
                    e2.printStackTrace();
                }
                update();
            }
        } );
        JPanel jPanelClassifTree = new JPanel( new BorderLayout(5,5) );
        jPanelClassifTree.add( BorderLayout.CENTER, jScrollPaneClassifTree);
        jPanelClassifTree.add( BorderLayout.SOUTH, jCheckBoxShowTaxumId );
        jPanelCenter.add( jPanelClassifs );
        jPanelCenter.add( jPanelClassifTree );
        JPanel jPanelSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        ActionAddClassif actionAddClassif = new ActionAddClassif();
        JButton buttonAddClassif = new JButton( actionAddClassif );
        buttonAddClassif.setFont(defaultFont);
        ActionRemoveClassif actionRemoveClassif = new ActionRemoveClassif();
        JButton buttonRemoveClassif = new JButton( actionRemoveClassif );
        buttonRemoveClassif.setFont(defaultFont);
        jPanelSouth.add( buttonAddClassif );
        jPanelSouth.add( buttonRemoveClassif );
        jPanelMain.add(BorderLayout.CENTER, jPanelCenter);
        jPanelMain.add(BorderLayout.SOUTH, jPanelSouth);
        JButton ok = new JButton();
        ok.setFont(defaultFont);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel jPanelButtons = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
        jPanelButtons.add(ok);
        cp.add(BorderLayout.CENTER, jPanelMain);
        cp.add(BorderLayout.SOUTH, jPanelButtons);
        setSize(400, 150);

        treeModels.clear();
        try {
            Classification.loadAll();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        
        listModelClassifs = new DefaultListModel();
        for( Classification classif : Classification.getAll() )
            listModelClassifs.addElement( classif );

        jListClassifs.setModel( listModelClassifs );

        listSelectionListenerClassifs  = new ListSelectionListenerClassifs();
        jListClassifs.addListSelectionListener( listSelectionListenerClassifs );

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogManageClassificationsRes", Preferences.getInstance().getLocale());
        setTitle(resBundle.getString("title"));
        jCheckBoxShowTaxumId.setText(resBundle.getString("showTaxumId"));
        actionAddClassif.putValue( Action.NAME, resBundle.getString("addClassif") );
        actionRemoveClassif.putValue( Action.NAME, resBundle.getString("removeClassif") );
        ok.setText(resBundle.getString("ok"));
    }

    public void setVisible( boolean isVisible ) {
        if( isVisible ) {
            Rectangle bounds = getParent().getBounds();
            Rectangle abounds = getBounds();
            setLocation(bounds.x + (bounds.width - abounds.width) / 2,
                    bounds.y + (bounds.height - abounds.height) / 2);
        }
        super.setVisible( isVisible );
    }

    private void update() {
        int selectedIndex = jListClassifs.getSelectedIndex();
        if( selectedIndex == -1 ) 
            updateClassifTree( null );
        else {
            Classification selectedClassif = (Classification)jListClassifs.getModel().getElementAt( selectedIndex );
            updateClassifTree( selectedClassif.getUrl() );
        }
    }

    private void updateClassifTree( String urlStr ) {
        String key = ( urlStr == null ? "" : urlStr );
        TreeModel model = treeModels.get( key );
        if( model == null ) {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( "Hidden Root Node" );
            if( urlStr == null ) 
                model = new DefaultTreeModel( rootNode );
            else {
                Classification classif = Classification.get( urlStr );
                model = new ClassifTreeModel( rootNode, classif.getModel() );
            }
            treeModels.put( key, model );
        }
        jTreeClassif.setModel( model );
        jTreeClassif.updateUI();
        
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)jTreeClassif.getModel().getRoot();
        jTreeClassif.expandPath( new TreePath( rootNode.getPath() ) );
    }

    class ActionAddClassif extends AbstractAction {

        public ActionAddClassif() {
            super( "AddClassif", Util.plusIcon ); 
        }

        public void actionPerformed( ActionEvent evt ) {
            File classifFile = Classification.doImportFile( JDialogManageClassifications.this );
            if( classifFile != null ) {
                try {
                    Classification classif = Classification.load( classifFile );
                    if( !listModelClassifs.contains( classif ) )
                        listModelClassifs.addElement( classif );
                    update();
                }
                catch( Exception e ) {
                    // Ignore files that we cannot handle properly.
                    e.printStackTrace();
                }
            }
        }

    }

    class ActionRemoveClassif extends AbstractAction {

        public ActionRemoveClassif() {
            super( "RemoveClassif", Util.minusIcon ); 
        }

        public void actionPerformed( ActionEvent evt ) {
            JDialogQuestion dialog = new JDialogQuestion( JDialogManageClassifications.this, "title", "confirmRemoveClassif" );
            dialog.setVisible( true );
            if( dialog.res == JDialogQuestion.YES ) {
                List<Classification> itemsToRemove = new ArrayList<Classification>();
                int[] indices = jListClassifs.getSelectedIndices();
                for( int i = 0; i < indices.length; i++ ) {
                    Classification classif = (Classification)jListClassifs.getModel().getElementAt( indices[ i ] );
                    if( classif.delete() )
                        itemsToRemove.add( classif );
                }
                
                jListClassifs.removeListSelectionListener( listSelectionListenerClassifs );
                for( Classification classif : itemsToRemove )
                    listModelClassifs.removeElement( classif );
                jListClassifs.addListSelectionListener( listSelectionListenerClassifs );
                update();
            }
        }

    }

    class ListSelectionListenerClassifs implements ListSelectionListener {
        public void valueChanged( ListSelectionEvent evt ) {
            if( !evt.getValueIsAdjusting() )
                update();
        }
    }

    private DefaultListModel listModelClassifs;
    private ListSelectionListener listSelectionListenerClassifs;
    private JList jListClassifs;
    private JTree jTreeClassif;
    private JCheckBox jCheckBoxShowTaxumId;
    private Map<String,TreeModel> treeModels = new HashMap<String,TreeModel>();

}

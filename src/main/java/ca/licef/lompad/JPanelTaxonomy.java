/*
 * Copyright (C) 2007 Alexis Miara (alexis.miara@licef.ca)
 * 
 * This file is part of LomPad.
 * 
 * PALOMA is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * PALOMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with PALOMA; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ca.licef.lompad;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.xml.sax.SAXException;

import licef.CommonNamespaceContext;
import licef.IOUtil;
import licef.reflection.Invoker;
import licef.reflection.ThreadInvoker;
import licef.StringUtil;
import licef.XMLUtil;

public class JPanelTaxonomy extends JPanel {

    private JDialogTaxonPathSelector parentDialog;
    JPanel jPanelClassifications;
    public JComboBox jComboBoxClassification;
    private JCheckBox jCheckBoxShowTaxumId;
    ArrayList trees = new ArrayList();
    ArrayList classificationSource = new ArrayList();
    ArrayList associatedPurpose = new ArrayList();

    public JPanelTaxonomy( JDialogTaxonPathSelector parentDialog ) {
        this.parentDialog = parentDialog;
        setLayout(new BorderLayout());
        Font defaultFont = new Font("Dialog", Font.PLAIN, 12);

        jComboBoxClassification = new JComboBox() {
            public Insets getInsets() {
                return new Insets(5, 2, 5, 2);
            }
        };
        jComboBoxClassification.setFocusable(false);
        jComboBoxClassification.setFont(new Font("Dialog", Font.PLAIN, 12));
        jCheckBoxShowTaxumId = new JCheckBox( "", Preferences.getInstance().isShowTaxumId() );
        jCheckBoxShowTaxumId.setFont(defaultFont);
        jCheckBoxShowTaxumId.addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent e ) {
                try {
                    Preferences.getInstance().setShowTaxumId( jCheckBoxShowTaxumId.isSelected() );
                }
                catch( Exception ex ) {
                    ex.printStackTrace();
                }
                update();
            }
        } );

        add(BorderLayout.NORTH, jComboBoxClassification);

        jPanelClassifications = new JPanel(new CardLayout());
        add(BorderLayout.CENTER, jPanelClassifications);
        add(BorderLayout.SOUTH, jCheckBoxShowTaxumId);

        initClassifications();

        if( Preferences.getInstance().getPrevSelectedClassif() != -1 ) {
            try {
                jComboBoxClassification.setSelectedIndex( Preferences.getInstance().getPrevSelectedClassif() );
            }
            catch( Exception e ) {
                // Do not select anything if the value is invalid.
            }
        }
        SymAction lSymAction = new SymAction();
        jComboBoxClassification.addActionListener(lSymAction);

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JPanelTaxonomyRes", Preferences.getInstance().getLocale());
        jCheckBoxShowTaxumId.setText(resBundle.getString("showTaxumId"));
    }

    public void setVisible(boolean b) {
        if (b)
            update();
    }

    public void update() {
        for (Iterator it = trees.iterator(); it.hasNext();) {
            JTree tree = (JTree) it.next();
            tree.clearSelection();
            tree.updateUI();
        }
    }

    private void initClassifications() {
        try {
            Classification.loadAll();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }

        trees.clear();
        classificationSource.clear();
        jPanelClassifications.removeAll();
        jComboBoxClassification.removeAllItems();

        int i = 0;
        for( Iterator<Classification> it = Classification.getAll().iterator(); it.hasNext(); i++ ) {
            Classification classif = it.next();
            try {
                initClassification( i, classif );
            }
            catch( Exception e ) {
                // Skip the classification if a problem occurs.
                e.printStackTrace();
            }
        }
        addImportClassifItem();
    }

    private void addImportClassifItem() {
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JPanelTaxonomyRes", Preferences.getInstance().getLocale());
        jComboBoxClassification.addItem( resBundle.getString( "ImportClassification" ) );
    }

    private void initClassification( int index, Classification classif ) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
        JPanel jPanelClassif = new JPanel(new BorderLayout());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode( "Hidden Root Node" );
        TreeModel model = new ClassifTreeModel( root, classif.getModel() );
        ClassifTree classifTree = new ClassifTree();
        classifTree.setModel( model );
        jPanelClassif.add( new JScrollPane( classifTree ), BorderLayout.CENTER );
        trees.add( classifTree );
        parentDialog.addTreeListener( classifTree );
        jPanelClassifications.add("" + index, jPanelClassif);
        
        ArrayList titleValues = new ArrayList();
        String[] languages = new String[] { "en", "fr" };
        for( int i = 0; i < languages.length; i++ ) { 
            titleValues.add( languages[ i ] );
            titleValues.add( classif.getTitle( languages[ i ] ) );
        }
        LocalizeValue localizeValue = new LocalizeValue( titleValues );

        jComboBoxClassification.addItem(localizeValue);
    }

    public int getSelectedIndex() {
        return jComboBoxClassification.getSelectedIndex();
    }

    public JTree getCurrentTree() {
        int index = getSelectedIndex();
        if (index != -1 && index < jComboBoxClassification.getItemCount() - 1 ) {
            JTree tree =  (JTree)trees.get(index);
            // bug fix: in jre7 , JTree.setRootVisible(false) renders whole tree invisible, must add a expandPath to reender tree
            tree.expandPath(new TreePath(((DefaultMutableTreeNode) tree.getModel().getRoot()).getPath()));
            return tree;
        }
        else
            return null;
    }

    public ArrayList getTrees() {
        return trees;
    }

    public Object[] getTaxonPath() {
        Object[] nodes = getCurrentTree().getSelectionPath().getPath();
        Object[] taxonPath = new Object[nodes.length - 1];
        for (int i = 1; i < nodes.length; i++) {
            LocalizeTaxon lt = (LocalizeTaxon) ((DefaultMutableTreeNode) nodes[i]).getUserObject();
            Object[] taxon = new Object[]{lt.id, lt.getOrderedTitles()};
            taxonPath[i - 1] = taxon;
        }
        return taxonPath;
    }

    class SymAction implements java.awt.event.ActionListener {
        public void actionPerformed(ActionEvent event) {
            Object object = event.getSource();
            if (object == jComboBoxClassification)
                jComboBoxClassification_actionPerformed( event );
        }
    }

    void jComboBoxClassification_actionPerformed( ActionEvent event ) {
        int selectedItem = jComboBoxClassification.getSelectedIndex();
        if( selectedItem == jComboBoxClassification.getItemCount() - 1 ) { 
            jComboBoxClassification.hidePopup(); // Required to prevent a bug on my laptop screen. - FB
            if( Classification.doImportFile( this ) != null )
                initClassifications();
        }
        else {
            try {
                Preferences.getInstance().setPrevSelectedClassif( selectedItem );
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
            CardLayout cardLayout = ((CardLayout)jPanelClassifications.getLayout());
            cardLayout.show(jPanelClassifications, selectedItem + "");
            for (Iterator it = trees.iterator(); it.hasNext();) {
                JTree tree = (JTree)it.next();
                tree.clearSelection();
                // bug fix: in jre7 , JTree.setRootVisible(false) renders whole tree invisible, must add a expandPath to reender tree
                tree.expandPath(new TreePath(((DefaultMutableTreeNode) tree.getModel().getRoot()).getPath()));
            }
        }
    }

    class LocalizeValue {
        ArrayList titles;

        public LocalizeValue(ArrayList titles) {
            this.titles = titles;
        }

        public String toString() {
            String lang = Preferences.getInstance().getLocale().getLanguage();
            if ("".equals(lang))
                lang = "en";
            int i = titles.indexOf(lang);
            if (i == -1) //first choice if language not defined
                i = 0;
            return (String) titles.get(i + 1);
        }
    }
}


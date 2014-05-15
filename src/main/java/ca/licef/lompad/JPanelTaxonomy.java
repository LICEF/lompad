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
import java.util.Hashtable;
import java.util.Iterator;
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
    ArrayList trees;
    ArrayList classificationSource;

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
                Preferences.getInstance().setShowTaxumId( jCheckBoxShowTaxumId.isSelected() );
                update();
            }
        } );

        add(BorderLayout.NORTH, jComboBoxClassification);

        jPanelClassifications = new JPanel(new CardLayout());
        add(BorderLayout.CENTER, jPanelClassifications);
        add(BorderLayout.SOUTH, jCheckBoxShowTaxumId);

        initClassifications();

        SymAction lSymAction = new SymAction();
        jComboBoxClassification.addActionListener(lSymAction);
        if( Preferences.getInstance().getPrevSelectedClassif() != -1 ) 
            jComboBoxClassification.setSelectedIndex( Preferences.getInstance().getPrevSelectedClassif() );

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
        trees = new ArrayList();
        classificationSource = new ArrayList();
        jPanelClassifications.removeAll();
        jComboBoxClassification.removeAllItems();

        String classifFolder = Util.getClassificationFolder();
        if( classifFolder != null ) {
            File[] classifFiles = new File( classifFolder ).listFiles();
            for( int i = 0, c = 0; i < classifFiles.length; i++ ) {
                try {
                    initClassification( c, classifFiles[ i ].toURI().toURL().toString() );
                    c++;
                }
                catch( Exception e ) {
                    // Skip the classification if a problem occurs.
                    e.printStackTrace();
                }
            }
        }
        addImportClassifItem();
    }

    private void addImportClassifItem() {
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JPanelTaxonomyRes", Preferences.getInstance().getLocale());
        jComboBoxClassification.addItem( resBundle.getString( "ImportClassification" ) );
    }

    private void initClassification( int index, String url ) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
        JTree classificationTree = null;
        DefaultTreeModel model;
        DefaultMutableTreeNode root = null;
        Hashtable nodes = new Hashtable();

        InputStream is = (InputStream) (new URL(url)).getContent();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setCoalescing(true); //convert CDATA node to Text node
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        NodeList list = document.getDocumentElement().getChildNodes();
        
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if( CommonNamespaceContext.skosNSURI.equals( node.getNamespaceURI() ) ) {
                if( Node.ELEMENT_NODE == node.getNodeType() ) {
                    if( "ConceptScheme".equals( node.getLocalName() ) ) {
                        JPanel jPanelClassif = new JPanel(new BorderLayout());
                        ArrayList titles = new ArrayList();
                        NodeList childs = node.getChildNodes();
                        for (int j = 0; j < childs.getLength(); j++) {
                            Node child = childs.item(j);
                            if (CommonNamespaceContext.skosNSURI.equals( child.getNamespaceURI() ) && 
                                Node.ELEMENT_NODE == child.getNodeType() &&
                                "prefLabel".equals( child.getLocalName() ) ) {
                                Node n = child.getAttributes().getNamedItem( "xml:lang" );
                                String lang = ( n == null ? "" : n.getNodeValue() );
                                int indexOfDash = lang.indexOf( "-" );
                                if( indexOfDash != -1 )
                                    lang = lang.substring( 0, indexOfDash );
                                titles.add(lang);
                                titles.add(child.getFirstChild().getNodeValue().trim());
                            }
                        }
                        jComboBoxClassification.addItem(new LocalizeValue(titles));

                        classificationSource.add(titles);
                        classificationSource.add(node.getAttributes().getNamedItem("rdf:about").getNodeValue());
                        classificationTree = ClassifUtil.createTree();
                        jPanelClassif.add(new JScrollPane(classificationTree), BorderLayout.CENTER);
                        trees.add(classificationTree);
                        parentDialog.addTreeListener( classificationTree );
                        jPanelClassifications.add("" + index, jPanelClassif);

                        model = (DefaultTreeModel) classificationTree.getModel();
                        root = ((DefaultMutableTreeNode) model.getRoot());
                    } 
                    else if( "Concept".equals( node.getLocalName() ) ) {
                        ArrayList titles = new ArrayList();
                        NodeList childs = node.getChildNodes();
                        String id = node.getAttributes().getNamedItem("rdf:about").getNodeValue();
                        String parentId = null;
                        for (int j = 0; j < childs.getLength(); j++) {
                            Node child = childs.item(j);
                            if (CommonNamespaceContext.skosNSURI.equals( child.getNamespaceURI() ) &&
                                Node.ELEMENT_NODE == child.getNodeType() ) {
                                if( "topConceptOf".equals( child.getLocalName() ) ) {
                                    // parentId stays null.
                                }
                                else if( "broader".equals( child.getLocalName() ) ) {
                                    Node n = child.getAttributes().getNamedItem("rdf:resource");
                                    if (n != null)
                                        parentId = n.getNodeValue();
                                }
                                else if ("prefLabel".equals( child.getLocalName() ) && child.getFirstChild() != null ) {
                                    Node n = child.getAttributes().getNamedItem("xml:lang");
                                    String lang = ( n == null ? "en" : n.getNodeValue() );
                                    int indexOfDash = lang.indexOf( "-" );
                                    if( indexOfDash != -1 )
                                        lang = lang.substring( 0, indexOfDash );
                                    String title = child.getFirstChild().getNodeValue().trim();
                                    titles.add(lang);
                                    titles.add(title);
                                }
                            }
                        }

                        String taxonPathId = ClassifUtil.retrieveTaxonPathId( id );
                        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new LocalizeTaxon(taxonPathId, titles));
                        nodes.put(id, newChild);
                        if (parentId == null)
                            root.add(newChild);
                        else {
                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodes.get(parentId);
                            if( parent != null )
                                parent.add(newChild);
                        }
                    }

                }
            }
        }
        classificationTree.updateUI();
    }

    public int getSelectedIndex() {
        return jComboBoxClassification.getSelectedIndex();
    }

    public ArrayList getTitles(int index) {
        return (ArrayList) classificationSource.get(2 * index);
    }

    public String getURL(int index) {
        return (String) classificationSource.get(2 * index + 1);
    }

    public String getAssociatedPurpose(int pos) {
        return (String)associatedPurpose.get(pos);
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
            Object[] taxon = new Object[]{lt.id, lt.titles};
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
            if( ClassifUtil.doImportFile( this ) != null )
                initClassifications();
        }
        else {
            Preferences.getInstance().setPrevSelectedClassif( selectedItem );
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


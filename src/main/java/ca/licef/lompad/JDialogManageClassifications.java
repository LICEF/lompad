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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
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
        jTreeClassif = ClassifUtil.createTree();
        JScrollPane jScrollPaneClassifTree = new JScrollPane( jTreeClassif );
        jCheckBoxShowTaxumId = new JCheckBox( "", Preferences.getInstance().isShowTaxumId() );
        jCheckBoxShowTaxumId.setFont(defaultFont);
        jCheckBoxShowTaxumId.addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent e ) {
                Preferences.getInstance().setShowTaxumId( jCheckBoxShowTaxumId.isSelected() );
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
        setSize(250, 125);

        ClassifEntry[] classifs = getClassifications();
        
        listModelClassifs = new DefaultListModel();
        for( int i = 0; i < classifs.length; i++ )
            listModelClassifs.addElement( classifs[ i ] );

        jListClassifs.setModel( listModelClassifs );

        listSelectionListenerClassifs  = new ListSelectionListenerClassifs();
        jListClassifs.addListSelectionListener( listSelectionListenerClassifs );

        //Localization
        ResourceBundle resBundle = ResourceBundle.getBundle("properties.JDialogManageClassificationsRes", Util.locale);
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
            ClassifEntry selectedEntry = (ClassifEntry)jListClassifs.getModel().getElementAt( selectedIndex );
            updateClassifTree( selectedEntry.getUrl() );
        }
    }

    private void updateClassifTree( String url ) {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)jTreeClassif.getModel().getRoot();
        rootNode.removeAllChildren();
        if( url != null ) {
            try {
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
                        if( Node.ELEMENT_NODE == node.getNodeType() && "Concept".equals( node.getLocalName() ) ) {
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
                                rootNode.add(newChild);
                            else {
                                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodes.get(parentId);
                                if( parent != null )
                                    parent.add(newChild);
                            }
                        }
                    }
                }
            }
            catch( Exception e ) {
                // Do not update the tree if a problem occurs.
                e.printStackTrace();
            }
        }
        jTreeClassif.updateUI();
        jTreeClassif.expandPath( new TreePath( rootNode.getPath() ) );
    }

    private ClassifEntry[] getClassifications() {
        List classifs = new ArrayList();
        String classifFolder = Util.getClassificationFolder();
        if( classifFolder != null ) {
            File[] classifFiles = new File( classifFolder ).listFiles();
            for( int i = 0; i < classifFiles.length; i++ ) {
                try {
                    ClassifEntry classifEntry = retrieveClassifEntry( classifFiles[ i ] );
                    classifs.add( classifEntry );
                }
                catch( Exception e ) {
                    // Skip the classification if a problem occurs.
                    e.printStackTrace();
                }
            }
        }

        ClassifEntry[] classifsAsArray = new ClassifEntry[ classifs.size() ];
        classifs.toArray( classifsAsArray );
        return( classifsAsArray );
    }

    private ClassifEntry retrieveClassifEntry( File classifFile ) throws Exception {
        ClassifEntry entry = null;

        URL url = classifFile.toURI().toURL();
        InputStream is = (InputStream) url.getContent();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setCoalescing(true); //convert CDATA node to Text node
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        NodeList list = document.getDocumentElement().getChildNodes();

        for (int j = 0; j < list.getLength(); j++) {
            Node node = list.item(j);
            if( CommonNamespaceContext.skosNSURI.equals( node.getNamespaceURI() ) ) {
                if( Node.ELEMENT_NODE == node.getNodeType() && "ConceptScheme".equals( node.getLocalName() ) ) {
                    String title = null;
                    NodeList childs = node.getChildNodes();
                    for (int k = 0; k < childs.getLength(); k++) {
                        Node child = childs.item(k);
                        if (CommonNamespaceContext.skosNSURI.equals( child.getNamespaceURI() ) && 
                            Node.ELEMENT_NODE == child.getNodeType() &&
                            "prefLabel".equals( child.getLocalName() ) ) {
                            String classifTitle = child.getFirstChild().getNodeValue().trim();
                            Node n = child.getAttributes().getNamedItem( "xml:lang" );
                            String classifLang = ( n == null ? "" : n.getNodeValue() );
                            int indexOfDash = classifLang.indexOf( "-" );
                            if( indexOfDash != -1 )
                                classifLang = classifLang.substring( 0, indexOfDash );

                            String lang = Util.locale.getLanguage();
                            if ("".equals(lang))
                                lang = "en";

                            // We find a title that matches the interface language.
                            // We take the first title if nothing better comes up.
                            if( title == null || classifLang.equals( lang ) ) {
                                title = classifTitle;
                                // No need to search further.
                                if( classifLang.equals( lang ) )
                                    break;
                            }
                        }
                    }
                    entry = new ClassifEntry( url.toString(), title );
                    break;
                } 
            }
        }
        return( entry );
    }

    class ClassifEntry {
        
        public ClassifEntry( String url, String title ) {
            this.url = url;
            this.title = title;
        }

        public String getUrl() {
            return( url );
        }

        public String getTitle() {
            return( title );
        }

        public String toString() {
            return( title );
        }

        public int hashCode() {
            return( url.hashCode() );
        }

        public boolean equals( Object obj ) {
            if( obj == this )
                return( true );
            if( !( obj instanceof ClassifEntry ) )
                return( false );
            ClassifEntry entry = (ClassifEntry)obj;
            return( url.equals( entry.getUrl() ) && title.equals( entry.getTitle() ) );
        }

        private String url;
        private String title;

    }

    class ActionAddClassif extends AbstractAction {

        public ActionAddClassif() {
            super( "AddClassif", Util.plusIcon ); 
        }

        public void actionPerformed( ActionEvent evt ) {
            File classifFile = ClassifUtil.doImportFile( JDialogManageClassifications.this );
            if( classifFile != null ) {
                try {
                    ClassifEntry entry = retrieveClassifEntry( classifFile );
                    listModelClassifs.addElement( entry );
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
            List<ClassifEntry> itemsToRemove = new ArrayList<ClassifEntry>();
            int[] indices = jListClassifs.getSelectedIndices();
            for( int i = 0; i < indices.length; i++ ) {
                try {
                    ClassifEntry entry = (ClassifEntry)jListClassifs.getModel().getElementAt( indices[ i ] );
                    File file = new File( new URI( entry.getUrl().toString() ) );
                    if( file.toString().indexOf( Util.getClassificationFolder() ) == -1 || file.delete() ) 
                        itemsToRemove.add( entry );
                }
                catch( URISyntaxException e ) {
                    e.printStackTrace();
                }
            }
            
            jListClassifs.removeListSelectionListener( listSelectionListenerClassifs );
            for( ClassifEntry entry : itemsToRemove )
                listModelClassifs.removeElement( entry );
            jListClassifs.addListSelectionListener( listSelectionListenerClassifs );
            update();
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

}

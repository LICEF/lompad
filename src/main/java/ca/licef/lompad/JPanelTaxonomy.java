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
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class JPanelTaxonomy extends JPanel {

    JPanel jPanelClassifications;
    public JComboBox jComboBoxClassification;
    ArrayList trees;
    ArrayList classificationSource;

    ArrayList associatedPurpose = new ArrayList();

    public JPanelTaxonomy(String taxonomyURL) {

        setLayout(new BorderLayout());

        trees = new ArrayList();
        classificationSource = new ArrayList();

        jComboBoxClassification = new JComboBox() {
            public Insets getInsets() {
                return new Insets(5, 2, 5, 2);
            }
        };
        jComboBoxClassification.setFocusable(false);
        jComboBoxClassification.setFont(new Font("Dialog", Font.PLAIN, 12));
        add(BorderLayout.NORTH, jComboBoxClassification);

        jPanelClassifications = new JPanel(new CardLayout());
        add(BorderLayout.CENTER, jPanelClassifications);

        initClassifications(taxonomyURL);

        SymAction lSymAction = new SymAction();
        jComboBoxClassification.addActionListener(lSymAction);

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

    private void initClassifications(String taxonomyURL) {
        Object[] urls = Util.readFile(taxonomyURL);
        // Read default classifications when no url is specified.
        if (urls == null) {
            urls = Util.readFile( getClass(), "classification.txt" );
            if( urls == null )
                return;
        }
        for (int i = 0, j = 0; i < urls.length; i++) {
            String url = (String) urls[i];
            int index = url.lastIndexOf("#");
            if (index != -1)
                associatedPurpose.add(url.substring(index + 1));
            url = url.substring(0, index);
            try {
                initClassification(j, url);
                j++;
            } catch (Exception e) {
                System.err.println( "Cannot read classification at url=" + url );
                e.printStackTrace();
            }
        }
    }

    private void initClassification(int index, String url) throws Exception {
        JTree classificationTree = null;
        DefaultTreeModel model;
        DefaultMutableTreeNode root = null;
        Hashtable nodes = new Hashtable();

        InputStream is = (InputStream) (new URL(url)).getContent();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setCoalescing(true); //convert CDATA node to Text node
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        NodeList list = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("owl:Ontology")) {
                    JPanel jPanelClassif = new JPanel(new BorderLayout());
                    ArrayList titles = new ArrayList();
                    NodeList childs = node.getChildNodes();
                    for (int j = 0; j < childs.getLength(); j++) {
                        Node child = childs.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            if (child.getNodeName().equals("rdfs:label")) {
                                Node n = child.getAttributes().getNamedItem("xml:lang");
                                titles.add(n == null ? "" : n.getNodeValue());
                                titles.add(child.getFirstChild().getNodeValue().trim());
                            }
                        }
                    }
                    jComboBoxClassification.addItem(new LocalizeValue(titles));

                    classificationSource.add(titles);
                    classificationSource.add(url);
                    classificationTree = createTree();
                    jPanelClassif.add(new JScrollPane(classificationTree), BorderLayout.CENTER);
                    trees.add(classificationTree);

                    jPanelClassifications.add("" + index, jPanelClassif);

                    model = (DefaultTreeModel) classificationTree.getModel();
                    root = ((DefaultMutableTreeNode) model.getRoot());


                } else if (node.getNodeName().equals("owl:Class")) {
                    ArrayList titles = new ArrayList();
                    NodeList childs = node.getChildNodes();
                    String id = node.getAttributes().getNamedItem("rdf:ID").getNodeValue();
                    String parentId = null;
                    for (int j = 0; j < childs.getLength(); j++) {
                        Node child = childs.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            if (child.getNodeName().equals("rdfs:subClassOf")) {
                                Node n = child.getAttributes().getNamedItem("rdf:resource");
                                if (n != null)
                                    parentId = n.getNodeValue().substring(1);
                            } else if (child.getNodeName().equals("rdfs:label")) {
                                Node n = child.getAttributes().getNamedItem("xml:lang");
                                titles.add(n.getNodeValue());
                                titles.add(child.getFirstChild().getNodeValue().trim());
                            }
                        }
                    }
                    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(new LocalizeTaxon(id, titles));
                    nodes.put(id, newChild);
                    if (parentId == null)
                        root.add(newChild);
                    else {
                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodes.get(parentId);
                        parent.add(newChild);
                    }
                }
            }
        }
           classificationTree.updateUI();
    }

    public JTree createTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("hidden");

        JTree tree = new JTree(top) {
            public Insets getInsets() {
                return new Insets(2, 2, 0, 0);
            }
        };

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        tree.setCellRenderer(renderer);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);

        return tree;
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
        if (index != -1){
            JTree tree =  (JTree)trees.get(index);
            // bug fix: in jre7 , JTree.setRootVisible(false) renders whole tree invisible, must add a expandPath to reender tree
             tree.expandPath(new TreePath(((DefaultMutableTreeNode) tree.getModel().getRoot()).getPath()));

         return tree;

        
        }else
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
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == jComboBoxClassification)
                jComboBoxClassification_actionPerformed();
        }
    }


    void jComboBoxClassification_actionPerformed() {
        ((CardLayout)jPanelClassifications.getLayout()).show(
                jPanelClassifications, jComboBoxClassification.getSelectedIndex() + "");
        for (Iterator it = trees.iterator(); it.hasNext();) {
            JTree tree = (JTree)it.next();
            tree.clearSelection();
            // bug fix: in jre7 , JTree.setRootVisible(false) renders whole tree invisible, must add a expandPath to reender tree
           tree.expandPath(new TreePath(((DefaultMutableTreeNode) tree.getModel().getRoot()).getPath()));

        }
    }

    class LocalizeValue {
        ArrayList titles;

        public LocalizeValue(ArrayList titles) {
            this.titles = titles;
        }

        public String toString() {
            String lang = Util.locale.getLanguage();
            if ("".equals(lang))
                lang = "en";
            int i = titles.indexOf(lang);
            if (i == -1) //first choice if language not defined
                i = 0;
            return (String) titles.get(i + 1);
        }
    }
}


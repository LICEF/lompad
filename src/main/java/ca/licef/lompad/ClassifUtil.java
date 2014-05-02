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

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import licef.CommonNamespaceContext;
import licef.IOUtil;

class ClassifUtil {

    public static JTree createTree() {
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

    public static String retrieveTaxonPathId( String id ) {
        int indexOfHash =  id.indexOf( "#" );
        if( indexOfHash != -1 )
            return( id.substring( indexOfHash + 1 ) );
        
        int indexOfLastSlash = id.lastIndexOf( "/" );
        if( indexOfLastSlash != -1 )
            return( id.substring( indexOfLastSlash + 1 ) );

        return( id );
    }

    public static File doImportFile( Component parent ) {
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog( parent );
        if( returnVal == JFileChooser.APPROVE_OPTION ) {
            String classifIdentifier = null;
            try {
                classifIdentifier = retrieveIdentifier( chooser.getSelectedFile() );
            }
            catch( Exception e ) {
                ResourceBundle resBundle = ResourceBundle.getBundle("properties.ClassifUtilRes", Util.locale);
                JOptionPane.showMessageDialog( parent, 
                    resBundle.getString( "ClassifNameNotFound" ), resBundle.getString( "Error" ), 
                        JOptionPane.ERROR_MESSAGE );
                return( null );
            }

            importFile( chooser.getSelectedFile(), classifIdentifier + ".rdf" );
            return( chooser.getSelectedFile() );
        }
        else
            return( null );
    }
    
    public static void importFile( File sourceFile, String classifFilename ) {
        File outputFile = new File( Util.getClassificationFolder(), classifFilename );
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream( new FileInputStream( sourceFile ) );
            bos = new BufferedOutputStream( new FileOutputStream( outputFile ) );
            IOUtil.copy( bis, bos );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        finally {
            try {
                if( bis != null )
                    bis.close();
            }
            catch( IOException e ) {
                e.printStackTrace();
            }
            try {
                if( bos != null )
                    bos.close();
            }
            catch( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public static String retrieveIdentifier( File classifFile ) throws Exception {
        InputStream is = new FileInputStream( classifFile );
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
                        ArrayList titles = new ArrayList();
                        NodeList childs = node.getChildNodes();
                        for (int j = 0; j < childs.getLength(); j++) {
                            Node child = childs.item(j);
                            if (CommonNamespaceContext.skosNSURI.equals( child.getNamespaceURI() ) && 
                                Node.ELEMENT_NODE == child.getNodeType() &&
                                "prefLabel".equals( child.getLocalName() ) && child.getFirstChild() != null ) {
                                String value = child.getFirstChild().getNodeValue().trim();
                                // Make sure that the identifier can be a proper filename.
                                value = value.replaceAll( "/", "_" ).replaceAll( "\\\\", "_" ).replaceAll( "\\*", "_" ).replaceAll( "\\?", "_" );
                                return( value );
                            }
                        }
                    }
                }
            }
        }
        return( null );
    }

}


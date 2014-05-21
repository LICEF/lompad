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

import licef.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
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
        int indexOfHash =  id.indexOf( "#" ); //uri like http://domain/voc#concept
        if( indexOfHash != -1 )
            return( id.substring( indexOfHash + 1 ) );
        
        String[] array = StringUtil.split(id, '/');
        int size = array.length;
        if( size > 0 ) {
            if (!"".equals(array[size - 1]))
                return (array[size - 1]);  //uri like http://domain/voc/concept
            else if (size > 1) {
                return (array[size - 2]);  //uri like http://domain/voc/concept/
            }
        }

        return( id );
    }

    public static File doImportFile( Component parent ) {
        JFileChooser chooser = new JFileChooser();
        if( Preferences.getInstance().getPrevClassifDir() != null )
            chooser.setCurrentDirectory( Preferences.getInstance().getPrevClassifDir() );
        int returnVal = chooser.showOpenDialog( parent );
        if( returnVal == JFileChooser.APPROVE_OPTION ) {
            try {
                Preferences.getInstance().setPrevClassifDir( chooser.getCurrentDirectory() );
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
            String classifIdentifier = null;
            try {
                classifIdentifier = retrieveIdentifier( chooser.getSelectedFile() );
            }
            catch( Exception e ) {
                String msg = "classifImportFailed";
                if( "Classification identifier not found.".equals( e.getMessage() ) )
                    msg = "classifIdentifierNotFound";
                JDialogAlert dialog = new JDialogAlert( Util.getTopJFrame( (Container)parent ), "titleErr", msg );
                dialog.setSize( 320, 140 ); 
                dialog.setVisible( true );
                return( null );
            }

            File localFile = importFile( chooser.getSelectedFile(), classifIdentifier + ".rdf" );
            return( localFile );
        }
        else
            return( null );
    }
    
    public static File importFile( File sourceFile, String classifFilename ) {
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
        return( outputFile );
    }

    public static String retrieveIdentifier( File classifFile ) throws FileNotFoundException, ParserConfigurationException, SAXException, Exception {
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
                        Node aboutNode = node.getAttributes().getNamedItem( "rdf:about" );
                        if( aboutNode != null ) {
                            String uri = aboutNode.getNodeValue();
                            String sha1 = DigestUtils.shaHex( uri );
                            return( sha1 );
                        }
                    }
                }
            }
        }
        throw( new Exception( "Classification identifier not found." ) );
    }

}


/*
 * Copyright (C) 2014  Frederic Bergeron (frederic.bergeron@licef.ca)
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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Preferences {

    public static final int MIME_SUBTYPE_SORT_MODE_UNSORTED = 0;
    public static final int MIME_SUBTYPE_SORT_MODE_ASC_SORTED = 1;
    public static final int MIME_SUBTYPE_SORT_MODE_DESC_SORTED = 2;

    public static Preferences getInstance() {
        if( instance == null )
            instance = new Preferences();
        return( instance );
    }

    public void load() throws Exception {
        File prefsFile = new File( Util.getDataFolder(), "prefs.xml" );
        if( prefsFile.exists() )
            fromXML( new BufferedInputStream( new FileInputStream( prefsFile ) ) );
    }

    public void save() throws Exception {
        File prefsFile = new File( Util.getDataFolder(), "prefs.xml" );
        FileOutputStream fos = new FileOutputStream( prefsFile );
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( fos, "UTF-8" ) );
        writer.write( toXML() );
        writer.flush();
        writer.close();
    }

    public boolean isShowHiddenFiles() {
        return( isShowHiddenFilesEnabled );
    }

    public void setShowHiddenFiles( boolean isShowHiddenFilesEnabled ) throws Exception {
        this.isShowHiddenFilesEnabled = isShowHiddenFilesEnabled;
        save();
    }

    public int getMimeSubtypeSortMode() {
        return( mimeSubtypeSortMode );
    }

    public void setMimeSubtypeSortMode( int mimeSubtypeSortMode ) throws Exception {
        this.mimeSubtypeSortMode = mimeSubtypeSortMode;
        save();
    }

    public boolean isShowTaxumId() {
        return( isShowTaxumIdEnabled );
    }

    public void setShowTaxumId( boolean isShowTaxumIdEnabled ) throws Exception {
        this.isShowTaxumIdEnabled = isShowTaxumIdEnabled;
        save();
    }

    public File getPrevClassifDir() {
        return( prevClassifDir );
    }

    public void setPrevClassifDir( File prevClassifDir ) throws Exception {
        this.prevClassifDir = prevClassifDir;
        save();
    }

    public File getWorkingDir() {
        return( workingDir );
    }

    public void setWorkingDir( File workingDir ) throws Exception {
        this.workingDir = workingDir;
        save();
    }

    public File getFileBrowserLocation() {
        return( fileBrowserLoc );
    }

    public void setFileBrowserLocation( File fileBrowserLoc ) throws Exception {
        this.fileBrowserLoc = fileBrowserLoc;
        save();
    }

    public boolean isFileBrowserOpened() {
        return( isFileBrowserOpen );
    }

    public void setFileBrowserOpened( boolean isFileBrowserOpen ) throws Exception {
        this.isFileBrowserOpen = isFileBrowserOpen;
        save();
    }

    public String getPrevSelectedClassif() {
        return( prevSelectedClassif );
    }

    public void setPrevSelectedClassif( String prevSelectedClassif ) throws Exception {
        this.prevSelectedClassif = prevSelectedClassif;
        save();
    }

    public Locale getLocale() {
        return( locale );
    }

    public void setLocale( Locale locale ) throws Exception {
        this.locale = locale;
        save();
    }

    public String getApplicationProfileView() {
        return( applProfileView );
    }

    public void setApplicationProfileView( String applProfileView ) throws Exception {
        this.applProfileView = applProfileView;
        save();
    }

    public String toXML() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
        xml += "<prefs>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "isShowHiddenFiles", isShowHiddenFilesEnabled + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "mimeSubtypeSortMode", mimeSubtypeSortMode + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "isShowTaxumId", isShowTaxumIdEnabled + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "prevClassifDir", prevClassifDir + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "workingDir", workingDir + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "fileBrowserLoc", fileBrowserLoc + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "fileBrowserOpened", isFileBrowserOpen + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "prevSelectedClassif", prevSelectedClassif + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "locale", ( locale == Locale.FRENCH ? "fr" : "en" ) + "" ) + "/>\n";
        xml += "  <pref " + getKeyValueAsXmlAttributes( "applProfView", applProfileView ) + "/>\n";
        xml += "</prefs>\n";
        return xml;
    }

    public void fromXML(InputStream is) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setCoalescing(true); //convert CDATA node to Text node
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);

        NodeList list = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;
                String tagName = e.getTagName().toLowerCase();
                if( "pref".equals( tagName ) ) {
                    NamedNodeMap attr = e.getAttributes();
                    if( attr != null ) {
                        Node keyNode = attr.getNamedItem( "key" );
                        String key = ( keyNode == null ? null : keyNode.getNodeValue() );
                        Node valueNode = attr.getNamedItem( "value" );
                        String value = ( valueNode == null ? null : valueNode.getNodeValue() );
                        if( "isShowHiddenFiles".equals( key ) )
                            isShowHiddenFilesEnabled = Boolean.parseBoolean( value );
                        else if( "mimeSubtypeSortMode".equals( key ) )
                            mimeSubtypeSortMode = Integer.parseInt( value );
                        else if( "isShowTaxumId".equals( key ) )
                            isShowTaxumIdEnabled = Boolean.parseBoolean( value );
                        else if( "prevClassifDir".equals( key ) )
                            prevClassifDir = new File( value );
                        else if( "workingDir".equals( key ) )
                            workingDir = new File( value );
                        else if( "fileBrowserLoc".equals( key ) )
                            fileBrowserLoc = new File( value );
                        else if( "fileBrowserOpened".equals( key ) )
                            isFileBrowserOpen = Boolean.parseBoolean( value );
                        else if( "prevSelectedClassif".equals( key ) )
                            prevSelectedClassif = value;
                        else if( "locale".equals( key ) ) {
                            if( "fr".equals( value ) )
                                locale = Locale.FRENCH;
                            else
                                locale = Locale.ENGLISH;
                        }
                        else if( "applProfView".equals( key ) )
                            applProfileView = value;
                    }
                }
            }
        }
    }

    private Preferences() {
    }

    private String getKeyValueAsXmlAttributes( String key, String value ) {
        return( "key=\"" + key + "\" value=\"" + value + "\"" );
    }

    private boolean isShowHiddenFilesEnabled;
    private int mimeSubtypeSortMode = MIME_SUBTYPE_SORT_MODE_UNSORTED;
    private boolean isShowTaxumIdEnabled;
    private File prevClassifDir = null;
    private File workingDir  = FileSystemView.getFileSystemView().getDefaultDirectory();
    private File fileBrowserLoc = FileSystemView.getFileSystemView().getDefaultDirectory();
    private boolean isFileBrowserOpen;
    private String prevSelectedClassif; 
    private Locale locale = Locale.FRENCH;
    private String applProfileView = "IEEE";

    private static Preferences instance;

}

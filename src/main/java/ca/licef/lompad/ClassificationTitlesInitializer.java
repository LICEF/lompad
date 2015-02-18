/*
 * Copyright (C) 2015  Frederic Bergeron (frederic.bergeron@licef.ca)
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

import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class ClassificationTitlesInitializer extends DefaultHandler {

    public ClassificationTitlesInitializer( InputStream inputStream ) throws IOException, ParserConfigurationException, SAXException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware( true );
        SAXParser parser = parserFactory.newSAXParser();
        try {
            parser.parse( new InputSource( inputStream ), this );
        }
        catch( SAXException e ) {
            if( !"EOP".equals( e.getMessage() )  )
                throw( e );
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attribs) throws SAXException {
        if( areStringsFound ) 
            return;

        if( "rdf:Description".equals( qName ) ) {
            isInsideResource = true;
            strings.clear();
        }
        else if( "rdf:type".equals( qName ) && isInsideResource && !isInsideConceptScheme ) {
            String type = attribs.getValue( "rdf:resource" );
            if( "http://www.w3.org/2004/02/skos/core#ConceptScheme".equals( type ) )
                isInsideConceptScheme = true;
        }
        else if( "skos:prefLabel".equals( qName ) && isInsideResource ) {
            lang = attribs.getValue( "xml:lang" );
            isInsidePrefLabel = true;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if( areStringsFound )
            return;

        if( isInsidePrefLabel ) {
            String title = new String(ch, start, length).trim();
            strings.put( lang == null ? "" : lang, title );
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if( areStringsFound )
            return;

        if( "rdf:Description".equals( qName ) ) {
            if( isInsideConceptScheme ) {
                areStringsFound = true;
                throw( new SAXException( "EOP" ) ); // This will terminate the parsing ASAP.
            }
            isInsideResource = false;
            isInsideConceptScheme = false;
        }
        else if( "skos:prefLabel".equals( qName ) && isInsideResource ) {
            isInsidePrefLabel = false;
            lang = null;
        }
    }

    public LangStrings getTitles() {
        return( new LangStrings( strings ) );
    }

    private boolean isInsideResource = false;
    private boolean isInsideConceptScheme = false;
    private boolean isInsidePrefLabel = false;
    private boolean areStringsFound = false;
    private String lang;

    private Map<String,String> strings = new HashMap<String,String>();

}

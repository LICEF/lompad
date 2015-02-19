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

import java.awt.Component;
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.xml.transform.stream.StreamSource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Node;
import licef.IOUtil;
import licef.StringUtil;
import licef.XMLUtil;

class Classification {

    public Classification( File file ) {
        this.file = file;
        
        try {
            URL url = file.toURI().toURL();
            this.url = url.toString();
        }
        catch( MalformedURLException e ) {
            e.printStackTrace(); // Should never happen. - FB
        }
    }

    public Classification( String url, File file ) {
        this.url = url;
        this.file = file;
    }

    public String getUrl() {
        return( url );
    }

    public File getFile() {
        return( file );
    }

    public LangStrings getTitles() {
        if( titles == null ) {
            try {
                ClassificationTitlesInitializer initializer = new ClassificationTitlesInitializer( new BufferedInputStream( new FileInputStream( file ) ) );
                titles = initializer.getTitles();
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
        }
        return( titles );
    }

    public String getTitle( String language ) {
        String title = getTitles().getString( language );
        return( title == null ? getUrl() : title );
    }

    public Model getModel() {
        if( model == null ) {
            try {
                initModel( false );
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
        }
        return( model );
    }

    public String getConceptSchemeUri() throws IOException {
        if( conceptSchemeUri == null ) {
            String queryStr = getQuery( "getConceptSchemeUri.sparql" );
            Query query = QueryFactory.create( queryStr );
            QueryExecution exec = QueryExecutionFactory.create( query, model );
            try {
                for( ResultSet results = exec.execSelect(); results.hasNext(); ) {
                    QuerySolution res = results.nextSolution();
                    for( Iterator it = res.varNames(); it.hasNext(); ) {
                        String varName = it.next().toString();
                        RDFNode n = res.get( varName );
                        conceptSchemeUri = n.toString();
                    }
                }
            }
            finally {
                exec.close() ;
            }
        }
        return( conceptSchemeUri );
    }

    public String toString() {
        return( getTitle( Preferences. getInstance().getLocale().getLanguage() ) );
    }

    public int hashCode() {
        return( url.hashCode() );
    }

    public boolean equals( Object obj ) {
        if( obj == this )
            return( true );
        if( !( obj instanceof Classification ) )
            return( false );
        Classification classif = (Classification)obj;
        return( url.equals( classif.getUrl() ) );
    }

    public static Collection<Classification> getAll() {
        if( classifs == null ) {
            classifs = new HashMap<String,Classification>();
            try {
                init();
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
        }
        return( classifs.values() );
    }

    public static Classification get( String strUrl ) {
        return( classifs.get( strUrl ) );
    }

    public void register() {
        classifs.put( getUrl(), this );
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

    public static void init() {
        classifs.clear();
        String classifFolder = Util.getClassificationFolder();
        if( classifFolder != null ) {
            File[] classifFiles = new File( classifFolder ).listFiles();
            for( int i = 0; i < classifFiles.length; i++ ) {
                try {
                    Classification classif = new Classification( classifFiles[ i ] );
                    classifs.put( classif.getUrl(), classif );
                }
                catch( Exception e ) {
                    // Skip the classification if a problem occurs.
                    e.printStackTrace();
                }
            }
        }
        language = Preferences.getInstance().getLocale().getLanguage();
        isShowTaxumId = Boolean.valueOf( Preferences.getInstance().isShowTaxumId() );
    }

    private void initModel( boolean isInferenceNeeded ) throws IOException, MalformedURLException {
        URL tmpUrl = new URL( this.url );
        InputStream is = (InputStream) tmpUrl.getContent();
        Model model = null;

        try {
            model = ModelFactory.createDefaultModel();
            model.read( is, null );
        }
        finally {
            try {
                is.close();
            }
            catch( java.io.IOException e ) {
                e.printStackTrace();
            }
        }

        this.model = model;

        if( isInferenceNeeded )
            computeInference();
    }

    public boolean delete() {
        try {
            File file = new File( new URI( getUrl().toString() ) );
            boolean isDeleted = file.delete();
            if( isDeleted ) 
                Classification.classifs.remove( getUrl() );
            return( isDeleted );
        }
        catch( URISyntaxException e ) {
            e.printStackTrace();
        }
        return( false );
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

            try {
                File skosInputFile = chooser.getSelectedFile();
                File tmpFile = null;

                String xml = IOUtil.readStringFromFile( chooser.getSelectedFile() );
                String rootTagName = XMLUtil.getRootTagName( xml );
                if (rootTagName != null) {
                    String[] array = StringUtil.split(rootTagName, ':');
                    rootTagName = array[array.length - 1].toLowerCase();
                }
                boolean isVdexFile = ( rootTagName != null && "vdex".equals( rootTagName ) );
                if( isVdexFile ) {
                    String xsltFile = "/xslt/convertVDEXToSKOS.xsl";
                    StreamSource xslt = new StreamSource( Util.class.getResourceAsStream( xsltFile ) );

                    StreamSource xmlSource = new StreamSource( new BufferedReader( new StringReader( xml ) ) );

                    Node skosNode = XMLUtil.applyXslToDocument2( xslt, xmlSource, null, null, null );
                    tmpFile = File.createTempFile( "lompad", "inputSkos" );
                    Writer tmpFileWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( tmpFile, false ), "UTF-8" ) );

                    try {
                        XMLUtil.serialize( skosNode, false, tmpFileWriter );
                    }
                    finally {
                        if( tmpFileWriter != null )
                            tmpFileWriter.close();
                    }
                    skosInputFile = tmpFile;
                }

                Classification classif = new Classification( skosInputFile );
                classif.initModel( true );
                String uri = classif.getConceptSchemeUri();
                if( uri == null )
                    throw new Exception( "ConceptScheme's URI not found."  );
                String sha1 = DigestUtils.shaHex( uri );
                File localFile = new File( Util.getClassificationFolder(), sha1 + ".rdf" );
                classif.dumpModelToRdf( localFile );

                if( tmpFile != null ) 
                    tmpFile.delete();

                return( localFile );
            }
            catch( Exception e ) {
                e.printStackTrace();
                String msg = "classifImportFailed";
                if( "Classification identifier not found.".equals( e.getMessage() ) )
                    msg = "classifIdentifierNotFound";
                JDialogAlert dialog = new JDialogAlert( Util.getTopJFrame( (Container)parent ), "titleErr", msg );
                dialog.setSize( 320, 140 ); 
                dialog.setVisible( true );
                return( null );
            }
        }
        else
            return( null );
    }

    public static boolean isVdexFile( File file ) throws /*IOException*/Exception {
        String xml = IOUtil.readStringFromFile( file );
        String rootTagName = XMLUtil.getRootTagName( xml );
        return( rootTagName != null && "vdex".equals( rootTagName ) );
    }

    /*
     * @param queryId Name of the query that corresponds to a file name in the query resource directory.
     * @param params Parameters which their value will be substituted in the query.  Beware, numeric values should be 
     * converted to string before the call to prevent locale-dependant formatting.
     */
    private String getQuery( String queryId, Object... params ) throws java.io.IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream( baos );
        InputStream is = getClass().getResourceAsStream( "/queries/" + queryId );

        BufferedInputStream bis = new BufferedInputStream( is );
        try {
            IOUtil.copy( bis, bos );
        }
        finally {
            bis.close();
            bos.close();
        }
        String rawQuery = baos.toString( "UTF-8" );
        if( params == null || params.length == 0 )
            return( rawQuery );

        String query = MessageFormat.format( rawQuery, params );
        return( query );
    }

    private void computeInference() {
        Model modelSkos = ModelFactory.createDefaultModel();
        InputStream is = getClass().getResourceAsStream("/skos.rdf");
        model.read( is, "" );
        
        Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
        reasoner = reasoner.bindSchema( modelSkos );
        InfModel infModel = ModelFactory.createInfModel( reasoner, model );
        model.add( infModel.listStatements() );
    }

    private void dumpModelToRdf( File file ) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file, false ), "UTF-8" ) );
            model.write( writer, "RDF/XML" ); 
        }
        finally {
            if( writer != null )
                writer.close();
        }
    }
    
    private String url;
    private File file;
    private LangStrings titles;
    private Model model;
    private String conceptSchemeUri;

    private static String language = null;
    private static Boolean isShowTaxumId = null;

    private static Map<String,Classification> classifs = null;

}

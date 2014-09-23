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
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
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
import licef.IOUtil;
import licef.StringUtil;

class Classification {
    
    public Classification( String url, Model model ) {
        this.url = url;
        this.model = model;
    }

    public String getUrl() {
        return( url );
    }

    public String getTitle( String language ) {
        if( titles.isEmpty() ) {
            try {
                initializeTitles();
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
        }
        String title = titles.get( language );

        // If no title is provided for the asked language,
        // try to find a good substitute.
        // Either the only title if there is only one provided.
        // If more than one title is available, look for one that
        // has a language code that has the same prefix as the one
        // passed in parameter.  In other words, if we call getTitle( "fr" )
        // and that we have a title that has a language code like "fr-CA", 
        // we will take it.  If this does not work,
        // take the English one or the French one.  
        // If all fails, use the url.
        if( title == null ) {
            if( titles.size() == 1 ) {
                Iterator<String> it = titles.values().iterator();
                title = it.next();
            }
            else {
                for( Iterator<String> it = titles.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    int indexOfDash = key.indexOf( "-" );
                    if( indexOfDash != -1 ) {
                        String lang = key.substring( 0, indexOfDash );
                        if( language.equals( lang ) ) 
                            return( titles.get( key ) );
                    }
                }

                title = titles.get( "en" );
                if( title == null )
                    title = titles.get( "fr" );
                if( title == null )
                    return( getUrl() );
            }
        }
        return( title );
    }

    public Model getModel() {
        return( model );
    }

    public String toString() {
        return( getTitle( Preferences. getInstance().getLocale().getLanguage() ) );
        //return( getUrl() );
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
        return( classifs.values() );
    }

    public static Classification get( String strUrl ) {
        return( classifs.get( strUrl ) );
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

    public static void loadAll() throws IOException, MalformedURLException {
        Classification.loadAll( false );
    }

    public static void loadAll( boolean isForced ) throws IOException, MalformedURLException {
        if( isForced || isDirty() ) {
            classifs.clear();
            String classifFolder = Util.getClassificationFolder();
            if( classifFolder != null ) {
                File[] classifFiles = new File( classifFolder ).listFiles();
                for( int i = 0; i < classifFiles.length; i++ ) {
                    try {
                        Classification classif = Classification.load( classifFiles[ i ] );
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
    }

    public static Classification read( File file ) throws IOException, MalformedURLException {
        return( Classification.read( file, false ) );
    }

    public static Classification read( File file, boolean isInferenceNeeded ) throws IOException, MalformedURLException {
        Classification classif = null;

        URL url = file.toURI().toURL();
        InputStream is = (InputStream) url.getContent();
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

        classif = new Classification( url.toString(), model );

        if( isInferenceNeeded )
            classif.computeInference();
        
        return( classif );
    }

    public static Classification load( File file ) throws IOException, MalformedURLException {
        return( Classification.load( file, false ) );
    }

    public static Classification load( File file, boolean isInferenceNeeded ) throws IOException, MalformedURLException {
        Classification classif = read( file, isInferenceNeeded );
        classifs.put( classif.getUrl(), classif );
        return( classif );
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
                Classification classif = Classification.read( chooser.getSelectedFile(), true );
                String uri = classif.getConceptSchemeUri();
                String sha1 = DigestUtils.shaHex( uri );
                File localFile = new File( Util.getClassificationFolder(), sha1 + ".rdf" );
                classif.dumpModelToRdf( localFile );
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

    private String getConceptSchemeUri() throws IOException {
        String queryStr = getQuery( "getConceptSchemeUri.sparql" );
        Query query = QueryFactory.create( queryStr );
        QueryExecution exec = QueryExecutionFactory.create( query, model );
        try {
            for( ResultSet results = exec.execSelect(); results.hasNext(); ) {
                QuerySolution res = results.nextSolution();
                for( Iterator it = res.varNames(); it.hasNext(); ) {
                    String varName = it.next().toString();
                    RDFNode n = res.get( varName );
                    return( n.toString() );
                }
            }
        }
        finally {
            exec.close() ;
        }
        return( null );
    }

    private void initializeTitles() throws IOException {
        String queryStr = getQuery( "getConceptSchemeLabels.sparql" );
        Query query = QueryFactory.create( queryStr );
        QueryExecution exec = QueryExecutionFactory.create( query, model );
        try {
            for( ResultSet results = exec.execSelect(); results.hasNext(); ) {
                QuerySolution res = results.nextSolution();
                for( Iterator it = res.varNames(); it.hasNext(); ) {
                    String varName = it.next().toString();
                    RDFNode n = res.get( varName );
                    if( n instanceof Literal ) {
                        Literal literal = (Literal)n;
                        //String lang = Util.getLangFromLocaleString( literal.getLanguage() );
                        String lang = literal.getLanguage();
                        String label = literal.getValue().toString().trim();
                        titles.put( lang, label );
                    }
                }
            }
        }
        finally {
            exec.close() ;
        }
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
    
    private static boolean isDirty() {
        return( 
            ( isShowTaxumId == null || isShowTaxumId.booleanValue() != Preferences.getInstance().isShowTaxumId() ) ||
            ( language == null || language != Preferences.getInstance().getLocale().getLanguage() ) 
        );
    }

    private String url;
    private Map<String,String> titles = new HashMap<String,String>();
    private Model model;

    private static String language = null;
    private static Boolean isShowTaxumId = null;

    private static Map<String,Classification> classifs = new HashMap<String,Classification>();

}



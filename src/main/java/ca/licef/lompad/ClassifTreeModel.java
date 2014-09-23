package ca.licef.lompad;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;

import licef.IOUtil;

public class ClassifTreeModel extends DefaultTreeModel {

    public ClassifTreeModel( TreeNode root, Model model ) {
        super( root );
        this.model = model;

        try {
            buildNodes();
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public ClassifTreeModel( TreeNode root, URL rdfData ) {
        this( root, rdfData, false );
    }

    public ClassifTreeModel( TreeNode root, URL rdfData, boolean isInferenceNeeded ) {
        super( root );
        
        try {
            InputStream is = (InputStream)rdfData.getContent();
            init( is, isInferenceNeeded );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public ClassifTreeModel( TreeNode root, File rdfData ) {
        this( root, rdfData, false );
    }

    public ClassifTreeModel( TreeNode root, File rdfData, boolean isInferenceNeeded ) {
        super( root );

        try {
            InputStream is = new FileInputStream( rdfData );
            init( is, isInferenceNeeded );
        }
        catch( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }

    private void init( InputStream is, boolean isInferenceNeeded ) {
        try {
            model = ModelFactory.createDefaultModel();
            model.read( is, null );
        }
        finally {
            try {
                is.close();
            }
            catch( IOException e ) {
                e.printStackTrace();
            }
        }

        if( isInferenceNeeded )
            doInference();

        try {
            buildNodes();
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public void addChildrenNodesIfNeeded( DefaultMutableTreeNode node ) throws IOException {
        for( int i = 0; i < node.getChildCount(); i++ ) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)node.getChildAt( i );
            if( childNode.getChildCount() == 0 ) {
                String childUri = ((LocalizeTaxon)childNode.getUserObject()).uri;
                String[] childrenConceptUris = getChildrenConcepts( childUri );
                buildChildrenNodes( childNode, childrenConceptUris ); 
            }
        }
    }

    private void doInference() {
        Model modelSkos = ModelFactory.createDefaultModel();
        InputStream is = getClass().getResourceAsStream("/skos.rdf");
        model.read( is, "" );
        
        Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
        reasoner = reasoner.bindSchema( modelSkos );
        InfModel infModel = ModelFactory.createInfModel( reasoner, model );
        model.add( infModel.listStatements() );
    }

    /*
     * @param queryId Name of the query that corresponds to a file name in the query resource directory.
     * @param params Parameters which their value will be substituted in the query.  Beware, numeric values should be 
     * converted to string before the call to prevent locale-dependant formatting.
     */
    private String getQuery( String queryId, Object... params ) throws IOException {
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

    private String[] getChildrenConcepts( String parentUri ) throws IOException {
        List<String> childrenConceptUris = new ArrayList<String>();
        String queryStr = getQuery( "getChildrenConcepts.sparql", parentUri );
        Query query = QueryFactory.create( queryStr, parentUri );
        QueryExecution exec = QueryExecutionFactory.create( query, model );
        try {
            for( ResultSet results = exec.execSelect(); results.hasNext(); ) {
                QuerySolution res = results.nextSolution();
                for( Iterator it = res.varNames(); it.hasNext(); ) {
                    String varName = it.next().toString();
                    RDFNode n = res.get( varName );
                    childrenConceptUris.add( n.toString() );
                }
            }
        }
        finally {
            exec.close() ;
        }
        return( childrenConceptUris.toArray( new String[ childrenConceptUris.size() ] ) ); 
    }

    private String[] getTopConcepts() throws IOException {
        List<String> topConceptUris = new ArrayList<String>();
        String queryStr = getQuery( "getTopConcepts.sparql" );
        Query query = QueryFactory.create( queryStr );
        QueryExecution exec = QueryExecutionFactory.create( query, model );
        try {
            for( ResultSet results = exec.execSelect(); results.hasNext(); ) {
                QuerySolution res = results.nextSolution();
                for( Iterator it = res.varNames(); it.hasNext(); ) {
                    String varName = it.next().toString();
                    RDFNode n = res.get( varName );
                    topConceptUris.add( n.toString() );
                }
            }
        }
        finally {
            exec.close() ;
        }
        return( topConceptUris.toArray( new String[ topConceptUris.size() ] ) ); 
    }

    private void buildChildrenNodes( DefaultMutableTreeNode parentNode, String[] childrenUris ) throws IOException {
        for( int i = 0; i < childrenUris.length; i++ ) {
            String childUri = childrenUris[ i ];

            Map<String,String> titles = new HashMap<String,String>();

            String queryStr = getQuery( "getPrefLabels.sparql", childUri );
            Query query = QueryFactory.create( queryStr, childUri );
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

            String taxonPathId = Classification.retrieveTaxonPathId( childUri );
            LocalizeTaxon taxon = new LocalizeTaxon(taxonPathId, childUri, titles);
            DefaultMutableTreeNode newChild = new DefaultMutableTreeNode( taxon );
            parentNode.add( newChild );
        }
    }

    private void buildNodes() throws IOException {
        String[] topConceptUris = getTopConcepts(); 
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getRoot();
        buildChildrenNodes( rootNode, topConceptUris );
        for( int i = 0; i < rootNode.getChildCount(); i++ ) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)rootNode.getChildAt( i );
            String childUri = ((LocalizeTaxon)childNode.getUserObject()).uri;
            String[] childrenConceptUris = getChildrenConcepts( childUri );
            buildChildrenNodes( childNode, childrenConceptUris ); 
        }
    }

    private Model model;

}

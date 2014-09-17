package ca.licef.lompad;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ClassifTree extends JTree {

    public ClassifTree() {
        super();
        setRootVisible( false );
        getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon( null );
        renderer.setClosedIcon( null );
        renderer.setOpenIcon( null );
        setCellRenderer( renderer );

        setShowsRootHandles( true );

        TreeWillExpandListener expandListener = new TreeWillExpandListener() {

            public void treeWillCollapse( TreeExpansionEvent evt ) {
            }

            public void treeWillExpand( TreeExpansionEvent evt ) {
                TreePath selectedPath = evt.getPath();
                treeNodeExpanded( selectedPath );
            }

        };
        addTreeWillExpandListener( expandListener );
    }

    public Insets getInsets() {
        return new Insets(2, 2, 0, 0);
    }

    public void treeNodeExpanded( TreePath selectedPath ) {
        ClassifTreeModel model = (ClassifTreeModel)getModel();
        try {
            model.addChildrenNodesIfNeeded( (DefaultMutableTreeNode)selectedPath.getLastPathComponent() );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws Exception {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( "Hidden Root Node" );
        //File rdfLocation = new File( "/home/fred/.lompad/classif/2e63c57d4368d3e6a907c2e89bfd0792e5d1c019.rdf" );
        URL rdfLocation = new URL( "file:///home/fred/.lompad/classif/2e63c57d4368d3e6a907c2e89bfd0792e5d1c019.rdf" );
        ClassifTreeModel classifTreeModel = new ClassifTreeModel( rootNode, rdfLocation );
        ClassifTree classifTree = new ClassifTree();
        classifTree.setModel( classifTreeModel );
        
        JScrollPane scrollPane = new JScrollPane( classifTree );
        JFrame frame = new JFrame( "Test" );
        frame.setVisible( true );
        frame.setTitle( "Test" );
        frame.setSize( 800, 600 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.getContentPane().add( scrollPane );
    }

}


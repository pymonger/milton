package bradswebdavclient;

import com.ettrema.httpclient.ConnectionListener;
import com.ettrema.httpclient.Host;
import java.awt.Cursor;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author mcevoyb
 */
public class HostNode extends FolderNode {

    final Host host;

    public HostNode( AbstractTreeNode parent, Host host ) throws Exception {
        super( parent, host.getFolder( "" ) );
        this.host = host;
        host.connectionListeners.add( new ConnectionListener() {

            public void onStartRequest() {
                Cursor hourglassCursor = new Cursor( Cursor.WAIT_CURSOR );
                App.current().getFrame().getComponent().setCursor( hourglassCursor );
            }

            public void onFinishRequest() {
                Cursor normalCursor = new Cursor( Cursor.DEFAULT_CURSOR );
                App.current().getFrame().getComponent().setCursor( normalCursor );
            }
        } );
    }

    @Override
    protected String getIconName() {
        return "home.png";
    }

    void select( String path ) {
        System.out.println( "select: " + path );
        if( path.startsWith( "/" ) ) {
            path = path.substring( 1 );
        }
        if( path.endsWith( "/" ) ) {
            path = path.substring( 0, path.length() - 1 );
        }
        path = path.trim();
        if( path.length() == 0 ) {
            select();
        } else {
            System.out.println( "path=" + path );
            String[] arr = path.split( "[/]" );
            for( String s : arr ) {
                System.out.println( " arr=" + s );
            }
            System.out.println( "  selecting: " + arr.length );
            select( arr, 0 );
        }
    }

    @Override
    void updatePopupMenu( JPopupMenu popupMenu ) {
        super.updatePopupMenu( popupMenu );

        JMenuItem item = new JMenuItem( "Remove Host" );
        item.addMouseListener( new AbstractMouseListener( this ) {

            @Override
            public void onClick() {
                parent.remove( HostNode.this );
            }
        } );
        popupMenu.add( item );
    }

    @Override
    public String toString() {
        return host.server;
    }
}



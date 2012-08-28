package com.ettrema.json;

import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.FileItem;
import com.bradmcevoy.http.MakeCollectionableResource;
import com.bradmcevoy.http.PostableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.ettrema.event.EventManager;
import com.ettrema.event.NewFolderEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forwards the POST request to the createCollection method on the wrapped
 * resource, passin it the "name" request parameter
 *
 * @author brad
 */
public class MkcolJsonResource extends JsonResource implements PostableResource {

    private static final Logger log = LoggerFactory.getLogger( MkcolJsonResource.class );
    private final MakeCollectionableResource wrapped;
    private final String href;
    private final EventManager eventManager;

    public MkcolJsonResource( MakeCollectionableResource makeCollectionableResource, String href, EventManager eventManager ) {
        super( makeCollectionableResource, Request.Method.PUT.code, null );
        this.eventManager = eventManager;
        this.wrapped = makeCollectionableResource;
        this.href = href;
    }

    public String processForm( Map<String, String> parameters, Map<String, FileItem> files ) throws BadRequestException, NotAuthorizedException {
        try {
            CollectionResource col = wrapped.createCollection( parameters.get( "name" ) );
            if( eventManager != null ) {
                eventManager.fireEvent( new NewFolderEvent( col ) );
            }
            return null;
        } catch( ConflictException ex ) {
            throw new BadRequestException( wrapped, "A conflict occured. The folder might already exist" );
        }
    }

    public void sendContent( OutputStream out, Range range, Map<String, String> params, String contentType ) throws IOException, NotAuthorizedException, BadRequestException {
        // nothing to do
    }

    @Override
    public Method applicableMethod() {
        return Method.MKCOL;
    }
}

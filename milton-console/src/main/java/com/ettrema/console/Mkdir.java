package com.ettrema.console;

import com.bradmcevoy.common.Path;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.MakeCollectionableResource;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.ettrema.event.NewFolderEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mkdir extends AbstractConsoleCommand {

    private static final Logger log = LoggerFactory.getLogger( ConsoleResourceFactory.class );

    public Mkdir( List<String> args, String host, String currentDir, ConsoleResourceFactory resourceFactory ) {
        super( args, host, currentDir, resourceFactory );
    }

    @Override
    public Result execute() {
        try {
            String newName = args.get( 0 );
            log.debug( "mkdir. execute: " + newName );
            if( !cursor.isFolder() ) {
                return result( "current dir not found: " + cursor.getPath() );
            } else {
                CollectionResource cur = (CollectionResource) cursor.getResource();
                Result validationResult = validate( cur, newName );
                if( validationResult != null ) {
                    return validationResult;
                }
                if( cur instanceof MakeCollectionableResource ) {
                    MakeCollectionableResource mcr = (MakeCollectionableResource) cur;
                    CollectionResource newCol;
                    try {
                        newCol = doCreate( mcr, newName );
                    } catch( BadRequestException ex) {
                        log.debug( "ex", ex );
                        return result( "BadRequestException exception: " + ex.getMessage() );
                    } catch( ConflictException ex ) {
                        log.debug( "ex", ex );
                        return result( "conflict exception: " + ex.getMessage() );
                    } catch( NotAuthorizedException ex ) {
                        log.debug( "ex", ex );
                        return result( "you are not authorized to create a folder here: " + ex.getMessage() );
                    }
                    Path newPath = cursor.getPath().child( newName );
                    return new Result( cursor.getPath().toString(), "created: <a href='" + newPath + "'>" + newCol.getName() + "</a>" );
                } else {
                    return result( "current dir does not support creating child collections: " + cursor.getPath() );
                }
            }
        } catch (NotAuthorizedException ex) {
            log.error("not authorised", ex);
            return result(ex.getLocalizedMessage());
        } catch (BadRequestException ex) {
            log.error("bad req", ex);
            return result(ex.getLocalizedMessage());
        }
    }

    protected CollectionResource doCreate(MakeCollectionableResource parent, String newName ) throws ConflictException, NotAuthorizedException, BadRequestException {
        CollectionResource col = parent.createCollection( newName );
        if( eventManager != null ) {
            eventManager.fireEvent( new NewFolderEvent( col));
        }
        return col;
    }

    protected Result validate( CollectionResource cur, String newName ) throws NotAuthorizedException, BadRequestException {
        Resource existing = cur.child( newName );
        if( existing != null ) {
            return result( "An item of that name already exists. Is a: " + existing.getClass() );
        } else {
            return null;
        }
    }
}


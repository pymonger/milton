package com.ettrema.json;

import com.bradmcevoy.http.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brad
 */
public class TypeResourceMatcher implements ResourceMatcher{

    private Logger log = LoggerFactory.getLogger( TypeResourceMatcher.class );

    private final Class matchClass;

    public TypeResourceMatcher( Class matchClass ) {
        this.matchClass = matchClass;
    }
    

    public boolean matches( Resource r ) {
        boolean b = matchClass.isAssignableFrom( r.getClass() );
        log.debug( "matches: " + r.getClass() + " - " + b);
        return b;
    }

}

package com.ettrema.json;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.DigestResource;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.http11.auth.DigestResponse;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class to contain common properties
 *
 * @author brad
 */
public abstract class JsonResource implements DigestResource {

    private static final Logger log = LoggerFactory.getLogger( JsonResource.class );

    private final Resource wrappedResource;
    private final String name;
    private final Long maxAgeSecs;

    public abstract Method applicableMethod();

    public JsonResource( Resource wrappedResource, String name, Long maxAgeSecs ) {
        this.wrappedResource = wrappedResource;
        this.name = name;
        this.maxAgeSecs = maxAgeSecs;
    }



    public Long getMaxAgeSeconds( Auth auth ) {
        return maxAgeSecs;
    }

    public String getContentType( String accepts ) {
        String s = "application/x-javascript; charset=utf-8";
        return s;
        //return "application/json";
    }

    public Long getContentLength() {
        return null;
    }

	@Override
    public String getUniqueId() {
        return null;
    }

	@Override
    public String getName() {
        return name;
    }

	@Override
    public Object authenticate( String user, String password ) {
        if( log.isDebugEnabled()) {
            log.debug( "authenticate: " + user);
        }
        Object o = wrappedResource.authenticate( user, password );
        if( log.isDebugEnabled()) {
            if( o == null ) {
                log.debug( "authentication failed on wrapped resource of type: " + wrappedResource.getClass());
            }
        }
        return o;
    }

	@Override
    public Object authenticate( DigestResponse digestRequest ) {
        if( wrappedResource instanceof DigestResource) {
            return ((DigestResource)wrappedResource).authenticate( digestRequest );
        } else {
            return null;
        }
    }

	@Override
    public boolean isDigestAllowed() {
        return wrappedResource instanceof DigestResource;
    }





	@Override
    public boolean authorise( Request request, Method method, Auth auth ) {
        boolean b = wrappedResource.authorise( request, applicableMethod(), auth );
        if( log.isDebugEnabled()) {
            if( !b ) {
                log.trace( "authorise failed on wrapped resource of type: " + wrappedResource.getClass());
            } else {
                log.trace("all ok");
            }
        }
        return b;
    }

	@Override
    public String getRealm() {
        return wrappedResource.getRealm();
    }

	@Override
    public Date getModifiedDate() {
        return null;
    }

	@Override
    public String checkRedirect( Request request ) {
        return null;
    }

	public Resource getWrappedResource() {
		return wrappedResource;
	}
	
	
}

package com.ettrema.examples.db.resources;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.SecurityManager;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author brad
 */
public class AllMakes implements CollectionResource, PropFindableResource, GetableResource{

    private final DemoDbResourceFactory resourceFactory;

    private final com.bradmcevoy.http.SecurityManager securityManager;

    public AllMakes( DemoDbResourceFactory resourceFactory, SecurityManager securityManager ) {
        this.resourceFactory = resourceFactory;
        this.securityManager = securityManager;
    }

    public Resource child( String childName ) {
        for( Resource r : getChildren() ) {
            if(r.getName().equals( childName)) {
                return r;
            }
        }
        return null;
    }

    
    public List<? extends Resource> getChildren() {
        return resourceFactory.getAllMakes();
    }

    public String getUniqueId() {
        return null;
    }

    public String getName() {
        return "";
    }

    public Object authenticate( String user, String password ) {
        return securityManager.authenticate( user, password );
    }

    public boolean authorise( Request request, Method method, Auth auth ) {
        return securityManager.authorise( request, method, auth, this );
    }

    public String getRealm() {
        return securityManager.getRealm( null );
    }

    public Date getModifiedDate() {
        return null;
    }

    public String checkRedirect( Request request ) {
        return null;
    }

    public Date getCreateDate() {
        return null;
    }

    public void sendContent( OutputStream out, Range range, Map<String, String> params, String contentType ) throws IOException, NotAuthorizedException, BadRequestException {
        PrintWriter pw = new PrintWriter( out );
        pw.print( "<html>" );
        pw.print( "<body>" );
        pw.print( "<h1>All Vehicles</h1>" );
        pw.print( "<ul>");
        for( Resource r : resourceFactory.getAllMakes()) {
            pw.print( "<li>");
            pw.print( "<a href='" + r.getName() + "/'>" + r.getName() + "</a>");
            pw.print( "</li>");
        }
        pw.print( "</ul>");
        pw.print( "</body>" );
        pw.print( "</html>" );
        pw.flush();
        pw.close();
    }

    public Long getMaxAgeSeconds( Auth auth ) {
        return null;
    }

    public String getContentType( String accepts ) {
        return "text/html";
    }

    public Long getContentLength() {
        return null;
    }

}

package com.ettrema.console;

import com.bradmcevoy.http.DigestResource;
import com.bradmcevoy.http.ResourceFactory;
import com.bradmcevoy.http.http11.auth.DigestResponse;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author brad
 */
public class DigestConsole extends Console implements DigestResource{

    final DigestResource digestResource;

    public DigestConsole(String host, final ResourceFactory wrappedFactory, String name, DigestResource secureResource, Date modDate, Map<String,ConsoleCommandFactory> mapOfFactories) {
        super(host, wrappedFactory, name, secureResource, modDate, mapOfFactories );
        this.digestResource = secureResource;
    }

    public Object authenticate( DigestResponse digestRequest ) {
        return digestResource.authenticate( digestRequest );
    }

    public boolean isDigestAllowed() {
        return digestResource.isDigestAllowed();
    }



}

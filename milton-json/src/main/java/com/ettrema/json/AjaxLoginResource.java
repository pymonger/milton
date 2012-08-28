package com.ettrema.json;

import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author brad
 */
public class AjaxLoginResource extends JsonResource implements GetableResource{

    private final String name;

    private final GetableResource wrapped;

    public AjaxLoginResource( String name, GetableResource wrapped ) {
        super(wrapped, name, null );
        this.name = name;
        this.wrapped = wrapped;
    }

    @Override
    public void sendContent( OutputStream out, Range range, Map<String, String> params, String contentType ) throws IOException, NotAuthorizedException, BadRequestException {
        // nothing to send
    }

    @Override
    public Method applicableMethod() {
        return Method.GET;
    }

}

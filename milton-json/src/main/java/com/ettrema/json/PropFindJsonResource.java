package com.ettrema.json;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropFindJsonResource extends JsonResource implements GetableResource {

    private static final Logger log = LoggerFactory.getLogger( PropFindJsonResource.class );

    private final PropFindableResource wrappedResource;
    private final JsonPropFindHandler jsonPropFindHandler;
    private final String encodedUrl;

    public PropFindJsonResource(PropFindableResource wrappedResource, JsonPropFindHandler jsonPropFindHandler, String encodedUrl, Long maxAgeSecs) {
        super(wrappedResource, Request.Method.PROPFIND.code, maxAgeSecs);
        this.wrappedResource = wrappedResource;
        this.encodedUrl = encodedUrl;
        this.jsonPropFindHandler = jsonPropFindHandler;
    }

	@Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException {
        //jsonPropFindHandler.sendContent( wrappedResource, encodedUrl, out, range, params, contentType );
        jsonPropFindHandler.sendContent(wrappedResource, encodedUrl, out, range, params, contentType);
    }

    @Override
    public Method applicableMethod() {
        return Method.PROPFIND;
    }

    /**
     * Overridden to allow clients to specifiy the max age as a request parameter
     *
     * This is to allow efficient browser caching of results in cases that need it,
     * while also permitting non-cached access
     *
     * @param auth
     * @return
     */
    @Override
    public Long getMaxAgeSeconds(Auth auth) {
        Request req = HttpManager.request();
        if (req != null) {
            String sMaxAge = req.getParams().get("maxAgeSecs");
            if (sMaxAge != null && sMaxAge.length() > 0) {
                try {
                    log.trace("using max age from parameter");
                    Long maxAge = Long.parseLong(sMaxAge);
                    return maxAge;
                } catch (NumberFormatException e) {
                    log.debug("Couldnt parse max age parameter: " + sMaxAge);
                }
            }
        }
        return super.getMaxAgeSeconds(auth);
    }

	
}

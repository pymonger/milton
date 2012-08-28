package com.ettrema.json;

import com.bradmcevoy.http.FileItem;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.PostableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.exceptions.NotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * This just allows the determination of the per method handling class to be
 * figured out during POST or GET processing, rather then requiring that
 * determination to be made solely on the basis of the url
 *
 * @author brad
 */
public class PostJsonResource extends JsonResource implements PostableResource {
	private final JsonResourceFactory jsonResourceFactory;
	private final String methodParamName;
	private final String host;
	private final String href;

	private Resource res; // this is the method handling instance

	
	public PostJsonResource(String host, String href, Resource wrappedResource, String methodParamName, JsonResourceFactory jsonResourceFactory) {
		super(wrappedResource, wrappedResource.getName(), null);
		this.methodParamName = methodParamName;
		this.jsonResourceFactory = jsonResourceFactory;
		this.host = host;
		this.href = href;
	}

	@Override
	public Method applicableMethod() {
		return Method.POST;
	}

	@Override
	public String processForm(Map<String, String> parameters, Map<String, FileItem> files) throws BadRequestException, NotAuthorizedException, ConflictException {
		String method = parameters.get(methodParamName);
		
		// what about allowing devs to invoke methods on their resources? do we
		// allow any method value, if it matches a method name?
		
		res = jsonResourceFactory.wrapResource(host, this, method, href);
		if( res instanceof PostableResource) {
			PostableResource pr = (PostableResource) res;
			return pr.processForm(parameters, files);
		} else {
			return null;
		}
	}

	@Override
	public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
		if( res == null) {
			String method = params.get(methodParamName);
			res = jsonResourceFactory.wrapResource(host, this, method, href);
		}
		if( res instanceof GetableResource) {
			GetableResource gr = (GetableResource) res;
			gr.sendContent(out, range, params, contentType);
		}
	}
	
}

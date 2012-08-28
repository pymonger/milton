package com.ettrema.http.caldav.demo.servlet;

import com.bradmcevoy.http.AuthenticationService;
import com.bradmcevoy.http.HandlerHelper;
import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.MiltonServlet;
import com.bradmcevoy.http.ProtocolHandlers;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Response;
import com.bradmcevoy.http.ServletRequest;
import com.bradmcevoy.http.ServletResponse;
import com.bradmcevoy.http.http11.Http11Protocol;
import com.bradmcevoy.http.webdav.DefaultWebDavResponseHandler;
import com.bradmcevoy.http.webdav.WebDavProtocol;
import com.bradmcevoy.http.webdav.WebDavResourceTypeHelper;
import com.ettrema.http.acl.ACLProtocol;
import com.ettrema.http.caldav.CalDavProtocol;
import com.ettrema.http.caldav.CalendarResourceTypeHelper;
import com.ettrema.http.caldav.demo.TResourceFactory;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an example servlet to show how to use milton's caldav without spring
 * 
 * Currently this isnt used in this project.
 * 
 * Note that this is functionally equivalent to the spring xml configuration, just
 * uses simple substitution of xml constructs (eg <constructur-arg>) with plain
 * java equivalents.
 *
 * @author brad
 */
public class CaldavMiltonServlet implements Servlet {

	private Logger log = LoggerFactory.getLogger(MiltonServlet.class);
	private ServletConfig config;
	protected HttpManager httpManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		TResourceFactory demoResourceFactory = new com.ettrema.http.caldav.demo.TResourceFactory();
		WebDavResourceTypeHelper rth = new com.bradmcevoy.http.webdav.WebDavResourceTypeHelper();
		CalendarResourceTypeHelper crth = new com.ettrema.http.caldav.CalendarResourceTypeHelper(
				new com.ettrema.http.acl.AccessControlledResourceTypeHelper(rth));
		AuthenticationService authService = new com.bradmcevoy.http.AuthenticationService();
		HandlerHelper hh = new com.bradmcevoy.http.HandlerHelper(authService);
		DefaultWebDavResponseHandler defaultResponseHandler = new com.bradmcevoy.http.webdav.DefaultWebDavResponseHandler(authService, crth);
		Http11Protocol http11 = new com.bradmcevoy.http.http11.Http11Protocol(defaultResponseHandler, hh);
		WebDavProtocol webdav = new com.bradmcevoy.http.webdav.WebDavProtocol(hh, crth, defaultResponseHandler, null);
		CalDavProtocol caldav = new com.ettrema.http.caldav.CalDavProtocol(demoResourceFactory, defaultResponseHandler, hh, webdav);
		ACLProtocol acl = new com.ettrema.http.acl.ACLProtocol(webdav);
		ProtocolHandlers protocols = new com.bradmcevoy.http.ProtocolHandlers(Arrays.asList(http11, webdav, caldav, acl));
		httpManager = new com.bradmcevoy.http.HttpManager(demoResourceFactory, defaultResponseHandler, protocols);

	}

	@Override
	public void destroy() {
	}

	@Override
	public void service(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;
		try {
			Request request = new ServletRequest(req);
			Response response = new ServletResponse(resp);
			httpManager.process(request, response);
		} finally {
			try {
				//servletResponse.getOutputStream().flush();
				servletResponse.flushBuffer();
			} catch (Exception e) {
				log.warn("exception flushing, probably no real problem", e);
			}
		}
	}

	@Override
	public String getServletInfo() {
		return "CaldavMiltonServlet";
	}

	@Override
	public ServletConfig getServletConfig() {
		return config;
	}
}

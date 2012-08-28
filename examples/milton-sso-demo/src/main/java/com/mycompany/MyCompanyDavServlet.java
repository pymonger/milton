package com.mycompany;

import com.bradmcevoy.http.AuthenticationHandler;
import com.bradmcevoy.http.AuthenticationService;
import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Response;
import com.bradmcevoy.http.ServletRequest;
import com.bradmcevoy.http.ServletResponse;
import com.bradmcevoy.http.ServletSsoSessionProvider;
import com.bradmcevoy.http.http11.auth.DigestGenerator;
import com.bradmcevoy.http.webdav.DefaultWebDavResponseHandler;
import com.bradmcevoy.http.webdav.WebDavResponseHandler;
import com.ettrema.http.fs.FileSystemResourceFactory;
import com.ettrema.http.fs.LockManager;
import com.ettrema.http.fs.SimpleLockManager;
import com.ettrema.http.fs.SimpleSecurityManager;
import com.ettrema.sso.SimpleSsoSessionProvider;
import com.ettrema.sso.SsoAuthenticationHandler;
import com.ettrema.sso.SsoResourceFactory;
import com.ettrema.sso.SsoSessionProvider;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author brad
 */
public class MyCompanyDavServlet implements Servlet {

	private ServletConfig config;
	private HttpManager httpManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		
		//SsoSessionProvider ssoSessionProvider = new ServletSsoSessionProvider();		
		SimpleSsoSessionProvider ssoSessionProvider = new SimpleSsoSessionProvider();
		ssoSessionProvider.setPrefix("XXX");
				
		LockManager lockManager = new SimpleLockManager();
		Map<String, String> users = new HashMap<String, String>();
		users.put("me", "pwd");
		SimpleSecurityManager securityManager = new SimpleSecurityManager("demo", users);
		securityManager.setDigestGenerator(new DigestGenerator());
		
		String home = System.getProperty("user.dir");
		File root = new File(home);
		
		FileSystemResourceFactory rf = new FileSystemResourceFactory(root, securityManager, home);
		rf.setLockManager(lockManager);
		rf.setContextPath("/webdav");
		rf.setSsoPrefix("XXX");
		
		
		
		SsoResourceFactory ssoResourceFactory = new SsoResourceFactory(rf, ssoSessionProvider);
		SsoAuthenticationHandler ssoAuthenticationHandler = new SsoAuthenticationHandler();
		List<AuthenticationHandler> ssoHandlers = new ArrayList<AuthenticationHandler>();
		ssoHandlers.add(ssoAuthenticationHandler);
		
		AuthenticationService authenticationService = new AuthenticationService();				
		authenticationService.setExtraHandlers(ssoHandlers);

		WebDavResponseHandler respHandler = new DefaultWebDavResponseHandler(authenticationService);

		
		httpManager = new HttpManager(ssoResourceFactory, respHandler, authenticationService);

	}

	@Override
	public ServletConfig getServletConfig() {
		return config;
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

			servletResponse.getOutputStream().flush();
			servletResponse.flushBuffer();
		}
	}

	@Override
	public String getServletInfo() {
		return "MyCompanyDavServlet";
	}

	@Override
	public void destroy() {
	}
}

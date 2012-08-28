package com.bradmcevoy.http;

import java.io.File;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Provides access to servlet resources (ie files defined within the folder
 * which contains WEB-INF) in a milton friendly resource factory
 *
 * @author bradm
 */
public class ServletResourceFactory implements ResourceFactory {

	private final ServletContext servletContext;

	@Autowired
	public ServletResourceFactory(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public Resource getResource(String host, String path) {
		String contextPath = MiltonServlet.request().getContextPath();
//		System.out.println("url: " + path);
//		System.out.println("context: " + contextPath);
		String localPath = path.substring(contextPath.length());
//		System.out.println("localpath: " + localPath);
		String realPath = servletContext.getRealPath(localPath);
//		System.out.println("realpath: " + realPath);
		if (realPath != null) {
			File file = new File(realPath);
			if (file.exists() && !file.isDirectory()) {
				return new ServletResource(file, localPath, MiltonServlet.request(), MiltonServlet.response());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}

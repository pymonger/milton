package com.mycompany;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Response;
import com.bradmcevoy.http.ServletRequest;
import com.bradmcevoy.http.ServletResponse;



/**
 * This filter demonstrates how you can easily write your own servlet filter
 * to invoke milton
 *
 * Using this approach allows you to mix non-milton resources. This example
 * shows the filter bypassing milton for JSP files, but allowing milton
 * to handle all other requests.
 *
 * You can also use StaticResourceFilter
 *
 * @author brad
 */
public class CustomFilter implements javax.servlet.Filter {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CustomFilter.class);

    private HttpManager httpManager;

    public void init( FilterConfig filterConfig ) throws ServletException {
        TResourceFactory fact = new TResourceFactory();
        httpManager = new HttpManager( fact );
    }

    public void doFilter( javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, FilterChain chain ) throws IOException, ServletException {
        log.debug( "doFilter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String url = null;
        url = ( (HttpServletRequest) servletRequest ).getRequestURL().toString();
        if( !url.endsWith( ".jsp") ) {
            log.debug( "not a JSP, use milton");
            try {
                Request request = new ServletRequest( req );
                Response response = new ServletResponse( resp );
                httpManager.process( request, response );
            } finally {
                servletResponse.getOutputStream().flush();
                servletResponse.flushBuffer();
            }
        } else {
            log.debug( "is a JSP, do not use milton");
            chain.doFilter( servletRequest, servletResponse );
        }

    }

    public void destroy() {

    }
}

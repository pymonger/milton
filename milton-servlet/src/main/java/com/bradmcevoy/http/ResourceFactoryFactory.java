package com.bradmcevoy.http;

import com.bradmcevoy.http.webdav.WebDavResponseHandler;

/**
 *  Implement this interface to configure the ResourceFactory instance
 * 
 * To use your implementation, specify its class name in: resource.factory.factory.class
 * as an init parameter on the servlet or filter in web.xml
 * 
 * Example:
 * <PRE>
 * {@code
 * <servlet>
 *   <servlet-name>milton</servlet-name>
 *   <servlet-class>com.bradmcevoy.http.MiltonServlet</servlet-class>
 *     <init-param>
 *       <param-name>resource.factory.factory.class</param-name>
 *       <param-value>com.bradmcevoy.http.SpringResourceFactoryFactory</param-value>
 *     </init-param>
 * </servlet>
 * }
 * </PRE>
 *
 * 
 */
public interface ResourceFactoryFactory {

    /**
     * Create and return a ResponseHandler. Normally this will be DefaultResponseHandler
     *
     * @return
     */
    public WebDavResponseHandler createResponseHandler();

    /**
     * Called immediately after construction
     */
    void init();
    
    /**
     * Create and return a ResourceFactory instance. This single instance
     * will usually be used for the lifetime of the servlet
     * 
     * @return 
     */
    ResourceFactory createResourceFactory();
}

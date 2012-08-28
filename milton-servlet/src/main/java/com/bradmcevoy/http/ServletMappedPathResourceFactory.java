package com.bradmcevoy.http;

import com.ettrema.common.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For any requests which match the mapping path, the request is handled by
 * simply including a servlet resource at that path
 *
 * @author brad
 */
public class ServletMappedPathResourceFactory implements ResourceFactory {

    private static final Logger log = LoggerFactory.getLogger(ServletMappedPathResourceFactory.class);
    
    private String basePath;

    @Override
    public Resource getResource(String host, String path) {
        String contextPath = MiltonServlet.request().getContextPath();
        String localPath = path.substring(contextPath.length());
        if( localPath.startsWith(basePath)) {
            LogUtils.trace(log, "getResource: matched path: ", localPath);
            return new ServletResource(localPath, MiltonServlet.request(), MiltonServlet.response());
        } else {
            LogUtils.trace(log, "getResource: did not match path: requested:", localPath, "base:", basePath);
            return null;
        }
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
    
    

}
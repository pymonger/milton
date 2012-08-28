package com.bradmcevoy.http;

/**
 *
 * @author brad
 */
public interface Initable {
    void init(ApplicationConfig config, HttpManager manager);
    void destroy(HttpManager manager);
}

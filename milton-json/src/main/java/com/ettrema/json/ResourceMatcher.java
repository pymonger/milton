package com.ettrema.json;

import com.bradmcevoy.http.Resource;

/**
 *
 * @author brad
 */
public interface ResourceMatcher {
    boolean matches(Resource r);
}

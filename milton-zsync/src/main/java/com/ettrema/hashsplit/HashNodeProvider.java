package com.ettrema.hashsplit;

import java.io.File;
import java.util.List;

/**
 * Provides a means to find hash node information for a given file.
 * 
 *
 * @author brad
 */
public interface HashNodeProvider {
    /**
     * 
     * @param file - the local file to find hash nodes 
     * @param roots - a path of "fanout" values (which may be empty indicating that we want the topmost hash nodes)
     * @return 
     */
    List<Long> getHashNodes(File file, Long[] roots);    
}

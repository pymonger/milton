package com.ettrema.hashsplit;

import java.io.File;
import java.io.InputStream;

/**
 * Generate a list of deltas - ie changed blocks
 *
 * @author brad
 */
public class HashSplitDeltaGenerator {
    
    private final HashNodeProvider hashNodeProvider;

    public HashSplitDeltaGenerator(HashNodeProvider hashNodeProvider) {
        this.hashNodeProvider = hashNodeProvider;
    }
          
    public void generateDeltas(InputStream modified, File dest) {
        
    }
}

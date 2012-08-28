package com.ettrema.json;

import junit.framework.TestCase;

/**
 *
 * @author brad
 */
public class TypeResourceMatcherTest extends TestCase {
    
    public TypeResourceMatcherTest(String testName) {
        super(testName);
    }

    public void testMatches() {
        TypeResourceMatcher trm = new TypeResourceMatcher( JsonResource.class);
        assertTrue( trm.matches( new CopyJsonResource( null, null, null)));
        assertTrue( trm.matches( new PropPatchJsonResource( null, null, null)));
    }

}

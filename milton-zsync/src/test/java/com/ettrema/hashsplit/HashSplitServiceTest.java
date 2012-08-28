package com.ettrema.hashsplit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author brad
 */
public class HashSplitServiceTest {

    HashSplitService service;
    
    //@Test
    public void testClientUploads() throws IOException {
        // Get the test data
        InputStream inOrig = this.getClass().getResourceAsStream("/hashsplit-original.txt");
        assertNotNull(inOrig);
        InputStream inMod = this.getClass().getResourceAsStream("/hashsplit-modified.txt");
        assertNotNull(inMod);
        
        // Parse the original
        List<HashNode> rootNodes = service.parse(inOrig);
        LocalHashNodeProvider hashNodeProvider = new LocalHashNodeProvider(rootNodes);
        HashSplitDeltaGenerator deltaGenerator = new HashSplitDeltaGenerator(hashNodeProvider);
        File dest = File.createTempFile("hashsplit-test", null);
        deltaGenerator.generateDeltas(inMod, dest);
        
        // so we should now have changed blocks in the "dest" file
        
    }

    @Before
    public void setUp() {
        service = new HashSplitService();
    }
    
    @After
    public void tearDown() {
    }

}

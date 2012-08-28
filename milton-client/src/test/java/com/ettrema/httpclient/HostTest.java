package com.ettrema.httpclient;

import com.bradmcevoy.common.Path;
import com.ettrema.httpclient.PropFindMethod.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author brad
 */
public class HostTest extends TestCase {
    
    public HostTest(String testName) {
        super(testName);
    }

    public void testFind() throws Exception {
    }

    public void test_find() throws Exception {
    }

    public void testGetFolder() throws Exception {
    }

    public void testOptions() throws Exception {
    }

    public void testGet() {
    }

    public void testHost() {
    }

    public void testHref() {
    }

    public void testUrlEncode() {
        String s = Host.urlEncode( "http://bb.shmego.com/a&b");
        System.out.println( "s: " + s );
        assertEquals( "http://bb.shmego.com/a%26b", s);

        s = Host.urlEncode( "http://bb.shmego.com/r%/a b");
        System.out.println( "s: " + s );
        assertEquals( "http://bb.shmego.com/r%25/a%20b", s);
		
    }

    public void testGetPropFindXml() {
    }

    public void testSetPropFindXml() {
    }

    public void testScratch() throws URISyntaxException, Exception {
//		Host h = new Host("127.0.0.1", 8080, "me", "pwd", null);
//		String url = h.getHref(Path.root);
//		List<Response> resp = h.doPropFind(url, 5);
//		System.out.println("resp: " + resp.size());
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ettrema.http.fs;

import junit.framework.TestCase;

/**
 *
 * @author brad
 */
public class FsDirectoryResourceTest extends TestCase {
	
	public FsDirectoryResourceTest(String testName) {
		super(testName);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCreateCollection() {
	}

	public void testChild() {
	}

	public void testGetChildren() {
	}

	public void testCheckRedirect() {
	}

	public void testCreateNew() throws Exception {
	}

	public void testCreateAndLock() throws Exception {
	}

	public void testSendContent() throws Exception {
	}

	public void testGetMaxAgeSeconds() {
	}

	public void testGetContentType() {
	}

	public void testGetContentLength() {
	}

	public void testInsertSsoPrefix() {
		String s = FsDirectoryResource.insertSsoPrefix("http://test.com/folder/file", "xxxyyy");
		System.out.println(s);
		assertEquals("http://test.com/xxxyyy/folder/file", s);
	}
}

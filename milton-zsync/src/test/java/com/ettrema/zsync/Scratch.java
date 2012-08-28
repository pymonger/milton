package com.ettrema.zsync;

import com.ettrema.httpclient.Host;
import com.ettrema.httpclient.ProgressListener;
import com.ettrema.httpclient.Resource;
import com.ettrema.httpclient.TransferService;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author brad
 */
public class Scratch {
	Host host;
	File fOrig;
	File fUpdated;
	File fTemp;
	String remoteName = "scratchTest.txt";
	TestProgressListener pl;
	
	@Before
	public void setUp() throws IOException {
		fOrig = new File("src/test/resources/large-text-server.txt"); // this represents the remote file we want to download
		fUpdated = new File("src/test/resources/large-text-local.txt"); // this represents the current version of the local file we want to update
		fTemp = File.createTempFile("milton-test", "orig");
                TransferService transferService = new TransferService(null, null);
                ZSyncClient zSyncClient = new ZSyncClient(transferService);
		host = new Host("localhost", "webdav", 8080, "user1", "pwd1", null, 30000, null, zSyncClient);
		pl = new TestProgressListener();
		FileUtils.copyFile(fOrig, fTemp);
	}

	@Test
	public void test_Everything() throws FileNotFoundException, Exception {
		Resource r = host.find(remoteName);
		if( r == null ) {
			System.out.println("Existing file not found");
		} else {
			System.out.println("Found existing file, deleting");
			r.delete();
		}
		System.out.println("Upload original file");
		com.ettrema.httpclient.File newFile = host.uploadFile(remoteName, fOrig, pl);
		System.out.println("Uploaded bytes: " + pl.uploadedBytes);
		System.out.println("Created: " + newFile.href());
		System.out.println("Now update file, should only be small ----------------------------");
		newFile = host.uploadFile(remoteName, fUpdated, pl );		
		assertEquals("/" + remoteName , newFile.path().toString());
		System.out.println("Uploaded bytes: " + pl.uploadedBytes);
		System.out.println("Doing incremental download");
		newFile.downloadToFile(fTemp, pl);
		System.out.println("downloaded bytes: " + pl.uploadedBytes);
		
	}

	class TestProgressListener implements ProgressListener {

		Long uploadedBytes;
		
		@Override
		public void onRead(int bytes) {
			
		}

		@Override
		public void onComplete(String fileName) {
			System.out.println("Finished");
			
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public void onProgress(long bytesRead, Long totalBytes, String fileName) {
			uploadedBytes = bytesRead;
			if( totalBytes != null && totalBytes > 0) {
				int perc = (int)(bytesRead * 100 / totalBytes);
				System.out.print(perc + "%, ");
			} else {
				System.out.print(bytesRead + ", ");
			}
		}		
	}
}

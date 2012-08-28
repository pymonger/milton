package com.ettrema.zsync;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.bradmcevoy.http.Range;

/**
 * Tests for the UploadReader class
 * 
 * @author Nick
 *
 */
public class UploadReaderTests {

	File servercopy;
	File updatedcopy;
	@Test
	public void testMoveBlocks() throws UnsupportedEncodingException {
		
		int blocksize = 5;
		String inString = "XXXXXXXXXXXXXXXXXXXXMOVEBLOCKSXXXXX";
		String outString = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
		byte[] inbytes = inString.getBytes("US-ASCII");
		byte[] outbytes = outString.getBytes("US-ASCII");
		
		List<RelocateRange> relocs = new ArrayList<RelocateRange>();
		relocs.add( new RelocateRange( new Range( 4, 6 ), 4 ) );
		
		UploadReader.moveBlocks( inbytes, relocs, blocksize, outbytes);
		String expResult = "XXXXMOVEBLOCKSXXXXXXXXXXXXXXXXXXXXXXXXXX";
		String actResult = new String( outbytes, "US-ASCII" );
		
		Assert.assertEquals( expResult, actResult );
		
	}
	
	@Test
	public void testSendBytes() throws UnsupportedEncodingException {
		
		String inString = "MOVEBLOCKSXXXXXXXXXXXXXXXXXXXXXXXXX";
		String outString = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
		byte[] inbytes = inString.getBytes("US-ASCII");
		byte[] outbytes = outString.getBytes("US-ASCII");
		
		List<Range> ranges = new ArrayList<Range>();
		ranges.add( new Range( 20, 30 ) );
		
		UploadReader.sendRanges(inbytes, ranges, outbytes);
		String expResult = "XXXXXXXXXXXXXXXXXXXXMOVEBLOCKSXXXXXXXXXX";
		String actResult = new String( outbytes, "US-ASCII" );
		
		Assert.assertEquals( expResult, actResult );
		
	}
	
	@Test
	public void testMoveBlocksFileInput() throws IOException {
		
		createTestFiles();
		
		int blocksize = 5;
		List<RelocateRange> relocs = new ArrayList<RelocateRange>();
		relocs.add( new RelocateRange( new Range( 4, 6 ), 4 ) );
		
		Enumeration<RelocateRange> relocEnum = new Upload.IteratorEnum<RelocateRange>(relocs);
		UploadReader.moveBlocks(servercopy, relocEnum, blocksize, updatedcopy);
		
		byte[] updatedbytes = FileUtils.readFileToByteArray( updatedcopy );
		
		String expResult = "XXXXMOVEBLOCKSXXXXXXXXXXXXXXXXXXXXXXXXXX";
		String actResult = new String( updatedbytes, "US-ASCII" );
		
		Assert.assertEquals( expResult, actResult );
		
	}
	
	@Test
	public void testSendRangesFileInput() throws IOException{
		
		createTestFiles();
		
		String inString = "MOVEBLOCKSXXXXXXXXXXXXXXXXXXXXXXXXX";
		InputStream dataIn = new ByteArrayInputStream(inString.getBytes( "US-ASCII" ) );
		
		List<ByteRange> ranges = new ArrayList<ByteRange>();
		ranges.add( new ByteRange( new Range( 20, 30 ), dataIn ) );
		
		Enumeration<ByteRange> dataEnum = new Upload.IteratorEnum<ByteRange>(ranges);
		UploadReader.sendRanges( dataEnum, updatedcopy );
		
		byte[] updatedbytes = FileUtils.readFileToByteArray( updatedcopy );
		
		String expResult = "XXXXXXXXXXXXXXXXXXXXMOVEBLOCKSXXXXXXXXXX";
		String actResult = new String( updatedbytes, "US-ASCII" );
		
		Assert.assertEquals( expResult, actResult );
		
		
	}
	private void createTestFiles() throws IOException {
		
		
		String inString = "XXXXXXXXXXXXXXXXXXXXMOVEBLOCKSXXXXX";
		String outString = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
		
		servercopy = File.createTempFile("servercopy", "test");
		updatedcopy = File.createTempFile("updatedcopy", "test");
		
		FileOutputStream serverOut = new FileOutputStream( servercopy );
		FileOutputStream updatedOut = new FileOutputStream( updatedcopy );
		
		serverOut.write( inString.getBytes( "US-ASCII" ) );
		updatedOut.write( outString.getBytes( "US-ASCII" ) );
		
		serverOut.close();
		updatedOut.close();
		
	}
	
}

package jazsync;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import static com.ettrema.zsync.Upload.*;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.bradmcevoy.http.Range;
import com.ettrema.zsync.ByteRange;
import com.ettrema.zsync.ByteRangeWriter;
import com.ettrema.zsync.DataRange;
import com.ettrema.zsync.RelocWriter;
import com.ettrema.zsync.RelocateRange;
import com.ettrema.zsync.Upload;
import com.ettrema.zsync.UploadMakerEx;

/**
 * Tests for the Upload class
 * 
 * @author Nick
 *
 */
public class UploadTests {

	int MAX_SEARCH;
	int NEWLINE;
	
	String version = VERSION + ": testVersion\n";
	String blocksize = BLOCKSIZE + ": 1024\n";
	String filelength = FILELENGTH + ": 32768\n";
	String someKV = "SOMEKEY: somevalue\n";
	String sha1 = SHA_1 + ": sha1checksum\n";
	
	String relocString = "Relocate: 2-6/123, 8-98/987\n";
	String rangeString = "\nRange: 45-50\nABCDE\nRange: 51-52\nZ";
	
	@Before
	public void setUp() throws UnsupportedEncodingException {
		
		MAX_SEARCH = 2000;
		NEWLINE = new String( "\n" ).getBytes( "US-ASCII" )[0];
		
	}
	
	@Test
	public void testParseParams() throws IOException {
		
		String uploadString = version + blocksize + filelength + someKV + sha1 
			+ relocString + rangeString;
		
		Upload um = Upload.parse( IOUtils.toInputStream( uploadString ) );
		
		Assert.assertEquals( 1024, um.getBlocksize() );
		Assert.assertEquals( "testVersion", um.getVersion() );
		Assert.assertEquals( 32768, um.getFilelength() );
		Assert.assertEquals( "sha1checksum" , um.getSha1() );
		
	}
	
	@Test
	public void testParseRelocatesAndByteRanges() throws UnsupportedEncodingException, IOException {
		
		String uploadString = version + blocksize + filelength + someKV + sha1 
		+ relocString  + rangeString;
	
		Upload um = Upload.parse( IOUtils.toInputStream( uploadString ) );
		
		String expRelocs = relocString;
		String actRelocs = IOUtils.toString( um.getRelocStream(), "US-ASCII" );
		actRelocs = "Relocate:" + actRelocs + "\n";
		
		String expRanges = rangeString;
		String actRanges = IOUtils.toString( um.getDataStream(), "US-ASCII" );
		actRanges = "\n" + actRanges;
		
		Assert.assertEquals( expRelocs, actRelocs );
		Assert.assertEquals( expRanges, actRanges );
	}
	
	/**
	 * Sets the fields of an Upload object and tests whether the {@link Upload#getInputStream()}
	 * returns the upload data in the expected format.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetInputStream() throws IOException {
		
		Upload um = new Upload();
		
		um.setVersion( "testVersion" );
		um.setBlocksize(1024);
		um.setFilelength(32768);
		um.setSha1("sha1checksum");
		
		File testFile = File.createTempFile( "Upload", "Test");
		RandomAccessFile randAccess = new RandomAccessFile( testFile, "rw");
		String inString = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXABCDEYZXXXXX";
		randAccess.write( inString.getBytes( "US-ASCII" ) );
		
		ByteRangeWriter dataRanges = new ByteRangeWriter( 16384);
		dataRanges.add( new Range( 45, 50 ), randAccess );
		dataRanges.add( new Range( 51, 52 ), randAccess );
		um.setDataStream( dataRanges.getInputStream() );
		
		RelocWriter relocRanges = new RelocWriter( 16384 );
		relocRanges.add( new RelocateRange( new Range( 2, 6 ), 123 ) );
		relocRanges.add( new RelocateRange( new Range( 8, 98 ), 987 ) );
		um.setRelocStream( relocRanges.getInputStream() );
		
		InputStream uploadIn = um.getInputStream();
		String actString = IOUtils.toString( uploadIn, Upload.CHARSET );
		String expString = version + filelength + blocksize + sha1 + relocString 
		+ rangeString;
		
		uploadIn.close();
		randAccess.close();
		
		System.out.println(actString);
		Assert.assertEquals( expString, actString );
	
	}
	
}

package com.ettrema.zsync;

import com.ettrema.httpclient.zsyncclient.RangeLoader;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.http11.PartialGetHelper;
import com.bradmcevoy.io.StreamUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author brad
 */
public class LocalFileRangeLoader implements RangeLoader{

	private File file;
	
	private long bytesDownloaded;

	public LocalFileRangeLoader(File file) {
		this.file = file;
	}
		
	
	@Override
	public byte[] get(List<Range> rangeList) { 
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for(Range r : rangeList) {
			writeRange(r, bout);
		}
		int expectedLength = calcExpectedLength(rangeList);
		byte[] bytes = bout.toByteArray();
		return bytes;
	}

	private void writeRange(Range r, ByteArrayOutputStream bout) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
			BufferedInputStream bufIn = new BufferedInputStream(fin);
			bytesDownloaded += (r.getFinish() - r.getStart());
			PartialGetHelper.writeRange(bufIn, r, bout);
			//StreamUtils.readTo(bufIn, bout, true, false, r.getStart(), r.getFinish());						
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		} catch(IOException e) {
			throw new RuntimeException(e);
//		} catch(ReadingException e) {
//			throw new RuntimeException(e);
//		} catch(WritingException e) {
//			throw new RuntimeException(e);
		} finally {
			StreamUtils.close(fin);			
		}
	}

	public long getBytesDownloaded() {
		return bytesDownloaded;
	}
	
	public static  int calcExpectedLength(List<Range> rangeList) {
		int l = 0;
		for( Range r : rangeList) {
			l += (r.getFinish() - r.getStart());
		}
		return l;
	}	
	
}

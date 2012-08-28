package com.ettrema.zsync;

import java.io.InputStream;

import com.bradmcevoy.http.Range;

/**
 * A simple container for a Range and a reference to an InputStream. 
 * 
 * @author Administrator
 */
public class ByteRange {

	private Range range;
	private InputStream dataQueue;
	
	/**
	 * Constructs a ByteRange with the specified Range and InputStream. The dataQueue field
	 * will simply reference the specified InputStream rather than copying from it.
	 * 
	 * @param range 
	 * @param queue 
	 */
	public ByteRange( Range range, InputStream queue ) {
		this.range = range;
		this.dataQueue = queue;
	}

	public Range getRange() {
		return range;
	}

	public InputStream getDataQueue() {
		return dataQueue;
	}
}

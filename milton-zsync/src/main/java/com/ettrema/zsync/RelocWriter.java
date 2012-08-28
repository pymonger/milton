/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ettrema.zsync;

import com.bradmcevoy.io.BufferingOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * An object used to create the relocStream portion of an Upload. <p/>
 *
 * The relocStream portion consists of a comma separated sequence of RelocateRanges,
 * terminated by the LF character.
 *
 * @author Nick
 */
/**
 *
 * @author HP
 */
public class RelocWriter {
	private BufferingOutputStream relocOut;
	private boolean first;

	public RelocWriter(int buffersize) {
		this.relocOut = new BufferingOutputStream(buffersize);
		this.first = true;
	}

	public void add(RelocateRange reloc) throws UnsupportedEncodingException, IOException {
		String relocString = reloc.getRelocation();
		if (!first) {
			relocString = ", " + relocString;
		}
		first = false;
		relocOut.write(relocString.getBytes(Upload.CHARSET));
	}

	public InputStream getInputStream() throws IOException {
		relocOut.write(Character.toString(Upload.LF).getBytes(Upload.CHARSET)[0]);
		relocOut.close();
		return relocOut.getInputStream();
	}
	
}

package com.ettrema.ldap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 *
 * @author brad
 */
class LineReaderInputStream extends PushbackInputStream {
	final String encoding;

	/**
	 * @inheritDoc
	 */
	protected LineReaderInputStream(InputStream in, String encoding) {
		super(in);
		if (encoding == null) {
			this.encoding = "ASCII";
		} else {
			this.encoding = encoding;
		}
	}

	public String readLine() throws IOException {
		ByteArrayOutputStream baos = null;
		int b;
		while ((b = read()) > -1) {
			if (b == '\r') {
				int next = read();
				if (next != '\n') {
					unread(next);
				}
				break;
			} else if (b == '\n') {
				break;
			}
			if (baos == null) {
				baos = new ByteArrayOutputStream();
			}
			baos.write(b);
		}
		if (baos != null) {
			return new String(baos.toByteArray(), encoding);
		} else {
			return null;
		}
	}

	/**
	 * Read byteSize bytes from inputStream, return content as String.
	 *
	 * @param byteSize content size
	 * @return content
	 * @throws IOException on error
	 */
	/**
	 * Read byteSize bytes from inputStream, return content as String.
	 *
	 * @param byteSize content size
	 * @return content
	 * @throws IOException on error
	 */
	public String readContentAsString(int byteSize) throws IOException {
		return new String(readContent(byteSize), encoding);
	}

	/**
	 * Read byteSize bytes from inputStream, return content as byte array.
	 *
	 * @param byteSize content size
	 * @return content
	 * @throws IOException on error
	 */
	/**
	 * Read byteSize bytes from inputStream, return content as byte array.
	 *
	 * @param byteSize content size
	 * @return content
	 * @throws IOException on error
	 */
	public byte[] readContent(int byteSize) throws IOException {
		byte[] buffer = new byte[byteSize];
		int startIndex = 0;
		int count = 0;
		while (count >= 0 && startIndex < byteSize) {
			count = in.read(buffer, startIndex, byteSize - startIndex);
			startIndex += count;
		}
		if (startIndex < byteSize) {
			throw new RuntimeException("EXCEPTION_END_OF_STREAM");
		}
		return buffer;
	}

}

package com.ettrema.zsync;

import java.util.Comparator;


/**
 * An object representing a single match between a block on the client file and a block
 * on the server file. The Pair (localOffset, remoteBlock) means that the block in the local file
 * at byte <code>localOffset</code> is identical to block number <code>remoteBlock</code> in
 * the remote file.
 * 
 * @author Nick
 */
public class OffsetPair{
	
	/**
	 * The byte offset of the block in the local file 
	 */
	public final long localOffset;
	/**
	 * The index of the block in the remote file
	 */
	public final long remoteBlock;

	
	/**
	 * Constructs an immutable OffsetPair. 
	 * 
	 * @param offset The start byte of the local block
	 * @param blockIndex The index of the remote block
	 */
	public OffsetPair(long offset, long blockIndex){
		
		localOffset = offset;
		remoteBlock = blockIndex;
	}

	/**
	 * A Comparator used to sort a list of OffsetPairs by their remoteBlock values.
	 * 
	 * @author Nick
	 *
	 */
	static class RemoteSort implements Comparator<OffsetPair>{

		@Override
		public int compare(OffsetPair o1, OffsetPair o2) {
			
			return (int) (o1.remoteBlock - o2.remoteBlock);
		}
		
	}

	/**
	 * A Comparator used to sort a list of OffsetPairs by their localOffset values.
	 * 
	 * @author Nick
	 *
	 */
	static class LocalSort implements Comparator<OffsetPair>{

		@Override
		public int compare(OffsetPair o1, OffsetPair o2) {
			
			return (int) (o1.localOffset - o2.localOffset);
		}
		
		
	}
	
}

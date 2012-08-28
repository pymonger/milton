package com.ettrema.zsync;

import com.bradmcevoy.http.Resource;
import com.ettrema.zsync.MetaFileMaker.MetaData;

/**
 * Allows optimisation of ZSync through persistence of computationally derived
 * metadata
 *
 * @author brad
 */
public interface ZSyncResource extends  Resource {


	public MetaData getZSyncMetaData();
	
}

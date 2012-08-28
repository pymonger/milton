package com.ettrema.davproxy.adapter;

import com.ettrema.httpclient.Folder;

/**
 * Just common interface between FolderResourceAdapter and MappedHostResourceAdapter
 *
 * @author brad
 */
public interface IFolderAdapter {
    /**
     * Get a reference to the remote folder (via milton-client) that this
     * adapter is adapting
     * 
     * @return 
     */
    Folder getRemoteFolder();
}

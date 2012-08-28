package com.ettrema.hashsplit;

import java.util.List;

/**
 * This provides a data structure which is a tree of long values. This
 * can be used to represent the "fanout" structure described in the Bup DESIGN
 * page
 * 
 * https://github.com/apenwarr/bup/blob/master/DESIGN
 *
 * @author brad
 */
public class HashNode {
    private long hashValue;
    private List<HashNode> childNodes;

    public List<HashNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<HashNode> childNodes) {
        this.childNodes = childNodes;
    }

    public long getHashValue() {
        return hashValue;
    }

    public void setHashValue(long hashValue) {
        this.hashValue = hashValue;
    }
    
    
    
}

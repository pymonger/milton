package com.ettrema.http.fs;


import com.bradmcevoy.http.LockInfo;
import com.bradmcevoy.http.LockResult;
import com.bradmcevoy.http.LockTimeout;
import com.bradmcevoy.http.LockToken;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import java.io.File;
import junit.framework.TestCase;

/**
 *
 * @author brad
 */
public class MemoryLockManagerTest extends TestCase {

    FsMemoryLockManager lockManager;

    @Override
    protected void setUp() throws Exception {
        lockManager = new FsMemoryLockManager();
    }

    
    public void testLockUnLock() throws NotAuthorizedException {
        LockTimeout timeout = new LockTimeout( 100l );
        LockInfo lockInfo = new LockInfo( LockInfo.LockScope.NONE, LockInfo.LockType.READ, "me", LockInfo.LockDepth.ZERO );
        FsResource resource = new FsFileResource( null, null, new File( File.pathSeparator ) );

        // lock it
        LockResult res = lockManager.lock( timeout, lockInfo, resource );
        assertNotNull( res );
        assertTrue( res.isSuccessful() );

        // check is locked
        LockToken token = lockManager.getCurrentToken( resource );
        assertNotNull( token );
        assertEquals( token.tokenId, res.getLockToken().tokenId );

        // unlock
        lockManager.unlock( token.tokenId, resource );

        // check removed
        token = lockManager.getCurrentToken( resource );
        assertNull( token );
    }

    public void testRefresh() {
    }

    public void testUnlock() {
    }

    public void testGetCurrentToken() {
    }
}

package com.ettrema.http.fs;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.CopyableResource;
import com.bradmcevoy.http.DigestResource;
import com.bradmcevoy.http.LockInfo;
import com.bradmcevoy.http.LockResult;
import com.bradmcevoy.http.LockTimeout;
import com.bradmcevoy.http.LockToken;
import com.bradmcevoy.http.LockableResource;
import com.bradmcevoy.http.MoveableResource;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.http11.auth.DigestResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class FsResource implements Resource, MoveableResource, CopyableResource, LockableResource, DigestResource {

    private static final Logger log = LoggerFactory.getLogger(FsResource.class);
    File file;
    final FileSystemResourceFactory factory;
    final String host;
    String ssoPrefix;

    protected abstract void doCopy(File dest);

    public FsResource(String host, FileSystemResourceFactory factory, File file) {
        this.host = host;
        this.file = file;
        this.factory = factory;
        
        log.info("===============================================");
        log.info("Instantiated FSResource() host: " + host);
        log.info("file: " + file.getAbsolutePath());
        
        if (file.isFile() && file.exists()) {
        	String md5 = "";
            try {
    			md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(file));
    		} catch (FileNotFoundException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
            log.info("md5: " + md5);    
        }
    }

    public File getFile() {
        return file;
    }

    public String getUniqueId() {
        String s = file.lastModified() + "_" + file.length() + "_" + file.getAbsolutePath();
        return s.hashCode() + "";
    }

    public String getName() {
        return file.getName();
    }

    public Object authenticate(String user, String password) {
        return factory.getSecurityManager().authenticate(user, password);
    }

    public Object authenticate(DigestResponse digestRequest) {
        return factory.getSecurityManager().authenticate(digestRequest);
    }

    public boolean isDigestAllowed() {
        return factory.isDigestAllowed();
    }

    public boolean authorise(Request request, Method method, Auth auth) {
        boolean b = factory.getSecurityManager().authorise(request, method, auth, this);
        if( log.isTraceEnabled()) {
            log.trace("authorise: result=" + b);
        }
        return b;
    }

    public String getRealm() {
        return factory.getRealm(this.host);
    }

    public Date getModifiedDate() {
        return new Date(file.lastModified());
    }

    public Date getCreateDate() {
        return null;
    }

    public int compareTo(Resource o) {
        return this.getName().compareTo(o.getName());
    }

    public void moveTo(CollectionResource newParent, String newName) {
    	log.info("###############################################");
        log.info("moveTo() newName: " + newName);
        if (newParent instanceof FsDirectoryResource) {
            FsDirectoryResource newFsParent = (FsDirectoryResource) newParent;
            File dest = new File(newFsParent.getFile(), newName);
            boolean ok = this.file.renameTo(dest);
            if (!ok) {
                throw new RuntimeException("Failed to move to: " + dest.getAbsolutePath());
            }
            this.file = dest;
        } else {
            throw new RuntimeException("Destination is an unknown type. Must be a FsDirectoryResource, is a: " + newParent.getClass());
        }
    }

    public void copyTo(CollectionResource newParent, String newName) {
    	log.info("###############################################");
        log.info("copyTo() newName: " + newName);
        if (newParent instanceof FsDirectoryResource) {
            FsDirectoryResource newFsParent = (FsDirectoryResource) newParent;
            File dest = new File(newFsParent.getFile(), newName);
            doCopy(dest);
        } else {
            throw new RuntimeException("Destination is an unknown type. Must be a FsDirectoryResource, is a: " + newParent.getClass());
        }
    }

    public void delete() {
    	log.info("###############################################");
        log.info("delete()");
        boolean ok = file.delete();
        if (!ok) {
            throw new RuntimeException("Failed to delete");
        }
    }

    public LockResult lock(LockTimeout timeout, LockInfo lockInfo) throws NotAuthorizedException {
        return factory.getLockManager().lock(timeout, lockInfo, this);
    }

    public LockResult refreshLock(String token) throws NotAuthorizedException {
        return factory.getLockManager().refresh(token, this);
    }

    public void unlock(String tokenId) throws NotAuthorizedException {
        factory.getLockManager().unlock(tokenId, this);
    }

    public LockToken getCurrentLock() {
        if (factory.getLockManager() != null) {
            return factory.getLockManager().getCurrentToken(this);
        } else {
            log.warn("getCurrentLock called, but no lock manager: file: " + file.getAbsolutePath());
            return null;
        }
    }
}

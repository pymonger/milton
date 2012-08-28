package com.ettrema.http.fs;

import com.bradmcevoy.http.*;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a directory in a physical file system.
 *
 */
public class FsDirectoryResource extends FsResource implements MakeCollectionableResource, PutableResource, CopyableResource, DeletableResource, MoveableResource, PropFindableResource, LockingCollectionResource, GetableResource {

    private static final Logger log = LoggerFactory.getLogger(FsDirectoryResource.class);

    public FsDirectoryResource(String host, FileSystemResourceFactory factory, File dir) {
        super(host, factory, dir);
        if (!dir.exists()) {
            throw new IllegalArgumentException("Directory does not exist: " + dir.getAbsolutePath());
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + dir.getAbsolutePath());
        }
        log.info("===============================================");
        log.info("Instantiated FsDirectoryResource() host: " + host);
        log.info("dir: " + dir.getAbsolutePath());
    }

    @Override
    public CollectionResource createCollection(String name) {
        File fnew = new File(file, name);
        
        log.info("###############################################");
        log.info("createCollection() name: " + name);
        log.info("fnew: " + fnew.getAbsolutePath());
        
        boolean ok = fnew.mkdir();
        if (!ok) {
            throw new RuntimeException("Failed to create: " + fnew.getAbsolutePath());
        }
        return new FsDirectoryResource(host, factory, fnew);
    }

    @Override
    public Resource child(String name) {
        File fchild = new File(file, name);
        
        log.info("###############################################");
        log.info("child() name: " + name);
        log.info("fchild: " + fchild.getAbsolutePath());
        
        return factory.resolveFile(this.host, fchild);

    }

    @Override
    public List<? extends Resource> getChildren() {
    	log.info("###############################################");
        log.info("getChildren() file: " + this.file.getAbsolutePath());
        
        ArrayList<FsResource> list = new ArrayList<FsResource>();
        File[] files = this.file.listFiles();
        if (files != null) {
            for (File fchild : files) {
                FsResource res = factory.resolveFile(this.host, fchild);
                if (res != null) {
                    list.add(res);
                } else {
                    log.error("Couldnt resolve file {}", fchild.getAbsolutePath());
                }
            }
        }
        return list;
    }

    /**
     * Will redirect if a default page has been specified on the factory
     *
     * @param request
     * @return
     */
    @Override
    public String checkRedirect(Request request) {
    	
    	log.info("###############################################");
        log.info("checkRedirect()");
        
        if (factory.getDefaultPage() != null) {
            return request.getAbsoluteUrl() + "/" + factory.getDefaultPage();
        } else {
            return null;
        }
    }

    @Override
    public Resource createNew(String name, InputStream in, Long length, String contentType) throws IOException {
        File dest = new File(this.getFile(), name);
        
        log.info("###############################################");
        log.info("createNew() name: " + name);
        log.info("dest: " + dest.getAbsolutePath());
        
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest);
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
        // todo: ignores contentType
        return factory.resolveFile(this.host, dest);

    }

    @Override
    protected void doCopy(File dest) {
    	
    	log.info("###############################################");
        log.info("doCopy() dest: " + dest.getAbsolutePath());
        
        try {
            FileUtils.copyDirectory(this.getFile(), dest);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to copy to:" + dest.getAbsolutePath(), ex);
        }
    }

    @Override
    public LockToken createAndLock(String name, LockTimeout timeout, LockInfo lockInfo) throws NotAuthorizedException {
        File dest = new File(this.getFile(), name);
        
        log.info("###############################################");
        log.info("createAndLock() name: " + name);
        log.info("dest: " + dest.getAbsolutePath());
        
        createEmptyFile(dest);
        FsFileResource newRes = new FsFileResource(host, factory, dest);
        LockResult res = newRes.lock(timeout, lockInfo);
        return res.getLockToken();
    }

    private void createEmptyFile(File file) {
    	
    	log.info("###############################################");
        log.info("createEmptyFile() file: " + file.getAbsolutePath());
        
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(fout);
        }
    }

    /**
     * Will generate a listing of the contents of this directory, unless the
     * factory's allowDirectoryBrowsing has been set to false.
     *
     * If so it will just output a message saying that access has been disabled.
     *
     * @param out
     * @param range
     * @param params
     * @param contentType
     * @throws IOException
     * @throws NotAuthorizedException
     */
    @Override
    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException, NotAuthorizedException {
        String subpath = getFile().getCanonicalPath().substring(factory.getRoot().getCanonicalPath().length()).replace('\\', '/');
        String uri = subpath;
        
        log.info("###############################################");
        log.info("sendContent() uri: " + uri);
        
        //String uri = "/" + factory.getContextPath() + subpath;
        XmlWriter w = new XmlWriter(out);
        w.open("html");
        w.open("head");
        w.writeText(""
                + "<script type=\"text/javascript\" language=\"javascript1.1\">\n"
                + "    var fNewDoc = false;\n"
                + "  </script>\n"
                + "  <script LANGUAGE=\"VBSCRIPT\">\n"
                + "    On Error Resume Next\n"
                + "    Set EditDocumentButton = CreateObject(\"SharePoint.OpenDocuments.3\")\n"
                + "    fNewDoc = IsObject(EditDocumentButton)\n"
                + "  </script>\n"
                + "  <script type=\"text/javascript\" language=\"javascript1.1\">\n"
                + "    var L_EditDocumentError_Text = \"The edit feature requires a SharePoint-compatible application and Microsoft Internet Explorer 4.0 or greater.\";\n"
                + "    var L_EditDocumentRuntimeError_Text = \"Sorry, couldnt open the document.\";\n"
                + "    function editDocument(strDocument) {\n"
                + "      if (fNewDoc) {\n"
                + "        if (!EditDocumentButton.EditDocument(strDocument)) {\n"
                + "          alert(L_EditDocumentRuntimeError_Text); \n"
                + "        }\n"
                + "      } else { \n"
                + "        alert(L_EditDocumentError_Text); \n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n");



        w.close("head");
        w.open("body");
        w.begin("h1").open().writeText(this.getName()).close();
        w.open("table");
        for (Resource r : getChildren()) {
            w.open("tr");

            w.open("td");
            String path = buildHref(uri, r.getName());
            w.begin("a").writeAtt("href", path).open().writeText(r.getName()).close();

            w.begin("a").writeAtt("href", "#").writeAtt("onclick", "editDocument('" + path + "')").open().writeText("(edit with office)").close();

            w.close("td");

            w.begin("td").open().writeText(r.getModifiedDate() + "").close();
            w.close("tr");
        }
        w.close("table");
        w.close("body");
        w.close("html");
        w.flush();
    }

    @Override
    public Long getMaxAgeSeconds(Auth auth) {
    	log.info("###############################################");
        log.info("getMaxAgeSeconds()");
        return null;
    }

    @Override
    public String getContentType(String accepts) {
    	log.info("###############################################");
        log.info("getContentType()");
        return "text/html";
    }

    @Override
    public Long getContentLength() {
    	log.info("###############################################");
        log.info("getContentLength()");
        return null;
    }

    private String buildHref(String uri, String name) {
    	log.info("###############################################");
        log.info("buildHref() uri: " + uri);
        log.info("name: " + name);
        
        // hmm, we're ignoring the path passed in uri. Dodgy...
        String abUrl = HttpManager.request().getAbsoluteUrl();
        if (!abUrl.endsWith("/")) {
            abUrl += "/";
        }
        log.warn("url: " + abUrl);
        if (ssoPrefix == null) {
            return abUrl + name;
        } else {
            // This is to match up with the prefix set on SimpleSSOSessionProvider in MyCompanyDavServlet
            String s = insertSsoPrefix(abUrl, ssoPrefix);
            return s += name;
        }
    }

    public static String insertSsoPrefix(String abUrl, String prefix) {
    	log.info("###############################################");
        log.info("insertSsoPrefix() abUrl: " + abUrl);
        log.info("prefix: " + prefix);
        
        // need to insert the ssoPrefix immediately after the host and port
        int pos = abUrl.indexOf("/", 8);
        String s = abUrl.substring(0, pos) + "/" + prefix;
        s += abUrl.substring(pos);
        return s;
    }
}

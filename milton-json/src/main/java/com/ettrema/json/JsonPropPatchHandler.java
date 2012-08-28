package com.ettrema.json;

import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.ConflictException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.webdav.PropFindResponse;
import com.bradmcevoy.http.webdav.PropFindResponse.NameAndError;
import com.bradmcevoy.http.webdav.PropPatchRequestParser.ParseResult;
import com.bradmcevoy.http.webdav.PropPatchSetter;
import com.bradmcevoy.http.webdav.PropPatchableSetter;
import com.bradmcevoy.http.webdav.WebDavProtocol;
import com.bradmcevoy.property.DefaultPropertyAuthoriser;
import com.bradmcevoy.property.PropertyAuthoriser;
import com.bradmcevoy.property.PropertyAuthoriser.CheckResult;
import com.ettrema.common.LogUtils;
import com.ettrema.event.EventManager;
import com.ettrema.event.PropPatchEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brad
 */
public class JsonPropPatchHandler {

    private static final Logger log = LoggerFactory.getLogger(JsonPropPatchHandler.class);
    private final PropPatchSetter patchSetter;
    private final PropertyAuthoriser permissionService;
    private final EventManager eventManager;

    public JsonPropPatchHandler(PropPatchSetter patchSetter, PropertyAuthoriser permissionService, EventManager eventManager) {
        this.patchSetter = patchSetter;
        this.permissionService = permissionService;
        this.eventManager = eventManager;
    }

    /**
     * Uses a PropPatchableSetter
     */
    public JsonPropPatchHandler() {
        this.patchSetter = new PropPatchableSetter();
        this.permissionService = new DefaultPropertyAuthoriser();
        this.eventManager = null;
    }

    public PropFindResponse process(Resource wrappedResource, String encodedUrl, Map<String, String> params) throws NotAuthorizedException, ConflictException, BadRequestException {
        log.trace("process");
        Map<QName, String> fields = new HashMap<QName, String>();
        for (String fieldName : params.keySet()) {
            String sFieldValue = params.get(fieldName);
            QName qn;
            if (fieldName.contains(":")) {
                // name is of form uri:local  E.g. MyDav:authorName
                String parts[] = fieldName.split(":");
                String nsUri = parts[0];
                String localName = parts[1];
                qn = new QName(nsUri, localName);
            } else {
                // name is simple form E.g. displayname, default nsUri to DAV
                qn = new QName(WebDavProtocol.NS_DAV.getPrefix(), fieldName);
            }
            log.debug("field: " + qn);
            fields.put(qn, sFieldValue);
        }

        ParseResult parseResult = new ParseResult(fields, null);

        if (log.isTraceEnabled()) {
            log.trace("check permissions with: " + permissionService.getClass());
        }
        Set<PropertyAuthoriser.CheckResult> errorFields = permissionService.checkPermissions(HttpManager.request(), Method.PROPPATCH, PropertyAuthoriser.PropertyPermission.WRITE, fields.keySet(), wrappedResource);
        if (errorFields != null && errorFields.size() > 0) {
            log.info("authorisation errors: " + errorFields.size());
            if (log.isTraceEnabled()) {
                for (CheckResult e : errorFields) {
                    LogUtils.trace(log, " - field error: ", e.getField(), e.getStatus(), e.getDescription());
                }
            }
            throw new NotAuthorizedException(wrappedResource);
        } else {
            LogUtils.trace(log, "setting properties with", patchSetter.getClass());
            PropFindResponse resp = patchSetter.setProperties(encodedUrl, parseResult, wrappedResource);
            if (eventManager != null) {
                log.trace("fire event");
                eventManager.fireEvent(new PropPatchEvent(wrappedResource, resp));
            } else {
                log.trace("no event manager");
            }
            if (resp.getErrorProperties().size() > 0) {
                LogUtils.warn(log, "Encountered errors setting fields with patch setter", patchSetter.getClass());
            }
            if (log.isTraceEnabled()) {
                if (resp.getErrorProperties().size() > 0) {
                    for (List<NameAndError> e : resp.getErrorProperties().values()) {
                        for (NameAndError ne : e) {
                            LogUtils.trace(log, " - field error setting properties: ", ne.getName(), ne.getError());
                        }
                    }
                }
            }
            return resp;

        }
    }

    public PropertyAuthoriser getPermissionService() {
        return permissionService;
    }
}

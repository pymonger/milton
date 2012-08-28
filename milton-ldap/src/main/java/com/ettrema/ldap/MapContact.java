package com.ettrema.ldap;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author brad
 */
public class MapContact extends HashMap<String, String> implements LdapContact {

	private final String userName;

	public MapContact(String id) {
		this.userName = id;
	}
	
		
	@Override
	public String getUniqueId() {
		return userName + hashCode();
	}

	@Override
	public Date getCreateDate() {
		return null;
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public Object authenticate(String user, String password) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean authorise(Request request, Method method, Auth auth) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getRealm() {
		return "ldapRealm";
	}

	@Override
	public Date getModifiedDate() {
		return null;
	}

	@Override
	public String checkRedirect(Request request) {
		return null;
	}

	
}

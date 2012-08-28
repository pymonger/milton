package com.ettrema.http.caldav.demo;

import com.bradmcevoy.http.Resource;
import com.ettrema.ldap.Condition;
import com.ettrema.ldap.LdapContact;
import com.ettrema.ldap.LdapPrincipal;
import com.ettrema.ldap.UserFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brad
 */
public class TLdapUserFactory implements UserFactory {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TLdapUserFactory.class);
	private final TResourceFactory resourceFactory;

	public TLdapUserFactory(TResourceFactory resourceFactory) {
		this.resourceFactory = resourceFactory;
	}

	@Override
	public String getUserPassword(String userName) {
		TCalDavPrincipal user = TResourceFactory.findUser(userName);
		if( user == null ) {
			return null;
		} else {
			return user.getPassword();
		}
	}

	@Override
	public LdapPrincipal getUser(String userName, String password) {
		TCalDavPrincipal user = TResourceFactory.findUser(userName);
		if (user.authenticate(password)) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	public List<LdapContact> galFind(Condition condition, int sizeLimit) {
		log.trace("galFind");
		List<LdapContact> results = new ArrayList<LdapContact>();

		for (Resource r : resourceFactory.getUsers()) {
			if (r instanceof TCalDavPrincipal) {
				TCalDavPrincipal user = (TCalDavPrincipal) r;
				if (condition == null || condition.isMatch(user)) {
					log.debug("searchContacts: add to results:" + user.getName());
					results.add(user);
					if (results.size() >= sizeLimit) {
						break;
					}
				}
			}
		}
		log.debug("galFind: results: " + results.size());
		return results;
	}
}

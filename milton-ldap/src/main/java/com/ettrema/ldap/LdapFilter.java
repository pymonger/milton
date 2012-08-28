package com.ettrema.ldap;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author brad
 */
public interface LdapFilter {

	Condition getContactSearchFilter();

	List<LdapContact> findInGAL(LdapPrincipal user, Set<String> returningAttributes, int sizeLimit) throws IOException;

	void add(LdapFilter filter);

	boolean isFullSearch();

	boolean isMatch(LdapContact person);

}

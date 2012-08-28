package com.ettrema.ldap;

import java.util.List;

/**
 * An LDAP principal (ie a user) is simply a contact which can contain other contacts, since
 * we often allow users to maintain their own private address books as well as
 * accessing the global contact list
 * 
 * Note that we imply certain meaning to properties defined elsewhere. The name
 * of the Resource is assumed to be the username of the principal, so is mapped
 * onto the "uid" ldap attribute.
 *
 * @author brad
 */
public interface LdapPrincipal extends LdapContact {


	/**
	 * Search for contacts in this user's private contact list. Generally these contacts
	 * will not be User accounts
	 * 
	 * @param contactReturningAttributes
	 * @param condition
	 * @param maxCount
	 * @return 
	 */
	List<LdapContact> searchContacts(Condition condition, int maxCount);
}

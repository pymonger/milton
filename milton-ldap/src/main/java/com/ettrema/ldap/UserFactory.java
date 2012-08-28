package com.ettrema.ldap;

import java.util.List;

/**
 *
 * @author brad
 */
public interface UserFactory {
	

	/**
	 * Used for SASL authentication
	 * 
	 * @param userName
	 * @return 
	 */
	String getUserPassword(String userName);

	LdapPrincipal getUser(String userName, String password);
	
	/**
	 * Search for contacts in the Global Address List
	 * 
	 * @param equalTo
	 * @param convertLdapToContactReturningAttributes
	 * @param sizeLimit
	 * @return 
	 */
	List<LdapContact> galFind(Condition equalTo, int sizeLimit);	
}

package com.ettrema.ldap;

import com.bradmcevoy.http.PropFindableResource;

/**
 * Represents an entry in an address book.
 *
 * Just a marker interface on top of PropFindableResource, ldap properties are
 * mapped onto dav property names and the normal milton property source processing
 * is applied.
 * 
 * This means that implementations of LdapContact may choose to expose their
 * properties via getters and setters (with BeanPropertyResource annotation)
 * or other property source implementations
 * 
 * @author brad
 */
public interface LdapContact extends PropFindableResource {
	
	
}

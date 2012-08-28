package com.ettrema.ldap;

/**
 *
 * @author brad
 */
public class NotCondition implements Condition {

	protected final Condition condition;

	protected NotCondition(Condition condition) {
		this.condition = condition;
	}

	public boolean isEmpty() {
		return condition.isEmpty();
	}

	public boolean isMatch(LdapContact contact) {
		return !condition.isMatch(contact);
	}

//	public void appendTo(StringBuilder buffer) {
//		buffer.append("(Not ");
//		condition.appendTo(buffer);
//		buffer.append(')');
//	}
}
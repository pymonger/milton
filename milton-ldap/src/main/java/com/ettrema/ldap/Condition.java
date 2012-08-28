package com.ettrema.ldap;

public interface Condition {

    @SuppressWarnings({"JavaDoc"})
    public enum Operator {
        Or, And, Not, IsEqualTo,
        IsGreaterThan, IsGreaterThanOrEqualTo,
        IsLessThan, IsLessThanOrEqualTo,
        IsNull, IsTrue, IsFalse,
        Like, StartsWith, Contains
    }	
	
	/**
	 * Append condition to buffer.
	 *
	 * @param buffer search filter buffer
	 */
	//void appendTo(StringBuilder buffer);

	/**
	 * True if condition is empty.
	 *
	 * @return true if condition is empty
	 */
	boolean isEmpty();

	/**
	 * Test if the contact matches current condition.
	 *
	 * @param contact Exchange Contact
	 * @return true if contact matches condition
	 */
	boolean isMatch(LdapContact contact);
}
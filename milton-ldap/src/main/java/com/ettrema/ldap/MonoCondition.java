package com.ettrema.ldap;

/**
 *
 * @author brad
 */
public class MonoCondition implements Condition {
	private LdapPropertyMapper propertyMapper;
	private final String attributeName;
	private final Operator operator;

	protected MonoCondition(LdapPropertyMapper propertyMapper, String attributeName, Operator operator) {
		this.attributeName = attributeName;
		this.operator = operator;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean isMatch(LdapContact contact) {
		String actualValue = propertyMapper.getLdapPropertyValue(attributeName, contact);
		return (operator == Operator.IsNull && actualValue == null)
				|| (operator == Operator.IsFalse && "false".equals(actualValue))
				|| (operator == Operator.IsTrue && "true".equals(actualValue));
	}
}
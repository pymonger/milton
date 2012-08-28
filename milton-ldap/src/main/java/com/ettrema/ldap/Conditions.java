package com.ettrema.ldap;

import com.ettrema.ldap.Condition.Operator;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author brad
 */
public class Conditions {

	protected static enum FolderQueryTraversal {
		Shallow, Deep
	}
	
	public final Map<Operator, String> OPERATOR_MAP = new EnumMap<Operator, String>(Operator.class);

	private final LdapPropertyMapper propertyMapper;
	
	

	public Conditions(final LdapPropertyMapper propertyMapper) {
		this.propertyMapper = propertyMapper;				
		OPERATOR_MAP.put(Operator.IsEqualTo, " = ");
		OPERATOR_MAP.put(Operator.IsGreaterThanOrEqualTo, " >= ");
		OPERATOR_MAP.put(Operator.IsGreaterThan, " > ");
		OPERATOR_MAP.put(Operator.IsLessThanOrEqualTo, " <= ");
		OPERATOR_MAP.put(Operator.IsLessThan, " < ");
		OPERATOR_MAP.put(Operator.Like, " like ");
		OPERATOR_MAP.put(Operator.IsNull, " is null");
		OPERATOR_MAP.put(Operator.IsFalse, " = false");
		OPERATOR_MAP.put(Operator.IsTrue, " = true");
		OPERATOR_MAP.put(Operator.StartsWith, " = ");
		OPERATOR_MAP.put(Operator.Contains, " = ");
	}
	
	

	public MultiCondition and(Condition... condition) {
		return new MultiCondition(Operator.And, condition);
	}

	public MultiCondition or(Condition... condition) {
		return new MultiCondition(Operator.Or, condition);
	}

	public Condition not(Condition condition) {
		if (condition == null) {
			return null;
		} else {
			return new NotCondition(condition);
		}
	}

	public Condition isEqualTo(String attributeName, String value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.IsEqualTo, value);
	}
	public Condition isEqualTo(String attributeName, int value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.IsEqualTo, value);
	}

//	public static Condition headerIsEqualTo(String headerName, String value) {
//		return new HeaderCondition(headerName, Operator.IsEqualTo, value);
//	}

	public Condition gte(String attributeName, String value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.IsGreaterThanOrEqualTo, value);
	}

	public Condition lte(String attributeName, String value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.IsLessThanOrEqualTo, value);
	}

	public Condition lt(String attributeName, String value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.IsLessThan, value);
	}

	public Condition gt(String attributeName, String value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.IsGreaterThan, value);
	}

	public Condition contains(String attributeName, String value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.Like, value);
	}

	public Condition startsWith(String attributeName, String value) {
		return new AttributeCondition(propertyMapper, attributeName, Operator.StartsWith, value);
	}

	public Condition isNull(String attributeName) {
		return new MonoCondition(propertyMapper, attributeName, Operator.IsNull);
	}

	public Condition isTrue(String attributeName) {
		return new MonoCondition(propertyMapper, attributeName, Operator.IsTrue);
	}

	public Condition isFalse(String attributeName) {
		return new MonoCondition(propertyMapper, attributeName, Operator.IsFalse);
	}
}

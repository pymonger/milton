package com.ettrema.ldap;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brad
 */
public class MultiCondition implements Condition {

	protected final Operator operator;
	protected final List<Condition> conditions;

	protected MultiCondition(Operator operator, Condition... conditions) {
		this.operator = operator;
		this.conditions = new ArrayList<Condition>();
		for (Condition condition : conditions) {
			if (condition != null) {
				this.conditions.add(condition);
			}
		}
	}

//	@Override
//	public void appendTo(StringBuilder buffer) {
//		boolean first = true;
//
//		for (Condition condition : conditions) {
//			if (condition != null && !condition.isEmpty()) {
//				if (first) {
//					buffer.append('(');
//					first = false;
//				} else {
//					buffer.append(' ').append(operator).append(' ');
//				}
//				condition.appendTo(buffer);
//			}
//		}
//		// at least one non empty condition
//		if (!first) {
//			buffer.append(')');
//		}
//	}

	/**
	 * Conditions list.
	 *
	 * @return conditions
	 */
	public List<Condition> getConditions() {
		return conditions;
	}

	/**
	 * Condition operator.
	 *
	 * @return operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Add a new condition.
	 *
	 * @param condition single condition
	 */
	public void add(Condition condition) {
		if (condition != null) {
			conditions.add(condition);
		}
	}

	public boolean isEmpty() {
		boolean isEmpty = true;
		for (Condition condition : conditions) {
			if (!condition.isEmpty()) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;
	}

	public boolean isMatch(LdapContact contact) {
		if (operator == Operator.And) {
			for (Condition condition : conditions) {
				if (!condition.isMatch(contact)) {
					return false;
				}
			}
			return true;
		} else if (operator == Operator.Or) {
			for (Condition condition : conditions) {
				if (condition.isMatch(contact)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
}

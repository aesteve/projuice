package io.projuice.model;

public enum Role {
	ADMIN,
	OBSERVER,
	REPORTER,
	SPECIFICATOR,
	DEVELOPER,
	QA,
	MEMBER;

	/**
	 * Permission checking
	 * 
	 * @param other
	 * @return true if this role has superior access level than the other, false otherwise
	 */
	public boolean isAtLeast(Role other) {
		if (this.equals(ADMIN)) {
			return true;
		} else if (other.equals(MEMBER)) {
			return true;
		} else {
			return this.equals(other);
		}
	}
}

package io.projuice.model.project;

import java.util.Set;

public class Phase {

	private String name;
	private Set<String> rolesInvolved;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getRolesInvolved() {
		return rolesInvolved;
	}

	public void setRolesInvolved(Set<String> rolesInvolved) {
		this.rolesInvolved = rolesInvolved;
	}

}

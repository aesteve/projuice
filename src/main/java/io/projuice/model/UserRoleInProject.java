package io.projuice.model;

public class UserRoleInProject {
	
	private ProjuiceUser user;
	private Role role;

	public ProjuiceUser getUser() {
		return user;
	}

	public void setUser(ProjuiceUser user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}

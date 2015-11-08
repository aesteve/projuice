package io.projuice.model;

public class UserRoleInProject {
	
	private String username;
	private Role role;

	public UserRoleInProject() {
		
	}

	public UserRoleInProject(String username, Role role) {
		this.username = username;
		this.role = role;
	}
	
	public String getUser() {
		return username;
	}

	public void setUser(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}

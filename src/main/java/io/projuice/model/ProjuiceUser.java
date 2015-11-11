package io.projuice.model;

import io.projuice.auth.ProjuiceAuthProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

import org.boon.json.annotations.JsonIgnore;

public class ProjuiceUser implements Comparable<ProjuiceUser>, User {

	private String username;
	private String emailAddress;
	private String githubId;
	@JsonIgnore
	private String password;

	@JsonIgnore
	private ProjuiceAuthProvider provider;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getGithubId() {
		return githubId;
	}

	public void setGithubId(String githubId) {
		this.githubId = githubId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValid() {
		return username != null && emailAddress != null && password != null;
	}

	public boolean isMemberOf(Project project) {
		return project.getParticipants().stream().anyMatch(this::belongsToRole);
	}

	public boolean isAdminOf(Project project) {
		return project.getParticipants().stream().anyMatch(this::isAdmin);
	}

	public boolean belongsToRole(UserRoleInProject role) {
		return role.getUser().equals(username);
	}

	public boolean isAdmin(UserRoleInProject role) {
		return role.getUser().equals(username) && role.getRole() == Role.ADMIN;
	}

	@Override
	public int compareTo(ProjuiceUser other) {
		if (other == null) {
			return -1;
		}
		return username.compareTo(other.getUsername());
	}

	@Override
	public User isAuthorised(String authority, Handler<AsyncResult<Boolean>> resultHandler) {
		provider.isAuthorised(this, authority, resultHandler);
		return this;
	}

	@Override
	public User clearCache() {
		provider.clearFor(this);
		return this;
	}

	@Override
	public JsonObject principal() {
		return new JsonObject()
				.put("username", username)
				.put("password", password);
	}

	@Override
	public void setAuthProvider(AuthProvider authProvider) {
		this.provider = (ProjuiceAuthProvider) authProvider;
	}
}

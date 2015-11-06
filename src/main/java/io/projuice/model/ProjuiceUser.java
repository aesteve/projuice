package io.projuice.model;

import io.projuice.auth.ProjuiceAuthProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.boon.json.annotations.JsonIgnore;

@Entity
public class ProjuiceUser implements Comparable<ProjuiceUser>, User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String username;
	private String emailAddress;
	private String githubId;
	private String password;

	@ManyToMany(mappedBy = "assignees")
	@JsonIgnore
	private Set<Issue> issues;

	@OneToMany(mappedBy = "project")
	@JsonIgnore
	private Set<UserRoleInProject> projects;

	@Transient
	@JsonIgnore
	private ProjuiceAuthProvider provider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Set<UserRoleInProject> getProjects() {
		return projects;
	}

	public void setProjects(Set<UserRoleInProject> projects) {
		this.projects = projects;
	}

	public Set<Issue> getIssues() {
		return issues;
	}

	public void setIssues(Set<Issue> issues) {
		this.issues = issues;
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

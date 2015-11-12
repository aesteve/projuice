package io.projuice.model.api;

import io.projuice.model.ProjuiceUser;

public class ApiUser {

	private String username;
	private String emailAddress;
	private String githubId;

	public ApiUser() {

	}

	public ApiUser(ProjuiceUser user) {
		username = user.getUsername();
		emailAddress = user.getEmailAddress();
		githubId = user.getGithubId();
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

}

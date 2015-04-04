package io.projuice.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.boon.json.annotations.JsonIgnore;

@Entity
public class ProjuiceUser implements Comparable<ProjuiceUser> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String username;
	private String emailAddress;
	private String githubId;
	
	@ManyToMany(fetch=FetchType.EAGER, mappedBy="assignees")
	@JsonIgnore
	private Set<Issue> issues;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="project")
	@JsonIgnore
	private Set<UserRoleInProject> projects;
	
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
	@Override
	public int compareTo(ProjuiceUser other) {
		if (other == null) {
			return -1;
		}
		return username.compareTo(other.getUsername());
	}
}

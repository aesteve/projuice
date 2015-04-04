package io.projuice.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private String githubUrl;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="user")
	private Set<UserRoleInProject> participants;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="project")
	private Set<Label> labels;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="project")
	private Set<Issue> issues;
	
	public String getGithubUrl() {
		return githubUrl;
	}
	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<UserRoleInProject> getParticipants() {
		return participants;
	}
	public void setParticipants(Set<UserRoleInProject> participants) {
		this.participants = participants;
	}
	public Set<Label> getLabels() {
		return labels;
	}
	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}
	public Set<Issue> getIssues() {
		return issues;
	}
	public void setIssues(Set<Issue> issues) {
		this.issues = issues;
	}
}

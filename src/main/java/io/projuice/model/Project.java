package io.projuice.model;

import java.util.HashSet;
import java.util.Set;

import com.github.aesteve.vertx.nubes.exceptions.ValidationException;

public class Project {
	
	private String id;
	private String name;
	private String description;
	private String githubUrl;

	private Set<UserRoleInProject> participants;
	private Set<Label> labels;
	
	public Project() {
		participants = new HashSet<>();
		labels = new HashSet<>();
	}
	
	public void generateId() {
		setId(name.replaceAll("\\s+","-").toLowerCase());
	}
	
	public void validate() throws ValidationException {
		if (name == null) {
			throw new ValidationException("Project name is mandatory");
		}
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
}

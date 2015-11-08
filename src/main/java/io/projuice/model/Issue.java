package io.projuice.model;

import java.util.Set;
import java.util.TreeSet;

import io.projuice.model.issue.IssueStatus;
import io.projuice.model.issue.IssueType;

public class Issue {

	private String id;
	private IssueType type;
	private IssueStatus status;
	private String name;
	private String description;

	private String projectId;

	private Set<ProjuiceUser> assignees;
	private Set<Label> labels;

	public Issue() {
		labels = new TreeSet<Label>();
		assignees = new TreeSet<ProjuiceUser>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public IssueType getType() {
		return type;
	}

	public void setType(IssueType type) {
		this.type = type;
	}

	public IssueStatus getStatus() {
		return status;
	}

	public void setStatus(IssueStatus status) {
		this.status = status;
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

	public Set<ProjuiceUser> getAssignees() {
		return assignees;
	}

	public void setAssignees(Set<ProjuiceUser> assignees) {
		this.assignees = assignees;
	}

	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	public void assign(ProjuiceUser user) {
		assignees.add(user);
	}
}

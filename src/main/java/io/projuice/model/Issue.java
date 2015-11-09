package io.projuice.model;

import io.projuice.model.issue.IssueStatus;
import io.projuice.model.issue.IssueType;
import io.projuice.utils.StringUtils;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.github.aesteve.vertx.nubes.exceptions.ValidationException;

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
		status = IssueStatus.WAITING;
	}

	public void generateId() {
		id = StringUtils.smallHash(projectId + ":" + Long.valueOf(new Date().getTime()).toString());
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

	public void validate() throws ValidationException {
		if (name == null) {
			throw new ValidationException("Issue name is mandatory");
		}
		if (type == null) {
			throw new ValidationException("Issue type is mandatory");
		}
		if (status == null) {
			throw new ValidationException("Issue status is mandatory");
		}
	}

	public void close() {
		status = IssueStatus.CLOSED;
	}

	public void assign(ProjuiceUser user) {
		assignees.add(user);
	}
}

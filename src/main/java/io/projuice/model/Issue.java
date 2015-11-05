package io.projuice.model;

import io.projuice.model.issue.IssueStatus;
import io.projuice.model.issue.IssueType;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.boon.json.annotations.JsonIgnore;

@Entity
public class Issue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private IssueType type;
	private IssueStatus status;
	private String name;
	private String description;

	@ManyToOne
	@JsonIgnore
	private Project project;

	@ManyToMany
	private Set<ProjuiceUser> assignees;

	@ManyToMany
	private Set<Label> labels;

	public Issue() {
		labels = new TreeSet<Label>();
		assignees = new TreeSet<ProjuiceUser>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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

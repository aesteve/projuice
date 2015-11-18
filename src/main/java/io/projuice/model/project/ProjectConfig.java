package io.projuice.model.project;

import java.util.List;

public class ProjectConfig {

	private List<Phase> phases;
	private List<Workflow> workflows;

	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

	public List<Workflow> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(List<Workflow> workflows) {
		this.workflows = workflows;
	}

}

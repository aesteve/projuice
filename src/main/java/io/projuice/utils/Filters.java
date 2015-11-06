package io.projuice.utils;

import io.projuice.model.UserRoleInProject;

import java.util.Set;

public class Filters {
	public static boolean hasProject(Set<UserRoleInProject> set, Long projectId) {
		return set.stream().filter(Predicates.getRole(projectId)).findFirst().isPresent();
	}
}

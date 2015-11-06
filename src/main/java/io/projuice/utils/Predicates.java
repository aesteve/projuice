package io.projuice.utils;

import io.projuice.model.UserRoleInProject;

import java.util.function.Predicate;

public class Predicates {

	public static Predicate<UserRoleInProject> getRole(Long projectId) {
		return role -> {
			return projectId.equals(role.getProject().getId());
		};
	}
}

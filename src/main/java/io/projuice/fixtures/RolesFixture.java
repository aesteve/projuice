package io.projuice.fixtures;

import io.projuice.WebVerticle;
import io.projuice.model.*;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.nubes.fixtures.Fixture;
import io.vertx.hibernate.queries.FindBy;

public class RolesFixture extends Fixture {

	@Override
	public int executionOrder() {
		return 2;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		WebVerticle.hibernateService.withinTransaction(em -> {
			Project projuice = new FindBy<Project, String>(Project.class, em).find("name", "Projuice");
			ProjuiceUser arnaud = new FindBy<ProjuiceUser, String>(ProjuiceUser.class, em).find("username", "Arnaud");
			UserRoleInProject role = new UserRoleInProject();
			role.setProject(projuice);
			role.setUser(arnaud);
			role.setRole(Role.ADMIN);
			em.persist(role);
			return role;
		}, res -> {
			if (res.succeeded()) {
				future.complete();
			} else {
				future.fail(res.cause());
			}
		});
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		future.complete();
	}

}

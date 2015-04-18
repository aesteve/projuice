package io.projuice.fixtures;

import io.projuice.WebVerticle;
import io.projuice.model.Project;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.hibernate.HibernateService;
import io.vertx.nubes.fixtures.Fixture;

public class ProjectsFixture extends Fixture {

	@Override
	public int executionOrder() {
		return 1;
	}
	
	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		HibernateService hibernate = WebVerticle.hibernateService;
		hibernate.withinTransaction(em -> {
			Project projuice = new Project();
			projuice.setName("Projuice");
			projuice.setDescription("The simple project manager");
			projuice.setGithubUrl("http://github.com/aesteve/projuice");
			em.persist(projuice);
			return projuice;
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

package io.projuice.fixtures;

import io.projuice.model.Project;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.services.HibernateService;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.fixtures.Fixture;

public class ProjectsFixture extends Fixture {

	public static Long ProjuiceID;

	@Service(HibernateNubes.HIBERNATE_SERVICE_NAME)
	private HibernateService hibernate;

	@Override
	public int executionOrder() {
		return 1;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		hibernate.withinTransaction(em -> {
			Project projuice = new Project();
			projuice.setName("Projuice");
			projuice.setDescription("The simple project manager");
			projuice.setGithubUrl("http://github.com/aesteve/projuice");
			em.persist(projuice);
			return projuice;
		}, res -> {
			if (res.failed()) {
				future.fail(res.cause());
				return;
			}
			Project projuice = res.result();
			ProjuiceID = projuice.getId();
			future.complete();
		});
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		future.complete();
	}

}

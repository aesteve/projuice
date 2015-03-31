package io.projuice.fixtures;

import io.projuice.WebVerticle;
import io.projuice.model.Project;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.hibernate.HibernateService;
import io.vertx.mvc.fixtures.Fixture;

public class ProjectsFixture implements Fixture {

	
	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		HibernateService hibernate = WebVerticle.hibernateService;
		hibernate.createSession(sessionResult -> {
			if (sessionResult.failed()) {
				future.fail(sessionResult.cause());
			} else {
				String sessionId = sessionResult.result();
				hibernate.beginTransaction(sessionId, txHandler -> {
					if (txHandler.failed()) {
						future.fail(txHandler.cause());
					} else {
						Project projuice = new Project();
						projuice.setName("Projuice");
						hibernate.persist(sessionId, projuice, persistHandler -> {
							if (persistHandler.failed()) {
								future.fail(persistHandler.cause());
							} else {
								hibernate.flushAndClose(sessionId, closeHandler -> {
									if (closeHandler.failed()) {
										future.fail(closeHandler.cause());
									} else {
										future.complete();
									}
								});
							}
						});
					}
				});
			}
		});
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		future.complete();
	}

}

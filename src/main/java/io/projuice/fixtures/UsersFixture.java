package io.projuice.fixtures;

import io.projuice.WebVerticle;
import io.projuice.model.ProjuiceUser;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.hibernate.HibernateService;
import io.vertx.nubes.fixtures.Fixture;

public class UsersFixture extends Fixture {
	
	@Override
	public int executionOrder() {
		return 0;
	}
	
	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		HibernateService hibernate = WebVerticle.hibernateService;
		hibernate.withinTransaction(em -> {
			ProjuiceUser arnaud = new ProjuiceUser();
			arnaud.setUsername("Arnaud");
			arnaud.setGithubId("aesteve");
			em.persist(arnaud);
			return arnaud;
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

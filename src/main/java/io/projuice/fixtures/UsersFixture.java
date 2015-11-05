package io.projuice.fixtures;

import io.projuice.model.ProjuiceUser;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.services.HibernateService;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.fixtures.Fixture;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

public class UsersFixture extends Fixture {

	@Service(HibernateNubes.HIBERNATE_SERVICE_NAME)
	HibernateService hibernate;

	@Override
	public int executionOrder() {
		return 0;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		hibernate.withinTransaction(em -> {
			ProjuiceUser arnaud = new ProjuiceUser();
			arnaud.setUsername("Arnaud");
			arnaud.setGithubId("aesteve");
			em.persist(arnaud);
			return arnaud;
		}, AsyncUtils.ignoreResult(future));
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		future.complete();
	}

}

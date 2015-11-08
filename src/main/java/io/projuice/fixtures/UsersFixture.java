package io.projuice.fixtures;

import java.util.Arrays;

import com.github.aesteve.nubes.orm.mongo.MongoNubes;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.annotations.auth.User;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.fixtures.Fixture;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

import io.projuice.model.ProjuiceUser;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class UsersFixture extends Fixture {

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	MongoService mongo;

	@Override
	public int executionOrder() {
		return 0;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		ProjuiceUser arnaud = new ProjuiceUser();
		arnaud.setUsername("Arnaud");
		arnaud.setPassword("secret");
		arnaud.setGithubId("aesteve");
		ProjuiceUser justRegistered = new ProjuiceUser();
		justRegistered.setUsername("BrandNew");
		justRegistered.setPassword("secret");
		justRegistered.setGithubId("someonewhodoesntexist");
		mongo.createMany(Arrays.asList(arnaud, justRegistered), AsyncUtils.ignoreResult(future));
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		mongo.deleteAll(new FindBy<>(User.class), AsyncUtils.ignoreResult(future));
	}
}

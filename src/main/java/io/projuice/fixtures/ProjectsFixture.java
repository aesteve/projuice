package io.projuice.fixtures;

import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import com.github.aesteve.nubes.orm.mongo.MongoNubes;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.fixtures.Fixture;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

public class ProjectsFixture extends Fixture {

	public final static String ProjuiceID = "projuice";

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	private MongoService mongo;

	@Override
	public int executionOrder() {
		return 1;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		Project projuice = new Project();
		projuice.setName("Projuice");
		projuice.setDescription("The simple project manager");
		projuice.setGithubUrl("http://github.com/aesteve/projuice");
		projuice.generateId();
		mongo.findBy(new FindBy<>(ProjuiceUser.class, "username", "Arnaud"), res -> {
			ProjuiceUser arnaud = res.result();
			projuice.addAdmin(arnaud);
			mongo.create(projuice, AsyncUtils.ignoreResult(future));
		});
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		mongo.deleteAll(new FindBy<>(Project.class), AsyncUtils.ignoreResult(future));
	}

}

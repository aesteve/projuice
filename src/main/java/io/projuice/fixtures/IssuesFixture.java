package io.projuice.fixtures;

import io.projuice.model.Issue;
import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.projuice.model.issue.IssueType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import com.github.aesteve.nubes.orm.mongo.MongoNubes;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.fixtures.Fixture;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

public class IssuesFixture extends Fixture {

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	MongoService mongo;

	@Override
	public int executionOrder() {
		return 3;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		System.out.println("FIND USER");
		mongo.findBy(new FindBy<ProjuiceUser>(ProjuiceUser.class, "username", "Arnaud"), res -> {
			ProjuiceUser arnaud = res.result();
			System.out.println("FIND PROJECT");
			mongo.findBy(new FindBy<Project>(Project.class, "id", ProjectsFixture.ProjuiceID), res2 -> {
				Project projuice = res2.result();
				if (projuice == null) {
					future.fail("Project not found");
					return;
				}
				Issue issue = new Issue();
				issue.setProjectId(projuice.getId());
				issue.setName("Design model");
				issue.setType(IssueType.ENHANCEMENT);
				issue.setDescription("## Design the whole projuice working model");
				issue.assign(arnaud);
				future.complete();
			});
		});
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		System.out.println("clear");
		mongo.deleteAll(new FindBy<>(Issue.class), AsyncUtils.ignoreResult(future));
	}

}

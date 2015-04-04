package io.projuice.fixtures;

import io.projuice.WebVerticle;
import io.projuice.model.Issue;
import io.projuice.model.Project;
import io.projuice.model.issue.IssueType;
import io.projuice.model.ProjuiceUser;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.hibernate.queries.FindBy;
import io.vertx.mvc.fixtures.Fixture;

public class IssuesFixture extends Fixture {

	@Override
	public int executionOrder() {
		return 3;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		WebVerticle.hibernateService.withinTransaction( em -> {
			ProjuiceUser arnaud = new FindBy<ProjuiceUser, String>(ProjuiceUser.class, em).find("username", "Arnaud");
			Project projuice = new FindBy<Project, String>(Project.class, em).find("name", "Projuice");
			Issue issue = new Issue();
			issue.setProject(projuice);
			issue.setName("Design model");
			issue.setType(IssueType.ENHANCEMENT);
			issue.setDescription("## Design the whole projuice working model");
			issue.assign(arnaud);
			em.persist(issue);
			return issue;
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
		// TODO Auto-generated method stub

	}

}

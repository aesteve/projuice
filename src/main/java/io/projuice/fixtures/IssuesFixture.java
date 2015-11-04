package io.projuice.fixtures;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.queries.FindBy;
import com.github.aesteve.nubes.hibernate.services.HibernateService;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.fixtures.Fixture;

import io.projuice.model.Issue;
import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.projuice.model.issue.IssueType;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class IssuesFixture extends Fixture {

	@Service(HibernateNubes.HIBERNATE_SERVICE_NAME)
	HibernateService hibernate;
	
	@Override
	public int executionOrder() {
		return 3;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		hibernate.withinTransaction( em -> {
			Issue issue = new Issue();
			hibernate.findBy(em, new FindBy<ProjuiceUser>(ProjuiceUser.class, "username", "Arnaud"), res -> {
				ProjuiceUser arnaud = res.result();
				hibernate.findBy(em, new FindBy<Project>(Project.class, "name", "Projuice"), res2 -> {
					Project projuice = res2.result();
					issue.setProject(projuice);
					issue.setName("Design model");
					issue.setType(IssueType.ENHANCEMENT);
					issue.setDescription("## Design the whole projuice working model");
					issue.assign(arnaud);
					em.persist(issue);
				});
			});
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
		future.complete();
	}

}

package io.projuice.controllers.api;

import io.projuice.model.Issue;
import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.mongo.MongoNubes;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

public class CheckController {

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	protected MongoService mongo;

	protected void checkUserHasAccessToProject(RoutingContext context, ProjuiceUser user, String projectId, Handler<Project> handler) {
		FindBy<Project> findById = new FindBy<>(Project.class, "id", projectId);
		mongo.findBy(findById, AsyncUtils.failOr(context, res -> {
			Project project = res.result();
			if (project == null || !user.isMemberOf(project)) {
				context.fail(404);
			} else {
				handler.handle(project);
			}
		}));
	}

	protected void checkUserIsProjectAdmin(RoutingContext context, ProjuiceUser user, String projectId, Handler<Project> handler) {
		FindBy<Project> findById = new FindBy<>(Project.class, "id", projectId);
		mongo.findBy(findById, AsyncUtils.failOr(context, res -> {
			Project project = res.result();
			if (project == null || !user.isMemberOf(project)) {
				context.fail(404);
			} else if (!user.isAdminOf(project)) {
				context.fail(403);
			} else {
				handler.handle(project);
			}
		}));
	}

	protected void checkUserExists(RoutingContext context, String username, Handler<ProjuiceUser> handler) {
		FindBy<ProjuiceUser> findById = new FindBy<>(ProjuiceUser.class, "username", username);
		mongo.findBy(findById, AsyncUtils.failOr(context, res -> {
			ProjuiceUser user = res.result();
			if (user == null) {
				context.fail(404);
			} else {
				handler.handle(user);
			}
		}));
	}

	protected void checkIssueExists(RoutingContext context, String issueId, Handler<Issue> handler) {
		FindBy<Issue> findById = new FindBy<>(Issue.class, "id", issueId);
		mongo.findBy(findById, AsyncUtils.failOr(context, res -> {
			Issue issue = res.result();
			if (issue == null) {
				context.fail(404);
			} else {
				handler.handle(issue);
			}
		}));
	}
}

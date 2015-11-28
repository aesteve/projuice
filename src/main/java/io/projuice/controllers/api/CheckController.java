package io.projuice.controllers.api;

import io.projuice.model.Issue;
import io.projuice.model.ProjuiceUser;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.mongo.MongoNubes;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

public abstract class CheckController {

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	protected MongoService mongo;

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

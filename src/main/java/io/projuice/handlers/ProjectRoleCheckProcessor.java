package io.projuice.handlers;

import io.projuice.annotations.ProjectRoleCheck;
import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.projuice.model.Role;
import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.handlers.impl.NoopAfterAllProcessor;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

public class ProjectRoleCheckProcessor extends NoopAfterAllProcessor implements AnnotationProcessor<ProjectRoleCheck> {

	private MongoService mongo;
	private Role[] roles;

	public ProjectRoleCheckProcessor(MongoService mongo, Role[] roles) {
		this.mongo = mongo;
		this.roles = roles;
	}

	@Override
	public void postHandle(RoutingContext context) {
		context.next();
	}

	@Override
	public void preHandle(RoutingContext context) {
		String projectId = context.request().getParam("projectId");
		ProjuiceUser user = (ProjuiceUser) context.user();
		System.out.println("user = " + user);
		System.out.println("projectId = " + projectId);
		if (projectId == null || user == null) {
			context.fail(401);
			return;
		}
		FindBy<Project> findById = new FindBy<>(Project.class, "id", projectId);
		mongo.findBy(findById, AsyncUtils.failOr(context, res -> {
			Project project = res.result();
			if (project == null || !user.hasRoleInProject(project, roles)) {
				context.fail(404);
				return;
			}
			context.next();
		}));
	}

	@Override
	public Class<? extends ProjectRoleCheck> getAnnotationType() {
		return ProjectRoleCheck.class;
	}
}

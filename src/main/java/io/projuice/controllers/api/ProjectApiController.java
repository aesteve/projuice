package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.projuice.model.UserRoleInProject;
import io.projuice.utils.Filters;
import io.vertx.ext.web.RoutingContext;

import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.annotations.Create;
import com.github.aesteve.nubes.hibernate.annotations.RetrieveById;
import com.github.aesteve.nubes.hibernate.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.hibernate.annotations.Update;
import com.github.aesteve.nubes.hibernate.queries.FindById;
import com.github.aesteve.nubes.hibernate.services.HibernateService;
import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.auth.Auth;
import com.github.aesteve.vertx.nubes.annotations.auth.User;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PATCH;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PUT;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.context.PaginationContext;
import com.github.aesteve.vertx.nubes.exceptions.http.impl.NotFoundException;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

@Controller("/api/1/projects")
@ContentType("application/json")
public class ProjectApiController {

	@Service(HibernateNubes.HIBERNATE_SERVICE_NAME)
	private HibernateService hibernate;

	@GET
	@RetrieveByQuery
	public CriteriaQuery<Project> list(PaginationContext pageContext, CriteriaBuilder builder) {
		CriteriaQuery<Project> crit = builder.createQuery(Project.class);
		crit.from(Project.class);
		return crit;
	}

	@POST
	@Create
	public Project createProject(@RequestBody Project project) {
		return project;
	}

	@GET("/:projectId/")
	@RetrieveById
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void getProject(RoutingContext context, @Param Long projectId, @User ProjuiceUser currentUser, Payload<FindById<Project>> payload) throws NotFoundException {
		// lazy proxy => within session
		hibernate.withEntityManager((em, future) -> {
			Set<UserRoleInProject> roles = currentUser.getProjects();
			if (roles == null || roles.isEmpty()) {
				future.fail(new NotFoundException());
			}
			if (!Filters.hasProject(roles, projectId)) {
				future.fail(new NotFoundException());
			}
			future.complete();
		}, res -> {
			if (res.failed()) {
				context.fail(res.cause());
				return;
			}
			payload.set(new FindById<>(Project.class, projectId));
			context.next();
		});
	}

	@PUT("/:projectId/")
	@PATCH("/:projectId/")
	@Update
	public Project updateProject(@Param Long projectId, @RequestBody Project project) {
		project.setId(projectId);
		return project;
	}
}

package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;

import java.util.List;
import java.util.stream.Collectors;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.annotations.Update;
import com.github.aesteve.nubes.orm.mongo.MongoNubes;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.nubes.orm.queries.UpdateBy;
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
import com.github.aesteve.vertx.nubes.exceptions.ValidationException;
import com.github.aesteve.vertx.nubes.exceptions.http.impl.BadRequestException;
import com.github.aesteve.vertx.nubes.marshallers.Payload;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.vertx.ext.web.RoutingContext;

@Controller("/api/1/projects")
@ContentType("application/json")
public class ProjectApiController {

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	private MongoService mongo;

	@GET
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void list(RoutingContext context, PaginationContext pageContext, @User ProjuiceUser loggedUser, Payload<List<Project>> payload) {
		mongo.listAndCount(new FindBy<>(Project.class), 0, Integer.MAX_VALUE, AsyncUtils.failOr(context, res -> {
			List<Project> projects = res.result().list;
			payload.set(projects.stream()
					.filter(loggedUser::isMemberOf)
					.collect(Collectors.toList()));
			context.next();
		}));
	}

	@POST
	@Create
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void createProject(RoutingContext context, @RequestBody Project project, Payload<Project> payload, @User ProjuiceUser loggedUser) throws BadRequestException {
		try {
			project.validate();
		} catch(ValidationException ve) {
			throw new BadRequestException(ve);
		}
		project.generateId();
		FindBy<Project> findById = new FindBy<>(Project.class, "id", project.getId());
		mongo.findBy(findById, AsyncUtils.failOr(context, res -> {
			Project proj = res.result();
			if (proj != null) {
				ValidationException ve = new ValidationException("A project with the same name already exists");
				context.fail(new BadRequestException(ve));
				return;
			}
			project.addAdmin(loggedUser);
			payload.set(project);
			context.next();
		}));
	}

	@GET("/:projectId/")
	@RetrieveById
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public FindBy<Project> getProject(@Param String projectId, @User ProjuiceUser currentUser) {
		// TODO : check user access
		return new FindBy<>(Project.class, "id", projectId);
	}

	@PUT("/:projectId/")
	@PATCH("/:projectId/")
	@Update
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public UpdateBy<Project> updateProject(@Param String projectId, @RequestBody Project project) {
		// TODO : check user access
		return new UpdateBy<>(project, "id", projectId);
	}
}

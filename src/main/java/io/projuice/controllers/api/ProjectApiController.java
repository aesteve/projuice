package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveByQuery;
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

import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;

@Controller("/api/1/projects")
@ContentType("application/json")
public class ProjectApiController {

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	private MongoService hibernate;

	@GET
	@RetrieveByQuery
	public FindBy<Project> list(PaginationContext pageContext) {
		return new FindBy<>(Project.class);
	}

	@POST
	@Create
	public Project createProject(@RequestBody Project project) {
		return project;
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

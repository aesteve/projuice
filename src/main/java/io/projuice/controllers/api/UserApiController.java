package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import static io.projuice.auth.ProjuiceAuthProvider.SUPER_USER;
import io.projuice.model.ProjuiceUser;
import io.projuice.model.api.ApiUser;
import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.annotations.RemoveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveByQuery;
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
import com.github.aesteve.vertx.nubes.annotations.routing.http.DELETE;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.context.PaginationContext;
import com.github.aesteve.vertx.nubes.exceptions.http.impl.BadRequestException;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

@Controller("/api/1/users")
@ContentType("application/json")
public class UserApiController {

	@Service(MongoNubes.MONGO_SERVICE_NAME)
	private MongoService mongo;

	// Everyone

	@POST
	@Create
	public UpdateBy<ProjuiceUser> register(@RequestBody ProjuiceUser body) throws BadRequestException {
		if (!body.isValid()) {
			throw new BadRequestException();
		}
		UpdateBy<ProjuiceUser> creation = new UpdateBy<>(body, null, null);
		creation.findBy.setTransform(ProjuiceUser::toApi);
		return creation;
	}

	// Logged users only

	@GET("/me")
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public ApiUser getMyInfos(@User ProjuiceUser me) {
		return me.toApi();
	}

	@GET("/:userId/")
	@RetrieveById
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public FindBy<ProjuiceUser> getUserInfos(RoutingContext context, @Param String username, Payload<ApiUser> payload) {
		FindBy<ProjuiceUser> findBy = new FindBy<ProjuiceUser>(ProjuiceUser.class, "username", username);
		findBy.setTransform(ProjuiceUser::toApi);
		return findBy;
	}

	// admin section

	@GET
	@RetrieveByQuery
	@Auth(method = API_TOKEN, authority = SUPER_USER)
	public FindBy<ProjuiceUser> getAllUsers(PaginationContext pageContext) {
		FindBy<ProjuiceUser> findBy = new FindBy<>(ProjuiceUser.class);
		findBy.setTransform(ProjuiceUser::toApi);
		return findBy;
	}

	@DELETE("/:userId/")
	@RemoveById
	@Auth(method = API_TOKEN, authority = SUPER_USER)
	public FindBy<ProjuiceUser> deleteUser(@Param String userId) {
		return new FindBy<>(ProjuiceUser.class, "id", userId); // no need to transform here, sends 204
	}

}

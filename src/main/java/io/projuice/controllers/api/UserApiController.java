package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import static io.projuice.auth.ProjuiceAuthProvider.SUPER_USER;
import io.projuice.model.ProjuiceUser;

import com.github.aesteve.nubes.orm.annotations.Create;
import com.github.aesteve.nubes.orm.annotations.RemoveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveById;
import com.github.aesteve.nubes.orm.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.nubes.orm.queries.FindById;
import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.auth.Auth;
import com.github.aesteve.vertx.nubes.annotations.auth.User;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.DELETE;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.context.PaginationContext;
import com.github.aesteve.vertx.nubes.exceptions.http.impl.BadRequestException;

@Controller("/api/1/users")
@ContentType("application/json")
public class UserApiController {

	// Everyone

	@POST
	@Create
	public ProjuiceUser register(@RequestBody ProjuiceUser body) throws BadRequestException {
		if (!body.isValid()) {
			throw new BadRequestException();
		}
		return body;
	}

	// Logged users only

	@GET("/me")
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public ProjuiceUser getMyInfos(@User ProjuiceUser me) {
		return me;
	}

	@GET("/:userId/")
	@RetrieveById
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public FindById<ProjuiceUser> getUserInfos(@Param String userId) {
		return new FindById<ProjuiceUser>(ProjuiceUser.class, userId);
	}

	// admin section

	@GET
	@RetrieveByQuery
	@Auth(method = API_TOKEN, authority = SUPER_USER)
	public FindBy<ProjuiceUser> getAllUsers(PaginationContext pageContext) {
		return new FindBy<>(ProjuiceUser.class);
	}

	@DELETE("/:userId/")
	@RemoveById
	@Auth(method = API_TOKEN, authority = SUPER_USER)
	public FindBy<ProjuiceUser> deleteUser(@Param String userId) {
		return new FindBy<>(ProjuiceUser.class, "id", userId);
	}

}

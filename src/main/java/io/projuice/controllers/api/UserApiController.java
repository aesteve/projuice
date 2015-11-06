package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import static io.projuice.auth.ProjuiceAuthProvider.SUPER_USER;
import io.projuice.Server;
import io.projuice.auth.AuthenticationException;
import io.projuice.auth.ProjuiceAuthProvider;
import io.projuice.model.ProjuiceUser;
import io.projuice.services.TokenService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.github.aesteve.nubes.hibernate.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.hibernate.annotations.SessionPerRequest;
import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.auth.Auth;
import com.github.aesteve.vertx.nubes.annotations.auth.User;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.context.PaginationContext;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

@Controller("/api/1/users")
@ContentType("application/json")
public class UserApiController {

	@Service(Server.AUTH_SERVICE)
	ProjuiceAuthProvider authProvider;

	@Service(Server.TOKEN_SERVICE)
	TokenService tokenService;

	@GET
	@RetrieveByQuery
	@Auth(method = API_TOKEN, authority = SUPER_USER)
	public CriteriaQuery<ProjuiceUser> getAllUsers(CriteriaBuilder builder, PaginationContext pageContext) {
		CriteriaQuery<ProjuiceUser> crit = builder.createQuery(ProjuiceUser.class);
		crit.from(ProjuiceUser.class);
		return crit;
	}

	@GET("/me")
	@SessionPerRequest
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public ProjuiceUser getMyInfos(@User ProjuiceUser me) {
		return me;
	}

	@POST("/login")
	public void login(RoutingContext context, @RequestBody JsonObject credentials, Payload<JsonObject> loginInfo) {
		authProvider.authenticateByUsername(credentials, res -> {
			if (res.failed()) {
				Throwable cause = res.cause();
				if (cause instanceof AuthenticationException) {
					context.fail(401);
				} else {
					context.fail(cause);
				}
				return;
			}
			ProjuiceUser user = res.result();
			if (user == null) {
				context.fail(401);
				return;
			}
			context.setUser(user);
			tokenService.getTokenFor(user, tokenRes -> {
				if (tokenRes.failed()) {
					context.fail(tokenRes.cause());
					return;
				}
				JsonObject userInfo = new JsonObject();
				userInfo.put("username", user.getUsername());
				userInfo.put("access_token", tokenRes.result());
				loginInfo.set(userInfo);
				context.next();
			});
		});
	}

	@POST("/logout")
	@SessionPerRequest
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public void logout(@User ProjuiceUser current) {
		authProvider.clearFor(current);
	}
}

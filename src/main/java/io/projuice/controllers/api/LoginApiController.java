package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import io.projuice.Server;
import io.projuice.auth.AuthenticationException;
import io.projuice.auth.ProjuiceAuthProvider;
import io.projuice.model.ProjuiceUser;
import io.projuice.services.TokenService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.auth.Auth;
import com.github.aesteve.vertx.nubes.annotations.auth.Logout;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.marshallers.Payload;

@Controller("/api/1")
@ContentType("application/json")
public class LoginApiController {

	@Service(Server.AUTH_SERVICE)
	ProjuiceAuthProvider authProvider;

	@Service(Server.TOKEN_SERVICE)
	TokenService tokenService;

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
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	@Logout
	public void logout() {}
}

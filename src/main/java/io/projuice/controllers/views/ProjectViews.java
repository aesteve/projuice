package io.projuice.controllers.views;

import io.projuice.Server;
import io.projuice.auth.ProjuiceAuthProvider;
import io.projuice.controllers.api.CheckController;
import io.projuice.model.ProjuiceUser;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.View;
import com.github.aesteve.vertx.nubes.annotations.cookies.CookieValue;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.context.ViewResolver;
import com.google.common.net.HttpHeaders;

@Controller("/projects")
public class ProjectViews extends CheckController {

	@Service(Server.AUTH_SERVICE)
	ProjuiceAuthProvider auth;

	@GET
	@View
	public String listProjects() {
		return "list.hbs";
	}

	@GET("/:projectId")
	@View
	public void viewProject(RoutingContext context, @Param String projectId, @CookieValue String access_token) {
		if (access_token == null) {
			toLogin(context);
			return;
		}
		auth.authenticate(new JsonObject().put("access_token", access_token), res -> {
			if (res.failed()) {
				toLogin(context);
				return;
			}
			ProjuiceUser user = (ProjuiceUser) res.result();
			if (user == null) {
				toLogin(context);
				return;
			}
			checkUserHasAccessToProject(context, user, projectId, access -> {
				ViewResolver.resolve(context, "project.hbs");
				context.next();
			});
		});
	}

	public void toLogin(RoutingContext context) {
		HttpServerResponse response = context.response();
		response.setStatusCode(302);
		response.putHeader(HttpHeaders.LOCATION, "/login");
		response.end();
	}
}

package io.projuice.controllers.views;

import io.vertx.ext.apex.RoutingContext;
import io.vertx.mvc.annotations.Controller;
import io.vertx.mvc.annotations.Path;
import io.vertx.mvc.annotations.View;
import io.vertx.mvc.annotations.params.Param;
import io.vertx.mvc.controllers.impl.ViewController;

@Controller("/projects")
public class ProjectViews extends ViewController {

	@Path("")
	@View("list.hbs")
	public void listProjects(RoutingContext context) {
		context.next();
	}
	
	@Path("/projects/:projectId")
	@View("projects/view.hbs")
	public void viewProject(RoutingContext context, @Param("projectId") Long projectId) {
		context.next();
	}
	
}

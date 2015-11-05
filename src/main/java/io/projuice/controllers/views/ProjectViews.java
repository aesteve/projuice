package io.projuice.controllers.views;

import java.util.Map;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.View;
import com.github.aesteve.vertx.nubes.annotations.params.ContextData;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;

@Controller("/projects")
public class ProjectViews {

	@GET
	@View
	public String listProjects() {
		return "list.hbs";
	}

	@GET("/:projectId")
	@View
	public String viewProject(@ContextData Map<String, Object> contextData, @Param Long projectId) {
		contextData.put("projectId", projectId);
		return "projects/view.hbs";
	}

}

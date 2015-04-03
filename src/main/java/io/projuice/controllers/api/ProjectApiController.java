package io.projuice.controllers.api;

import io.projuice.WebVerticle;
import io.projuice.model.Project;
import io.vertx.ext.apex.RoutingContext;
import io.vertx.hibernate.results.ListAndCount;
import io.vertx.mvc.annotations.Controller;
import io.vertx.mvc.annotations.params.PathParam;
import io.vertx.mvc.annotations.params.RequestBody;
import io.vertx.mvc.annotations.routing.GET;
import io.vertx.mvc.annotations.routing.POST;
import io.vertx.mvc.annotations.routing.PUT;
import io.vertx.mvc.annotations.routing.Path;
import io.vertx.mvc.context.PaginationContext;
import io.vertx.mvc.controllers.impl.JsonApiController;

@Controller("/api/1/projects")
public class ProjectApiController extends JsonApiController {
	
	@Path("")
	public void list(RoutingContext context, PaginationContext pageContext) {
		WebVerticle.hibernateService.withEntityManager(entityManager -> {
			ListAndCount<Project> result = new ListAndCount<Project>(Project.class, entityManager);
			result.queryAndCount(pageContext.firstItemInPage(), pageContext.lastItemInPage());
			return result;
		}, result -> {
			if (result.succeeded()) {
				ListAndCount<Project> listAndCount = result.result();
				pageContext.setNbItems(listAndCount.count());
				setPayload(context, listAndCount.result());
				context.next();
			} else {
				context.fail(result.cause());
			}
		});
	}
	
	@Path("")
	@POST
	public void createProject(RoutingContext context, @RequestBody Project project) {
		WebVerticle.hibernateService.withinTransaction(entityManager -> {
			entityManager.persist(project);
			return project;
		}, result -> {
			resultAsPayload(context, result);
		});		
	}
	
	@Path("/:projectId")
	@GET
	public void getProject(RoutingContext context, @PathParam("projectId") Long projectId) {
		WebVerticle.hibernateService.withEntityManager(entityManager -> {
			return entityManager.find(Project.class, projectId);
		}, result -> {
			resultAsPayload(context, result);
		});
	}
	
	@Path("/:projectId")
	@PUT
	public void updateProject(RoutingContext context, @PathParam("projectId") Long projectId, @RequestBody Project project) {
		if (project.getId() == null) {
			project.setId(projectId);
		}
		WebVerticle.hibernateService.withinTransaction(entityManager -> {
			entityManager.merge(project);
			return project;
		}, result -> {
			resultAsPayload(context, result);
		});
	}
}

package io.projuice.controllers.api;

import io.projuice.WebVerticle;
import io.projuice.model.Project;
import io.vertx.ext.apex.RoutingContext;
import io.vertx.hibernate.HibernateService;
import io.vertx.mvc.annotations.Controller;
import io.vertx.mvc.annotations.Path;
import io.vertx.mvc.annotations.params.Param;
import io.vertx.mvc.context.PaginationContext;
import io.vertx.mvc.controllers.impl.JsonApiController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Controller("/api/1/projects")
public class ProjectApiController extends JsonApiController {
	
	@Path("")
	public void list(RoutingContext context, PaginationContext pageContext) {
		HibernateService service = WebVerticle.hibernateService;
		service.createSession(sessionHandler -> {
			if (sessionHandler.failed()) {
				context.fail(sessionHandler.cause());
			} else {
				String sessionId = sessionHandler.result();
				CriteriaBuilder builder = service.getCriteriaBuilder(sessionId);
				CriteriaQuery<Project> query = builder.createQuery(Project.class);
				query.from(Project.class);
				service.list(sessionId, query, pageContext.firstItemInPage(), pageContext.lastItemInPage(), queryHandler -> {
					if (queryHandler.failed()) {
						context.fail(queryHandler.cause());
					} else {
						CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
						countQuery.select(builder.count(countQuery.from(Project.class)));
						service.singleResult(sessionId, countQuery, countHandler -> {
							if (countHandler.failed()) {
								context.fail(countHandler.cause());
							} else {
								pageContext.setNbItems(countHandler.result());
								setPayload(context, queryHandler.result());
								context.next();
							}
						});
					}
				});
			}
		});
	}
	
	@Path("/:projectId")
	public void getProject(RoutingContext context, @Param("projectId") Long projectId) {
		context.next();
	}
}

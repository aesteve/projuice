package io.projuice.controllers.api;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.annotations.Create;
import com.github.aesteve.nubes.hibernate.annotations.RetrieveById;
import com.github.aesteve.nubes.hibernate.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.hibernate.annotations.Update;
import com.github.aesteve.nubes.hibernate.queries.FindById;
import com.github.aesteve.nubes.hibernate.services.HibernateService;
import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.params.RequestBody;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.annotations.routing.http.POST;
import com.github.aesteve.vertx.nubes.annotations.routing.http.PUT;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.context.PaginationContext;

import io.projuice.model.Project;

@Controller("/api/1/projects")
@ContentType("application/json")
public class ProjectApiController {

	@Service(HibernateNubes.HIBERNATE_SERVICE_NAME)
	private HibernateService hibernate;

	@GET
	@RetrieveByQuery
	public CriteriaQuery<Project> list(PaginationContext pageContext, CriteriaBuilder builder) {
		return builder.createQuery(Project.class);
	}

	@POST
	@Create
	public Project createProject(@RequestBody Project project) {
		return project;
	}

	@GET("/:projectId/")
	@RetrieveById
	public FindById<Project> getProject(@Param Long projectId) {
		return new FindById<>(Project.class, projectId);
	}

	@PUT("/:projectId")
	@Update
	public Project updateProject(@Param Long projectId, @RequestBody Project project) {
		project.setId(projectId);
		return project;
	}
}

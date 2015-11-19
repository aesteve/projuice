package io.projuice.handlers;

import io.projuice.annotations.ProjectRoleCheck;

import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.vertx.nubes.handlers.AnnotationProcessor;
import com.github.aesteve.vertx.nubes.reflections.factories.AnnotationProcessorFactory;

public class ProjectRoleCheckProcessorFactory implements AnnotationProcessorFactory<ProjectRoleCheck> {

	private MongoService mongo;

	public ProjectRoleCheckProcessorFactory(MongoService mongo) {
		this.mongo = mongo;
	}

	@Override
	public AnnotationProcessor<ProjectRoleCheck> create(ProjectRoleCheck annotation) {
		return new ProjectRoleCheckProcessor(mongo, annotation.value());
	}

}

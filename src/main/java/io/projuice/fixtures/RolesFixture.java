package io.projuice.fixtures;

import io.projuice.model.Project;
import io.projuice.model.ProjuiceUser;
import io.projuice.model.Role;
import io.projuice.model.UserRoleInProject;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.queries.FindBy;
import com.github.aesteve.nubes.hibernate.services.HibernateService;
import com.github.aesteve.vertx.nubes.annotations.services.Service;
import com.github.aesteve.vertx.nubes.fixtures.Fixture;
import com.github.aesteve.vertx.nubes.utils.async.AsyncUtils;

public class RolesFixture extends Fixture {

	@Service(HibernateNubes.HIBERNATE_SERVICE_NAME)
	private HibernateService hibernate;

	@Override
	public int executionOrder() {
		return 2;
	}

	@Override
	public void startUp(Vertx vertx, Future<Void> future) {
		hibernate.withinTransactionDo((em, fut) -> {
			hibernate.findBy(em, new FindBy<ProjuiceUser>(ProjuiceUser.class, "username", "Arnaud"), res -> {
				ProjuiceUser arnaud = res.result();
				hibernate.findBy(em, new FindBy<Project>(Project.class, "name", "Projuice"), res2 -> {
					Project projuice = res2.result();
					UserRoleInProject role = new UserRoleInProject();
					role.setProject(projuice);
					role.setUser(arnaud);
					role.setRole(Role.ADMIN);
					em.persist(role);
					fut.complete();
				});
			});
		}, AsyncUtils.ignoreResult(future));
	}

	@Override
	public void tearDown(Vertx vertx, Future<Void> future) {
		future.complete();
	}

}

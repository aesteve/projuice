package integration.projects;

import org.junit.Test;

import integration.ProjuiceTestBase;
import io.projuice.fixtures.ProjectsFixture;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

public class ProjectSpec extends ProjuiceTestBase {

	@Test
	public void getProjectIBelongTo(TestContext context) {
		Async async = context.async();
		withAdminTokenDo(context, token -> {
			getJSON(token, "/api/1/projects/" + ProjectsFixture.ProjuiceID + "/", response -> {
				context.assertEquals(200, response.statusCode());
				async.complete();
			});
		});
	}
}

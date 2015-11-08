package integration.projects;

import org.junit.Test;

import integration.ProjuiceTestBase;
import io.projuice.fixtures.ProjectsFixture;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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

	@Test
	public void getProjectsIBelongTo(TestContext context) {
		Async async = context.async();
		withAdminTokenDo(context, token -> {
			getJSON(token, "/api/1/projects", response -> {
				context.assertEquals(200, response.statusCode());
				response.bodyHandler(buff -> {
					JsonArray result = new JsonArray(buff.toString("UTF-8"));
					context.assertEquals(1, result.size());
					async.complete();
				});
			});
		});
	}
	

	@Test
	public void getNoProjects(TestContext context) {
		Async async = context.async();
		withStandardTokenDo(context, token -> {
			getJSON(token, "/api/1/projects", response -> {
				context.assertEquals(200, response.statusCode());
				response.bodyHandler(buff -> {
					JsonArray result = new JsonArray(buff.toString("UTF-8"));
					context.assertEquals(0, result.size());
					async.complete();
				});
			});
		});
	}
	
	@Test
	public void createProjectUnauth(TestContext context) {
		Async async = context.async();
		JsonObject project = new JsonObject();
		project.put("name", "Something");
		postJSON(null, "/api/1/projects", project, response -> {
			context.assertEquals(401, response.statusCode());
			async.complete();
		});
	}
	
	@Test
	public void createProjectWithoutName(TestContext context) {
		Async async = context.async();
		JsonObject project = new JsonObject();
		withAdminTokenDo(context, token -> {
			postJSON(token, "/api/1/projects", project, response -> {
				context.assertEquals(400, response.statusCode());
				async.complete();
			});
		});
	}
	
	@Test
	public void createProjectWithExistingName(TestContext context) {
		Async async = context.async();
		JsonObject project = new JsonObject();
		project.put("name", "Projuice");
		withAdminTokenDo(context, token -> {
			postJSON(token, "/api/1/projects", project, response -> {
				context.assertEquals(400, response.statusCode());
				async.complete();
			});
		});
	}
	
	@Test
	public void createProjectSuccess(TestContext context) {
		String name = "Projuice2";
		Async async = context.async();
		JsonObject project = new JsonObject();
		project.put("name", name);
		withAdminTokenDo(context, token -> {
			postJSON(token, "/api/1/projects", project, response -> {
				context.assertEquals(200, response.statusCode());
				response.bodyHandler(buff -> {
					JsonObject createdProject = new JsonObject(buff.toString("UTF-8"));
					context.assertEquals(name, createdProject.getString("name"));
					context.assertNotNull(createdProject.getString("id"));
					JsonArray participants = createdProject.getJsonArray("participants");
					context.assertEquals(1, participants.size());
					async.complete();
				});
			});
		});
	}
}

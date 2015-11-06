package integration.user;

import org.junit.Test;

import integration.ProjuiceTestBase;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

public class UserSpec extends ProjuiceTestBase {

	private static final String username = "someone";
	private static final String password = "secred";
	private static final String address = "someone@somewhere.com";

	@Test
	public void registerNoEmail(TestContext context) {
		Async async = context.async();
		JsonObject newUser = new JsonObject();
		newUser.put("username", username);
		newUser.put("password", password);
		postJSON(null, "/api/1/users", newUser, response -> {
			context.assertEquals(400, response.statusCode());
			async.complete();
		});
	}

	@Test
	public void registerNoUsername(TestContext context) {
		Async async = context.async();
		JsonObject newUser = new JsonObject();
		newUser.put("emailAddress", address);
		newUser.put("password", password);
		postJSON(null, "/api/1/users", newUser, response -> {
			context.assertEquals(400, response.statusCode());
			async.complete();
		});
	}

	@Test
	public void registerNoPassword(TestContext context) {
		Async async = context.async();
		JsonObject newUser = new JsonObject();
		newUser.put("emailAddress", "someone@somewhere.com");
		newUser.put("username", password);
		postJSON(null, "/api/1/users", newUser, response -> {
			context.assertEquals(400, response.statusCode());
			async.complete();
		});
	}

	@Test
	public void registerValidThenLogin(TestContext context) {
		Async async = context.async();
		JsonObject newUser = new JsonObject();
		newUser.put("emailAddress", "someone@somewhere.com");
		newUser.put("username", username);
		newUser.put("password", password);
		postJSON(null, "/api/1/users", newUser, response -> {
			context.assertEquals(200, response.statusCode());
			response.bodyHandler(buff -> {
				JsonObject json = new JsonObject(buff.toString("UTF-8"));
				Long id = json.getLong("id");
				context.assertNotNull(id);
				assertLoginSuccess(username, password, context, async);
			});
		});
	}

	@Test
	public void test404(TestContext context) {
		Async async = context.async();
		withAdminTokenDo(context, token -> {
			getJSON(token, "/api/1/users/123456789", response -> {
				context.assertEquals(404, response.statusCode());
				async.complete();
			});
		});
	}

}

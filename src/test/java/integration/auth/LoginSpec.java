package integration.auth;

import org.junit.Test;

import integration.ProjuiceTestBase;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

public class LoginSpec extends ProjuiceTestBase {

	@Test
	public void testLogin(TestContext context) {
		String username = "Arnaud";
		Async async = context.async();
		JsonObject loginData = new JsonObject();
		loginData.put("username", username);
		loginData.put("password", "secret");
		postJSON(null, "/api/1/login", loginData, response -> {
			context.assertEquals(200, response.statusCode());
			response.bodyHandler(buff -> {
				JsonObject loginInfo = new JsonObject(buff.toString("UTF-8"));
				String accessToken = loginInfo.getString("access_token");
				context.assertNotNull(accessToken);
				getJSON(accessToken, "/api/1/users/me", myResponse -> {
					context.assertEquals(200, myResponse.statusCode());
					myResponse.bodyHandler(myBuff -> {
						JsonObject userInfo = new JsonObject(myBuff.toString("UTF-8"));
						context.assertEquals(username, userInfo.getString("username"));
						async.complete();
					});
				});
			});
		});
	}

	@Test
	public void testLogout(TestContext context) {
		String username = "Arnaud";
		Async async = context.async();
		JsonObject loginData = new JsonObject();
		loginData.put("username", username);
		loginData.put("password", "secret");
		postJSON(null, "/api/1/login", loginData, response -> {
			context.assertEquals(200, response.statusCode());
			response.bodyHandler(buff -> {
				JsonObject loginInfo = new JsonObject(buff.toString("UTF-8"));
				String accessToken = loginInfo.getString("access_token");
				context.assertNotNull(accessToken);
				postJSON(accessToken, "/api/1/logout", new JsonObject(), logoutResponse -> {
					context.assertEquals(204, logoutResponse.statusCode());
					getJSON(accessToken, "/api/1/users/me", myResponse -> {
						context.assertEquals(401, myResponse.statusCode());
						async.complete();
					});
				});
			});
		});
	}

}

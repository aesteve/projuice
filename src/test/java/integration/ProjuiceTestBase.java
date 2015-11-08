package integration;

import io.projuice.Server;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public abstract class ProjuiceTestBase {

	public static final int MONGO_PORT = 27017;
	public static final String DB = "projuice-test";
	
	protected Vertx vertx;
	protected String deploymentId;

	@Before
	public void setUp(TestContext context) {
		Async async = context.async();
		vertx = Vertx.vertx();
		vertx.deployVerticle(Server.class.getName(), testOptions(), res -> {
			deploymentId = res.result();
			async.complete();
		});
	}

	@After
	public void tearDown(TestContext context) {
		Async async = context.async();
		vertx.undeploy(deploymentId, res -> {
			System.out.println("Close Vert.x");
			vertx.close(closeRes -> async.complete());
		});
	}

	private static DeploymentOptions testOptions() {
		JsonObject conf = new JsonObject();
		conf.put("src-package", "io.projuice");
		conf.put("templates", new JsonArray().add("hbs"));
		conf.put("mongo", new JsonObject().put("host", "localhost").put("port", MONGO_PORT).put("db_name", DB));
		DeploymentOptions testOptions = new DeploymentOptions();
		testOptions.setInstances(1);
		testOptions.setConfig(conf);
		return testOptions;
	}

	protected void postJSON(String accessToken, String uri, JsonObject data, Handler<HttpClientResponse> responseHandler) {
		HttpClientRequest req = client()
				.post(uri, responseHandler)
				.putHeader(HttpHeaders.ACCEPT, "application/json")
				.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		if (accessToken != null) {
			req.putHeader(HttpHeaders.AUTHORIZATION, "token " + accessToken);
		}
		req.end(data.toString());
	}

	protected void getJSON(String accessToken, String uri, Handler<HttpClientResponse> responseHandler) {
		HttpClientRequest req = client()
				.get(uri, responseHandler)
				.putHeader(HttpHeaders.ACCEPT, "application/json")
				.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		if (accessToken != null) {
			req.putHeader(HttpHeaders.AUTHORIZATION, "token " + accessToken);
		}
		req.end();
	}

	protected void login(String username, String password, Handler<HttpClientResponse> handler) {
		JsonObject credentials = new JsonObject();
		credentials.put("username", username);
		credentials.put("password", password);
		postJSON(null, "/api/1/login", credentials, handler);
	}

	protected void assertLoginSuccess(String username, String password, TestContext context, Async async) {
		login(username, password, response -> {
			context.assertEquals(200, response.statusCode());
			response.bodyHandler(buff -> {
				JsonObject loginInfos = new JsonObject(buff.toString("UTF-8"));
				context.assertNotNull(loginInfos.getString("access_token"));
				async.complete();
			});
		});
	}

	protected void withAdminTokenDo(TestContext context, Handler<String> theTest) {
		login("Arnaud", "secret", response -> {
			context.assertEquals(200, response.statusCode());
			response.bodyHandler(buff -> {
				JsonObject cred = new JsonObject(buff.toString("UTF-8"));
				theTest.handle(cred.getString("access_token"));
			});
		});
	}

	protected HttpClient client() {
		HttpClientOptions options = new HttpClientOptions();
		options.setDefaultHost("localhost");
		options.setDefaultPort(9000);
		return vertx.createHttpClient(options);
	}

}

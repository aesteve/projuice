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
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ProjuiceTestBase {

	protected Vertx vertx;

	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		vertx.deployVerticle(Server.class.getName(), testOptions(), context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	private static DeploymentOptions testOptions() {
		JsonObject conf = new JsonObject();
		conf.put("src-package", "io.projuice");
		conf.put("templates", new JsonArray().add("hbs"));
		conf.put("persistence-unit", "projuice-dev");
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

	protected HttpClient client() {
		HttpClientOptions options = new HttpClientOptions();
		options.setDefaultHost("localhost");
		options.setDefaultPort(9000);
		return vertx.createHttpClient(options);
	}

}

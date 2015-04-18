package io.projuice;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.apex.Router;
import io.vertx.hibernate.HibernateService;
import io.vertx.nubes.VertxNubes;
import io.vertx.nubes.exceptions.MissingConfigurationException;

public class WebVerticle implements Verticle {

	private Vertx vertx;
	private JsonObject config;
	private VertxNubes vertxMVC;
	public static HibernateService hibernateService;
	
	
	@Override
	public void start(Future<Void> startFuture) {
		try {
			this.vertxMVC = new VertxNubes(vertx, config);
			hibernateService = new HibernateService(vertx, config);
		} catch (MissingConfigurationException mce) {
			startFuture.fail(mce);
			return;
		}
		HttpServer server = createHttpServer();
		Future<Void> hibernateFuture = Future.future();
		hibernateFuture.setHandler(hibernateResult -> {
			if (hibernateResult.failed()) {
				startFuture.fail(hibernateResult.cause());
			} else {
				Future<Router> mvcFuture = Future.future();
				mvcFuture.setHandler(mvcResult -> {
					if (mvcResult.succeeded()) {
						server.requestHandler(mvcResult.result()::accept);
						server.listen();
						startFuture.complete();
					} else {
						startFuture.fail(mvcResult.cause());
					}
				});
				vertxMVC.bootstrap(mvcFuture);
			}
		});
		hibernateService.start(hibernateFuture);
	}
	
	private HttpServer createHttpServer() {
		HttpServerOptions options = new HttpServerOptions();
		options.setHost(config.getString("host", "localhost"));
		options.setPort(config.getInteger("port", 9090));
		return vertx.createHttpServer(options);
	}
	
	@Override
	public void stop(Future<Void> stopFuture) {
		vertxMVC.stop(stopFuture);
	}

	@Override
	public Vertx getVertx() {
		return vertx;
	}

	@Override
	public void init(Vertx vertx, Context context) {
		this.vertx = vertx;
		this.config = context.config();
		// TODO : why isn't it working with conf.json ?
		JsonArray controllers = new JsonArray();
		JsonArray fixtures = new JsonArray();
		controllers.add("io.projuice.controllers");
		fixtures.add("io.projuice.fixtures");
		config.put("controller-packages", controllers);
		config.put("fixture-packages", fixtures);
		config.put("persistence-unit", "projuice-dev");
	}
}

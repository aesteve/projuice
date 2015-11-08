package io.projuice;

import com.github.aesteve.nubes.orm.mongo.MongoNubes;
import com.github.aesteve.nubes.orm.mongo.MongoNubesServer;
import com.github.aesteve.nubes.orm.mongo.services.MongoService;

import io.projuice.auth.ProjuiceAuthProvider;
import io.projuice.services.TokenService;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

public class Server extends MongoNubesServer {

	public static final String TOKEN_SERVICE = "tokens";
	public static final String AUTH_SERVICE = "auth-service";

	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		MongoService mongo = (MongoService) nubes.getService(MongoNubes.MONGO_SERVICE_NAME);
		TokenService tokens = TokenService.create();
		ProjuiceAuthProvider provider = new ProjuiceAuthProvider(mongo, tokens);
		nubes.registerService(TOKEN_SERVICE, tokens);
		nubes.registerService(AUTH_SERVICE, provider);
		nubes.setAuthProvider(provider);
	}
}

package io.projuice;

import io.projuice.auth.ProjuiceAuthProvider;
import io.projuice.services.TokenService;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.HibernateNubesServer;
import com.github.aesteve.nubes.hibernate.services.HibernateService;

public class Server extends HibernateNubesServer {

	public static final String TOKEN_SERVICE = "tokens";
	public static final String AUTH_SERVICE = "auth-service";

	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		HibernateService hibernate = (HibernateService) nubes.getService(HibernateNubes.HIBERNATE_SERVICE_NAME);
		TokenService tokens = TokenService.create();
		ProjuiceAuthProvider provider = new ProjuiceAuthProvider(hibernate, tokens);
		nubes.registerService(TOKEN_SERVICE, tokens);
		nubes.registerService(AUTH_SERVICE, provider);
		nubes.setAuthProvider(provider);
	}
}

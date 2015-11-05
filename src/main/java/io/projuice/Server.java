package io.projuice;

import io.projuice.auth.ProjuiceAuthProvider;
import io.projuice.services.TokenService;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

import com.github.aesteve.nubes.hibernate.HibernateNubes;
import com.github.aesteve.nubes.hibernate.HibernateNubesServer;
import com.github.aesteve.nubes.hibernate.services.HibernateService;

public class Server extends HibernateNubesServer {
	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
		HibernateService hibernate = (HibernateService) nubes.getService(HibernateNubes.HIBERNATE_SERVICE_NAME);
		System.out.println("hibernate ? " + hibernate);
		nubes.setAuthProvider(new ProjuiceAuthProvider(hibernate, TokenService.create()));
	}
}

package io.projuice.controllers.views;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.View;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;

@Controller("/")
public class PublicViews {

	@GET("login")
	@View("login.hbs")
	public void login() {}

	@GET("register")
	@View("register.hbs")
	public void register() {}
}

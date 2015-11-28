package io.projuice.controllers.views;

import io.vertx.core.http.HttpServerResponse;

import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;

@Controller("/")
public class PublicViews {

	@GET
	public void index(HttpServerResponse response) {
		response.sendFile("web/views/index.html");
	}

}

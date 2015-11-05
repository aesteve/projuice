package io.projuice.controllers.api;

import io.projuice.model.ProjuiceUser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.github.aesteve.nubes.hibernate.annotations.RetrieveByQuery;
import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.auth.Auth;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.auth.AuthMethod;
import com.github.aesteve.vertx.nubes.context.PaginationContext;

@Controller("/api/1/users")
@ContentType("application/json")
public class UserApiController {

	@GET
	@RetrieveByQuery
	@Auth(method = AuthMethod.REDIRECT, authority = "superuser", redirectURL = "/login")
	public CriteriaQuery<ProjuiceUser> getAllUsers(CriteriaBuilder builder, PaginationContext pageContext) {
		CriteriaQuery<ProjuiceUser> crit = builder.createQuery(ProjuiceUser.class);
		crit.from(ProjuiceUser.class);
		return crit;
	}
}

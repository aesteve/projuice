package io.projuice.controllers.api;

import static com.github.aesteve.vertx.nubes.auth.AuthMethod.API_TOKEN;
import static io.projuice.auth.ProjuiceAuthProvider.LOGGED_IN;
import static io.projuice.auth.ProjuiceAuthProvider.SUPER_USER;
import io.projuice.model.ProjuiceUser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.github.aesteve.nubes.hibernate.annotations.RetrieveById;
import com.github.aesteve.nubes.hibernate.annotations.RetrieveByQuery;
import com.github.aesteve.nubes.hibernate.annotations.SessionPerRequest;
import com.github.aesteve.nubes.hibernate.queries.FindById;
import com.github.aesteve.vertx.nubes.annotations.Controller;
import com.github.aesteve.vertx.nubes.annotations.auth.Auth;
import com.github.aesteve.vertx.nubes.annotations.auth.User;
import com.github.aesteve.vertx.nubes.annotations.mixins.ContentType;
import com.github.aesteve.vertx.nubes.annotations.params.Param;
import com.github.aesteve.vertx.nubes.annotations.routing.http.GET;
import com.github.aesteve.vertx.nubes.context.PaginationContext;

@Controller("/api/1/users")
@ContentType("application/json")
public class UserApiController {

	@GET
	@RetrieveByQuery
	@Auth(method = API_TOKEN, authority = SUPER_USER)
	public CriteriaQuery<ProjuiceUser> getAllUsers(CriteriaBuilder builder, PaginationContext pageContext) {
		CriteriaQuery<ProjuiceUser> crit = builder.createQuery(ProjuiceUser.class);
		crit.from(ProjuiceUser.class);
		return crit;
	}

	@GET("/me")
	@SessionPerRequest
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public ProjuiceUser getMyInfos(@User ProjuiceUser me) {
		return me;
	}

	@GET("/:userId/")
	@RetrieveById
	@Auth(method = API_TOKEN, authority = LOGGED_IN)
	public FindById<ProjuiceUser> getUserInfos(@Param Long userId) {
		return new FindById<ProjuiceUser>(ProjuiceUser.class, userId);
	}
}

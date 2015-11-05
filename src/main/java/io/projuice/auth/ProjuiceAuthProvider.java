package io.projuice.auth;

import io.projuice.model.ProjuiceUser;
import io.projuice.services.TokenService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

import com.github.aesteve.nubes.hibernate.queries.FindById;
import com.github.aesteve.nubes.hibernate.services.HibernateService;

public class ProjuiceAuthProvider implements AuthProvider {

	protected final static Logger log = LoggerFactory.getLogger(ProjuiceAuthProvider.class);

	private final HibernateService hibernate;
	private final TokenService tokenService;

	public ProjuiceAuthProvider(HibernateService hibernate, TokenService tokenService) {
		this.hibernate = hibernate;
		this.tokenService = tokenService;
	}

	@Override
	public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
		String token = authInfo.getString("token");
		if (token != null) {
			authenticateByToken(token, resultHandler);
			return;
		}
		String username = authInfo.getString("username");
		String pwd = authInfo.getString("password");
		if (username == null || pwd == null) {
			resultHandler.handle(Future.failedFuture("No username or password specified"));
			return;
		}
		authenticateByUsername(username, pwd, resultHandler);
	}

	public void clearFor(ProjuiceUser user) {
		tokenService.clearFor(user);
	}

	/**
	 * FIXME / TODO : describe business rules (who can see ?)
	 */
	public void isAuthorised(User user, String authority, Handler<AsyncResult<Boolean>> resultHandler) {
		log.info("is Authorised called");
		resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
	}

	private void authenticateByToken(String token, Handler<AsyncResult<User>> resultHandler) {
		tokenService.getUserAssociatedWith(token, resultHandler);
	}

	private void authenticateByUsername(String username, String pwd, Handler<AsyncResult<User>> resultHandler) {
		hibernate.withEntityManager((em, future) -> {
			hibernate.findById(em, new FindById<>(ProjuiceUser.class, username), userResult -> {
				if (userResult.failed()) {
					future.fail(userResult.cause());
					return;
				}
				ProjuiceUser user = userResult.result();
				if (user == null) {
					future.fail(new AuthenticationException());
					return;
				}
				if (pwd.equals(user.getPassword())) {
					future.fail(new AuthenticationException());
					return;
				}
				tokenService.createTokenFor(user, token -> {
					future.complete(user);
				});
			});
		}, resultHandler);
	}
}

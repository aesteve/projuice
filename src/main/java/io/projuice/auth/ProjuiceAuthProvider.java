package io.projuice.auth;

import io.projuice.model.ProjuiceUser;
import io.projuice.services.TokenService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

import com.github.aesteve.nubes.hibernate.queries.FindBy;
import com.github.aesteve.nubes.hibernate.services.HibernateService;
import com.github.aesteve.vertx.nubes.services.Service;

public class ProjuiceAuthProvider implements AuthProvider, Service {

	public final static String SUPER_USER = "SUPER_USER";
	public final static String LOGGED_IN = "LOGGED_IN";

	protected final static Logger log = LoggerFactory.getLogger(ProjuiceAuthProvider.class);

	private final HibernateService hibernate;
	private final TokenService tokenService;

	public ProjuiceAuthProvider(HibernateService hibernate, TokenService tokenService) {
		this.hibernate = hibernate;
		this.tokenService = tokenService;
	}

	@Override
	public void init(Vertx vertx, JsonObject json) {}

	@Override
	public void start(Future<Void> future) {
		future.complete();
	}

	@Override
	public void stop(Future<Void> future) {
		future.complete();
	}

	@Override
	public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
		String token = authInfo.getString("access_token");
		if (token != null) {
			authenticateByToken(token, resultHandler);
			return;
		}
	}

	public void clearFor(ProjuiceUser user) {
		tokenService.clearFor(user);
	}

	/**
	 * FIXME / TODO : describe business rules (who can see ?)
	 */
	public void isAuthorised(User user, String authority, Handler<AsyncResult<Boolean>> resultHandler) {
		log.info("is Authorised called " + authority);
		resultHandler.handle(Future.succeededFuture(Boolean.TRUE));
	}

	private void authenticateByToken(String token, Handler<AsyncResult<User>> resultHandler) {
		tokenService.getUserAssociatedWith(token, resultHandler);
	}

	public void authenticateByUsername(JsonObject authInfo, Handler<AsyncResult<ProjuiceUser>> resultHandler) {
		String username = authInfo.getString("username");
		String pwd = authInfo.getString("password");
		if (username == null || pwd == null) {
			resultHandler.handle(Future.failedFuture(new AuthenticationException()));
			return;
		}
		hibernate.withEntityManager((em, future) -> {
			hibernate.findBy(em, new FindBy<>(ProjuiceUser.class, "username", username), userResult -> {
				System.out.println("FindUserByUsername : " + username);
				System.out.println(userResult.failed());
				if (userResult.failed()) {
					future.fail(userResult.cause());
					return;
				}
				ProjuiceUser user = userResult.result();
				System.out.println(user);
				if (user == null) {
					future.fail(new AuthenticationException());
					return;
				}
				System.out.println(user.getPassword());
				System.out.println(pwd.equals(user.getPassword()));
				if (!pwd.equals(user.getPassword())) {
					future.fail(new AuthenticationException());
					return;
				}
				tokenService.createTokenFor(user, token -> {
					System.out.println("create token for : ");
					System.out.println(user);
					future.complete(user);
				});
			});
		}, res -> {
			if (res.failed()) {
				System.out.println("with entitiy manager faield");
				resultHandler.handle(Future.failedFuture(res.cause()));
			} else {
				System.out.println("with entitiy manager result");
				System.out.println(res.result());
				resultHandler.handle(Future.succeededFuture((ProjuiceUser) res.result()));
			}
		});
	}

}

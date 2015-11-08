package io.projuice.auth;

import com.github.aesteve.nubes.orm.mongo.services.MongoService;
import com.github.aesteve.nubes.orm.queries.FindBy;
import com.github.aesteve.vertx.nubes.services.Service;

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

public class ProjuiceAuthProvider implements AuthProvider, Service {

	public final static String SUPER_USER = "SUPER_USER";
	public final static String LOGGED_IN = "LOGGED_IN";

	protected final static Logger log = LoggerFactory.getLogger(ProjuiceAuthProvider.class);

	private final MongoService mongo;
	private final TokenService tokenService;

	public ProjuiceAuthProvider(MongoService mongo, TokenService tokenService) {
		this.mongo = mongo;
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
		mongo.findBy(new FindBy<>(ProjuiceUser.class, "username", username), userResult -> {
			if (userResult.failed()) {
				resultHandler.handle(Future.failedFuture(userResult.cause()));
				return;
			}
			ProjuiceUser user = userResult.result();
			if (user == null) {
				resultHandler.handle(Future.failedFuture(new AuthenticationException()));
				return;
			}
			if (!pwd.equals(user.getPassword())) {
				resultHandler.handle(Future.failedFuture(new AuthenticationException()));
				return;
			}
			tokenService.createTokenFor(user, token -> {
				resultHandler.handle(Future.succeededFuture(user));
			});
		});
	}

}

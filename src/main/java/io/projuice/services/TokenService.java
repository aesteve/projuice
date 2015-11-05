package io.projuice.services;

import io.projuice.model.ProjuiceUser;
import io.projuice.model.auth.AccessToken;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;

import java.util.function.Predicate;

import com.github.aesteve.vertx.nubes.services.Service;

public interface TokenService extends Service {

	public void getTokenFor(ProjuiceUser user, Handler<AsyncResult<String>> handler);

	public void getUserAssociatedWith(String token, Handler<AsyncResult<User>> handler);

	public void createTokenFor(ProjuiceUser user, Handler<AsyncResult<String>> handler);

	public void clearFor(ProjuiceUser user);

	public static Predicate<AccessToken> validTokens = token -> {
		return !token.hasExpired();
	};

	public static TokenService create() {
		return new MapTokenService();
	}
}

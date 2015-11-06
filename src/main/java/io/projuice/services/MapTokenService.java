package io.projuice.services;

import io.projuice.model.ProjuiceUser;
import io.projuice.model.auth.AccessToken;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * Cache for tokens, to allow fast API access
 * Simple in-memory map for now
 * Redis implementation ?
 * Mongo if persistent storage needed ?
 */
public class MapTokenService implements TokenService {

	private Map<String, ProjuiceUser> userForToken;
	private Map<ProjuiceUser, List<AccessToken>> tokensPerUser;

	@Override
	public void init(Vertx vertx, JsonObject conf) {}

	@Override
	public void start(Future<Void> future) {
		userForToken = new HashMap<>();
		tokensPerUser = new HashMap<>();
		future.complete();
	}

	@Override
	public void stop(Future<Void> future) {
		userForToken.clear();
		tokensPerUser.clear();
		future.complete();
	}

	@Override
	public void getTokenFor(ProjuiceUser user, Handler<AsyncResult<String>> handler) {
		List<AccessToken> tokens = tokensPerUser.get(user);
		if (tokens == null) {
			handler.handle(Future.failedFuture("No token found"));
			return;
		}
		tokens = tokens.stream().filter(validTokens).collect(Collectors.toList());
		if (tokens.isEmpty()) {
			handler.handle(Future.failedFuture("No token found"));
			return;
		}
		handler.handle(Future.succeededFuture(tokens.get(0).token));
	}

	@Override
	public void createTokenFor(ProjuiceUser user, Handler<AsyncResult<String>> handler) {
		String token = generateTokenFor(user);
		List<AccessToken> tokens = tokensPerUser.get(user);
		if (tokens == null) {
			tokens = new ArrayList<>(1);
			tokensPerUser.put(user, tokens);
		}
		AccessToken accessToken = new AccessToken(token);
		tokens.add(accessToken);
		userForToken.put(accessToken.token, user);
		handler.handle(Future.succeededFuture(accessToken.token));
	}

	@Override
	public void getUserAssociatedWith(String token, Handler<AsyncResult<User>> handler) {
		handler.handle(Future.succeededFuture(userForToken.get(token)));
	}

	@Override
	public void clearFor(ProjuiceUser user) {
		List<AccessToken> tokens = tokensPerUser.get(user);
		if (tokens == null || tokens.isEmpty()) {
			return;
		}
		tokens.forEach(userForToken::remove);
		tokensPerUser.remove(user);
	}

	private static String generateTokenFor(ProjuiceUser user) {
		String input = user.getEmailAddress() + user.getPassword() + new Date().getTime();
		return Hashing.sha256()
				.hashString(input, Charsets.UTF_8)
				.toString();
	}
}

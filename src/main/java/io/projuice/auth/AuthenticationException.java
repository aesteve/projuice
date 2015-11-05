package io.projuice.auth;

public class AuthenticationException extends Throwable {

	private static final long serialVersionUID = 3233255151210293890L;

	public AuthenticationException() {
		super("Wrong username / password");
	}
}

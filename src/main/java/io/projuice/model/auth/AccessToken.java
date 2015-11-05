package io.projuice.model.auth;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AccessToken {

	public static Long TOKEN_TTL = 24 * 3600 * 1000l; // One day (short access tokens)

	public String token;
	public Date expirationDate;

	public AccessToken(String token) {
		this.token = token;
		expirationDate = new Date(new Date().getTime() + TOKEN_TTL);
	}

	public boolean hasExpired() {
		return expirationDate.after(new Date());
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(token)
				.append(expirationDate)
				.build();
	}
}

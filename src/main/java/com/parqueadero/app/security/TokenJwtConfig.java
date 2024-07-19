package com.parqueadero.app.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {

    private TokenJwtConfig() {
        throw new IllegalStateException("Utility class");
    }

    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String BEARER = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}

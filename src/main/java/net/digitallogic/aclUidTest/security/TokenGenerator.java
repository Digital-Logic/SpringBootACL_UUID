package net.digitallogic.aclUidTest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenGenerator {

    private final String tokenSecret;
    private final String iss;
    private final long expiration;

    public TokenGenerator(String tokenSecret, String iss, long expiration) {
        this.tokenSecret = tokenSecret;
        this.iss = iss;
        this.expiration = expiration;
    }

    public long getExpiration() { return expiration; }

    public String allocateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(iss)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public Claims verifyToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}

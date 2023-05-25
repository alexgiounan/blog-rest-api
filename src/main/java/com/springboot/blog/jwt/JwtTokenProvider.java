package com.springboot.blog.jwt;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private Long jwtExpiration;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime()+jwtExpiration);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // get username from JwtToken
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        return username;
    }

    //validate jwt token
    public boolean isTokenValid(String token){
        try {

        Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parse(token);
        return true;
        }catch (MalformedJwtException ex){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        }catch (ExpiredJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
        }catch (UnsupportedJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token");
        }catch (IllegalArgumentException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }
}

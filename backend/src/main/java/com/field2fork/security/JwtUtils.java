package com.field2fork.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${spring.security.jwt.secret.key}")
    private String jwtSecret;

    @Value("${spring.security.jwt.exp.time}")
    private int jwtExpirationMs;

    private Key key;
    
   
    

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String generateJwtToken(Authentication authentication) {
        CustomUserDetailsImpl userPrincipal = (CustomUserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .claim("authorities", "ROLE_" + userPrincipal.getUserEntity().getRole().name()) 

                .claim("user_id", userPrincipal.getUserEntity().getId())
                .claim("email", userPrincipal.getUserEntity().getEmail())
                .claim("username", userPrincipal.getUserEntity().getUsername())
                .claim("contactNumber", userPrincipal.getUserEntity().getContactNumber())
                .claim("role", userPrincipal.getUserEntity().getRole().name()) 
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(Claims claims) {
        return claims.getSubject();
    }

    public Claims validateJwtToken(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private String getAuthoritiesInString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
        String authString = claims.get("authorities", String.class);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authString);
    }

    public Long getUserIdFromJwtToken(Claims claims) {
        return claims.get("user_id", Long.class);
    }

    public Authentication populateAuthenticationTokenFromJWT(String jwt) {
        Claims claims = validateJwtToken(jwt);
        String email = getUserNameFromJwtToken(claims);
        List<GrantedAuthority> authorities = getAuthoritiesFromClaims(claims);
        Long id = getUserIdFromJwtToken(claims);

        return new UsernamePasswordAuthenticationToken(email, id, authorities);
    }
    
}
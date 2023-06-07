package com.mehisen.referralquizbackend.security.jwt;

import com.mehisen.referralquizbackend.payload.request.ReferralRequest;
import com.mehisen.referralquizbackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {
    @Value("${mehisen.app.jwtSecret}")
    private String jwtSecret;

    @Value("${mehisen.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtTokenForAdminAuth(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateJwtTokenForReferral(ReferralRequest request, Integer expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("newGuestPhone", request.getNewGuestPhone());
        claims.put("currentGuestPhone", request.getCurrentGuestPhone());
        claims.put("fromAdmin", request.getFromAdmin());

        return Jwts.builder()
                .setSubject(request.getNewGuestPhone())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getAdminUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public ReferralRequest getReferralRequestFromJwtToken(String token) {
        return ReferralRequest.builder()
                .newGuestPhone(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("newGuestPhone").toString())
                .fromAdmin((Boolean) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("fromAdmin"))
                .currentGuestPhone(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("currentGuestPhone").toString()).build();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
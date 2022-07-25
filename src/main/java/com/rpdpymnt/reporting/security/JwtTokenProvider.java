package com.rpdpymnt.reporting.security;

import com.rpdpymnt.reporting.RpdpymntProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final RpdpymntProperties rpdpymntProperties;

    public JwtTokenProvider(RpdpymntProperties rpdpymntProperties) {
        this.rpdpymntProperties = rpdpymntProperties;
    }

    public String generateToken(Authentication authentication) {

        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final Date expiryDate = new Date(System.currentTimeMillis() + rpdpymntProperties.getJwt().getExpirationInMs());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, rpdpymntProperties.getJwt().getSecret())
                .compact();
    }

    public String generateVodagrafRegisterPageToken(String dfCode, String shopCode, String secretKey) {

        final Date expiryDate = new Date(System.currentTimeMillis() + rpdpymntProperties.getJwt().getExpirationVodagrafRegInMs());

        return Jwts.builder()
                .setSubject(dfCode + shopCode + secretKey)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, rpdpymntProperties.getJwt().getSecret())
                .compact();
    }

    public Long getUserIdFromJwt(String token) {
        final Claims claims = Jwts.parser().setSigningKey(rpdpymntProperties.getJwt().getSecret()).parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(rpdpymntProperties.getJwt().getSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
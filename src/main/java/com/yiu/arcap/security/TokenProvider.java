package com.yiu.arcap.security;

import com.yiu.arcap.entity.Token;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Duration;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final TokenRepository tokenRepository;

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;
    // 만료시간 => 1h => 1000L * 60 * 60
    private final long accessTokenValidTime = Duration.ofMinutes(1).toMillis();

    private final long refreshTokenValidTime = Duration.ofDays(14).toMillis();
    public String generateToken(User user) {
        Date now = new Date();
        return makeToken(new Date(now.getTime()+accessTokenValidTime), user);
    }

    private String makeToken(Date expiry, User user){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)//(헤더) 타입: JWT
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("nickname",user.getNickname())
                .claim("uid", user.getUid())
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken(User user) {
        Token token = tokenRepository.save(
                Token.builder()
                        .id(user.getUid())
                        .refreshToken(UUID.randomUUID().toString())
                        .expiration(refreshTokenValidTime)
                        .build()
        );
        return token.getRefreshToken();
    }
    public boolean validToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(),"",authorities
        ), token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("uid", Long.class);
    }
    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

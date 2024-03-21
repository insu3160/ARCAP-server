package com.yiu.arcap.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64.Decoder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
//@ConfigurationProperties("jwt")
public class JwtProperties implements InitializingBean {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    @Override
    public  void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}

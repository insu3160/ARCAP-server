package com.yiu.arcap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("refreshToken")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @JsonIgnore
    private Long id;

    private String refreshToken;

    private String accessToken;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}

package com.yiu.arcap.entity;

import jakarta.persistence.Id;
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
public class RefreshToken {

    @Id
    private Long id; // 토큰의 ID로 이를 사용자 식별자(예: 이메일 또는 사용자 ID)로 설정할 수 있습니다.

    private String refreshToken; // 리프레시 토큰 값

    @TimeToLive
    private Long expiration; // 토큰 만료 시간 (초 단위)

    // setter 메서드는 필요에 따라 추가
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}

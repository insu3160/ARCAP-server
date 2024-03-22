package com.yiu.arcap.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDto {
    private String refreshToken;
    private String accessToken;
}
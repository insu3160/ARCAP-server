package com.yiu.arcap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {
    private String email;
    private String nickname;
    private TokenResponseDto token;
}

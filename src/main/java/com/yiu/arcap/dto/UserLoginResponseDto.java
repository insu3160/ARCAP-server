package com.yiu.arcap.dto;

import com.yiu.arcap.entity.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto {
    private Long uid;
    private String email;
    private String nickname;
    private TokenDto token;
}

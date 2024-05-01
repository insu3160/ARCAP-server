package com.yiu.arcap.dto.signup;

import lombok.Data;

@Data
public class AuthVerifyRequestDto {
    private String email;
    private String authCode;
}

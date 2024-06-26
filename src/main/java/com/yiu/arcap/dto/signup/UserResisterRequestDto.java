package com.yiu.arcap.dto.signup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResisterRequestDto {
    private String email;
    private String nickname;
    private String password;
}

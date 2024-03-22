package com.yiu.arcap.controller;


import com.yiu.arcap.dto.AuthGenerateRequestDto;
import com.yiu.arcap.dto.AuthVerifyRequestDto;
import com.yiu.arcap.dto.NickCheckRequestDto;
import com.yiu.arcap.dto.TokenRequestDto;
import com.yiu.arcap.dto.TokenResponseDto;
import com.yiu.arcap.dto.UserLoginRequestDto;
import com.yiu.arcap.dto.UserLoginResponseDto;
import com.yiu.arcap.dto.UserResisterRequestDto;
import com.yiu.arcap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> join(UserResisterRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(userService.register(request), HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserLoginResponseDto> login(UserLoginRequestDto request) throws Exception {
        return new ResponseEntity<UserLoginResponseDto>(userService.login(request), HttpStatus.OK);
    }

    @PostMapping(value = "/join/authgenerate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> authgenerate(AuthGenerateRequestDto request) throws Exception {
        return new ResponseEntity<String>(userService.authGenerate(request.getEmail()), HttpStatus.OK);
    }

    @PostMapping(value = "/join/authverify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> authverify(AuthVerifyRequestDto request) throws Exception {
        return new ResponseEntity<Boolean>(userService.authVerify(request), HttpStatus.OK);
    }

    @PostMapping(value = "/nicknamecheck", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> checkNicknameDuplication(NickCheckRequestDto request) {
        return new ResponseEntity<Boolean>(userService.checkNicknameDuplication(request), HttpStatus.OK);
    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TokenResponseDto> refresh(TokenRequestDto tokenRequestDto) throws Exception {
        return new ResponseEntity<TokenResponseDto>(userService.generateNewAccessToken(tokenRequestDto), HttpStatus.OK);
    }
}

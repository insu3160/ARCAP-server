package com.yiu.arcap.controller;

import com.yiu.arcap.config.CustomUserDetails;
import com.yiu.arcap.dto.CapsuleRequest;
import com.yiu.arcap.dto.PartyRequest;
import com.yiu.arcap.service.CapsuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/capsule")
public class CapsuleController {

    private final CapsuleService capsuleService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, CapsuleRequest.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(capsuleService.create(user.getUsername(), request), HttpStatus.OK);
    }


}

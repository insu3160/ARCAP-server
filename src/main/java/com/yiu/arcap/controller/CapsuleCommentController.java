package com.yiu.arcap.controller;

import com.yiu.arcap.config.CustomUserDetails;
import com.yiu.arcap.dto.CapsuleCommentRequest;
import com.yiu.arcap.dto.CapsuleRequest;
import com.yiu.arcap.service.CapsuleCommentService;
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
@RequestMapping("/comment")
public class CapsuleCommentController {
    private final CapsuleCommentService capsuleCommentService;
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, CapsuleCommentRequest.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(capsuleCommentService.create(user.getUsername(), request), HttpStatus.OK);
    }
}

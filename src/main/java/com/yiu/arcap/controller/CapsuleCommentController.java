package com.yiu.arcap.controller;

import com.yiu.arcap.config.CustomUserDetails;
import com.yiu.arcap.dto.capsulecomment.CapsuleCommentRequestDto;
import com.yiu.arcap.service.CapsuleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CapsuleCommentController {

    private final CapsuleCommentService capsuleCommentService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, CapsuleCommentRequestDto.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(capsuleCommentService.create(user.getUsername(), request), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{commentid}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("commentid") Long commentid) throws Exception {
        return new ResponseEntity<Boolean>(capsuleCommentService.delete(user.getUsername(), commentid), HttpStatus.OK);
    }

}

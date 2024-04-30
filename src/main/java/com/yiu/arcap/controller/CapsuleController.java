package com.yiu.arcap.controller;

import com.yiu.arcap.config.CustomUserDetails;
import com.yiu.arcap.dto.CapsuleRequest;
import com.yiu.arcap.dto.CapsuleResponseDto;
import com.yiu.arcap.dto.PartyRequest;
import com.yiu.arcap.service.CapsuleService;
import com.yiu.arcap.service.LikesService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/capsule")
public class CapsuleController {

    private final CapsuleService capsuleService;

    private final LikesService likesService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, CapsuleRequest.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(capsuleService.create(user.getUsername(), request), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List> partyCapsules(@AuthenticationPrincipal CustomUserDetails user,
                                              @ModelAttribute CapsuleRequest.LocationDto request) throws Exception {
        return new ResponseEntity<List>(capsuleService.getPartyCapsules(user.getUsername(), request), HttpStatus.OK);
    }

    @PutMapping(value = "/like/{cid}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> toggleLike(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long cid) {
        return new ResponseEntity<Boolean>(likesService.toggleLike(user.getUsername(), cid), HttpStatus.OK);
    }

//    @GetMapping("/{cid}")
//    public ResponseEntity<CapsuleResponseDto> getCapsule(@AuthenticationPrincipal CustomUserDetails user,
//                                                         @PathVariable Long cid) throws Exception {
//        return new ResponseEntity<CapsuleResponseDto>(capsuleService.getCapsule(user.getUsername(), cid), HttpStatus.OK);
//    }

}

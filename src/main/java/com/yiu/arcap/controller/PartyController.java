package com.yiu.arcap.controller;

import com.yiu.arcap.config.CustomUserDetails;
import com.yiu.arcap.dto.PartyRequest;
import com.yiu.arcap.dto.PartyRequest.CreateDTO;
import com.yiu.arcap.dto.PartyResponse;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.service.PartyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    @GetMapping(value = "/my", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<List> getMyParties(@AuthenticationPrincipal CustomUserDetails user) throws Exception {
        return new ResponseEntity<List>(partyService.getMyParties(user.getUsername()), HttpStatus.OK);
    }

    @PostMapping(value = "/applications", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<List> getApplications(@AuthenticationPrincipal CustomUserDetails user, PartyRequest.PidDto request) throws Exception {
        return new ResponseEntity<List>(partyService.getApplications(user.getUsername(),request), HttpStatus.OK);
    }

    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, PartyRequest.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(partyService.create(user.getUsername(), request), HttpStatus.OK);
    }

    @PostMapping(value = "/join",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> join(@AuthenticationPrincipal CustomUserDetails user, PartyRequest.JoinDTO request) throws Exception {
        return new ResponseEntity<Boolean>(partyService.join(user.getUsername(), request), HttpStatus.OK);
    }

}

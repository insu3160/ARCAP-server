package com.yiu.arcap.controller;

import com.yiu.arcap.config.CustomUserDetails;
import com.yiu.arcap.dto.userparty.ApplicationResponseDto;
import com.yiu.arcap.dto.party.PartyRequestDto;
import com.yiu.arcap.service.PartyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, PartyRequestDto.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(partyService.create(user.getUsername(), request), HttpStatus.OK);
    }

    @GetMapping(value = "/my", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<List> getMyParties(@AuthenticationPrincipal CustomUserDetails user) throws Exception {
        return new ResponseEntity<List>(partyService.getMyParties(user.getUsername()), HttpStatus.OK);
    }

    @GetMapping(value = "/applications/{pid}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ApplicationResponseDto> getApplications(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("pid") Long pid) throws Exception {
        return new ResponseEntity<ApplicationResponseDto>(partyService.getApplications(user.getUsername(), pid), HttpStatus.OK);
    }

    @PostMapping(value = "/join",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> join(@AuthenticationPrincipal CustomUserDetails user, PartyRequestDto.JoinDTO request) throws Exception {
        return new ResponseEntity<Boolean>(partyService.join(user.getUsername(), request), HttpStatus.OK);
    }

    @PutMapping(value = "/joinaccept/{upid}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> joinAccept(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("upid") Long upid) throws Exception {
        return new ResponseEntity<Boolean>(partyService.joinAccept(user.getUsername(), upid), HttpStatus.OK);
    }

    @DeleteMapping(value = "/joinreject/{upid}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> joinReject(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("upid") Long upid) throws Exception {
        return new ResponseEntity<Boolean>(partyService.joinReject(user.getUsername(), upid), HttpStatus.OK);
    }

    @PostMapping(value = "/invite",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> invite(@AuthenticationPrincipal CustomUserDetails user, PartyRequestDto.InviteDto request) throws Exception {
        return new ResponseEntity<Boolean>(partyService.invite(user.getUsername(), request), HttpStatus.OK);
    }

    @GetMapping(value = "/invitations", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<List> getInvitations(@AuthenticationPrincipal CustomUserDetails user) throws Exception {
        return new ResponseEntity<List>(partyService.getInvitations(user.getUsername()), HttpStatus.OK);
    }
    @PutMapping(value = "/inviteaccept/{upid}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> inviteAccept(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("upid") Long upid) throws Exception {
        return new ResponseEntity<Boolean>(partyService.inviteAccept(user.getUsername(), upid), HttpStatus.OK);
    }

    @DeleteMapping(value = "/invitereject/{upid}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> inviteReject(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("upid") Long upid) throws Exception {
        return new ResponseEntity<Boolean>(partyService.inviteReject(user.getUsername(), upid), HttpStatus.OK);
    }

    @GetMapping(value = "/users/{pid}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<List> getPartyUsers(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("pid") Long pid) throws Exception {
        return new ResponseEntity<List>(partyService.getPartyUsers(user.getUsername(),pid), HttpStatus.OK);
    }

//    @GetMapping(value = "/capsule/{pid}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<List> getPartyUsers(@AuthenticationPrincipal CustomUserDetails user, @PathVariable("pid") Long pid) throws Exception {
//        return new ResponseEntity<List>(partyService.getPartyUsers(user.getUsername(), pid), HttpStatus.OK);
//    }

}

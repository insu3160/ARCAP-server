package com.yiu.arcap.service;

import com.yiu.arcap.constant.ParticipationStatus;
import com.yiu.arcap.dto.userparty.ApplicationResponseDto;
import com.yiu.arcap.dto.party.PartyRequestDto.CreateDTO;
import com.yiu.arcap.dto.party.PartyRequestDto.InviteDto;
import com.yiu.arcap.dto.party.PartyRequestDto.JoinDTO;
import com.yiu.arcap.dto.party.PartyResponseDto;
import com.yiu.arcap.dto.userparty.UserPartyResponseDto;
import com.yiu.arcap.entity.Party;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.entity.UserParty;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.exception.ErrorCode;
import com.yiu.arcap.repository.PartyRepository;
import com.yiu.arcap.repository.UserPartyRepository;
import com.yiu.arcap.repository.UserRepository;
import com.yiu.arcap.util.RandomCodeGenerator;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyService {

    private final UserRepository userRepository;

    private final PartyRepository partyRepository;

    private final UserPartyRepository userPartyRepository;
    @Transactional
    public Boolean create(String email, CreateDTO request) {
        if(request.getPartyName() == null) {
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }
        User user = userRepository.findById(email).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        try {
            // 유저 확인 404 포함

            String uniqueCode;
            do {
                uniqueCode = RandomCodeGenerator.createCode();
            }while (partyRepository.existsByPartyCode(uniqueCode));

            Party party = Party.builder()
                    .partyName(request.getPartyName())
                    .partyCode(uniqueCode)
                    .partyleader(user.getNickname())
                    .build();
            partyRepository.save(party);

            UserParty userParty = UserParty.builder()
                    .user(user)
                    .party(party)
                    .build();
            userParty.updateStatus(ParticipationStatus.ACCEPTED);
            userPartyRepository.save(userParty);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    @Transactional
    public Boolean join(String email, JoinDTO request) {
        if (request.getPartyCode() == null){
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
        }
        User user = userRepository.findById(email).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Party party = partyRepository.findByPartyCode(request.getPartyCode()).orElseThrow(()->new CustomException(ErrorCode.PARTY_NOT_FOUND));

        if (userPartyRepository.existsByUserAndParty(user, party)) {
            // 이미 존재하는 경우, 커스텀 예외 또는 에러 코드로 처리
            throw new CustomException(ErrorCode.ALREADY_EXIST);
        }

        try {

            UserParty userParty = UserParty.builder()
                    .user(user)
                    .party(party)
                    .status(ParticipationStatus.PENDING)
                    .build();
            userPartyRepository.save(userParty);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    @Transactional
    public List getMyParties(String email) {
        User user = userRepository.findById(email).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        List<UserParty> acceptedList = userPartyRepository.findByStatusAndUser(ParticipationStatus.ACCEPTED, user);
        return acceptedList.stream()
                .map(userParty -> {
                    Party party = userParty.getParty();
                    int participantCount = userPartyRepository.countByPartyAndStatus(party, ParticipationStatus.ACCEPTED);

                    return PartyResponseDto.builder()
                            .pid(party.getPid())
                            .partyName(party.getPartyName())
                            .createdAt(party.getCreatedAt())
                            .updatedAt(party.getUpdatedAt())
                            .participantCount(participantCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponseDto getApplications(String email, Long pid) {
        User user = userRepository.findById(email).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Party party = partyRepository.findById(pid).orElseThrow(()->new CustomException(ErrorCode.PARTY_NOT_FOUND));

        List<ParticipationStatus> unresolvedStatuses = Arrays.asList(ParticipationStatus.PENDING, ParticipationStatus.INVITED);
        if (party.isLeader(user.getNickname())){
            return ApplicationResponseDto.builder()
                    .partyCode(party.getPartyCode())
                    .applications(userPartyRepository.findByStatusInAndParty(unresolvedStatuses, party).stream()
                            .map(userParty -> UserPartyResponseDto.builder()
                                    .nickname(userParty.getUser().getNickname())
                                    .participationStatus(userParty.getStatus())
                                    .upid(userParty.getUpid())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();
        }
        throw new CustomException(ErrorCode.ACCESS_NO_AUTH);
    }

    @Transactional
    public Boolean joinAccept(String email, Long upid) {
        User user = userRepository.findById(email).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        UserParty userParty = userPartyRepository.findById(upid).orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST));
        if (userParty.isAccepted()){
            throw new CustomException(ErrorCode.ALREADY_EXIST);
        }
        else if (userParty.getParty().isLeader(user.getNickname())){
            userParty.updateStatus(ParticipationStatus.ACCEPTED);
            return true;
        }
        throw new CustomException(ErrorCode.NO_AUTH);
    }

    @Transactional
    public Boolean joinReject(String email, Long upid) {
        User user = userRepository.findById(email).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        UserParty userParty = userPartyRepository.findById(upid).orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST));
        if (userParty.isAccepted()){
            throw new CustomException(ErrorCode.ALREADY_EXIST);
        }
        else if (userParty.getParty().isLeader(user.getNickname())){
            userPartyRepository.delete(userParty);
            return true;
        }
        throw new CustomException(ErrorCode.NO_AUTH);
    }

    @Transactional
    public Boolean invite(String email, InviteDto request) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Party party = partyRepository.findById(request.getPid())
                .orElseThrow(()->new CustomException(ErrorCode.PARTY_NOT_FOUND));
        User invitedUser = userRepository.findByNickname(request.getNickname())
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        if (userPartyRepository.existsByUserAndParty(invitedUser, party)){
            throw new CustomException(ErrorCode.ALREADY_EXIST);
        }

        if (party.isLeader(user.getNickname())){
            try {
                UserParty userParty = UserParty.builder()
                        .user(invitedUser)
                        .party(party)
                        .status(ParticipationStatus.INVITED)
                        .build();

                userPartyRepository.save(userParty);

                return true;
            }
            catch (Exception e) {
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        throw new CustomException(ErrorCode.NO_AUTH);
    }

    @Transactional
    public List getInvitations(String email) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        return userPartyRepository.findByStatusAndUser(ParticipationStatus.INVITED,user).stream()
                .map(userParty -> UserPartyResponseDto.builder()
                        .participationStatus(userParty.getStatus())
                        .partyName(userParty.getParty().getPartyName())
                        .upid(userParty.getUpid())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean inviteAccept(String email, Long upid) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        UserParty userParty = userPartyRepository.findById(upid)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST));

        if (userParty.isInvited(user)){
            userParty.updateStatus(ParticipationStatus.ACCEPTED);
            return true;
        }
        throw new CustomException(ErrorCode.NO_AUTH);
    }

    @Transactional
    public Boolean inviteReject(String email, Long upid) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        UserParty userParty = userPartyRepository.findById(upid)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST));

        if (userParty.isInvited(user)){
            userPartyRepository.delete(userParty);
            return true;
        }
        throw new CustomException(ErrorCode.NO_AUTH);
    }

    @Transactional
    public List getPartyUsers(String email, Long pid) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Party party = partyRepository.findById(pid)
                .orElseThrow(()->new CustomException(ErrorCode.PARTY_NOT_FOUND));

        boolean isMember = userPartyRepository.existsByStatusAndUserAndParty(ParticipationStatus.ACCEPTED, user, party);
        if (!isMember) {
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        return userPartyRepository.findByStatusAndParty(ParticipationStatus.ACCEPTED, party).stream()
                .map(userParty -> UserPartyResponseDto.UserListDto.builder()
                        .nickname(userParty.getUser().getNickname())
                        .joinedAt(userParty.getJoinedAt())
                        .build())
                .collect(Collectors.toList());
    }

}

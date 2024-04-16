package com.yiu.arcap.service;

import com.yiu.arcap.constant.ParticipationStatus;
import com.yiu.arcap.dto.CapsuleRequest.CreateDTO;
import com.yiu.arcap.entity.Capsule;
import com.yiu.arcap.entity.Party;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.exception.ErrorCode;
import com.yiu.arcap.repository.CapsuleRepository;
import com.yiu.arcap.repository.PartyRepository;
import com.yiu.arcap.repository.UserPartyRepository;
import com.yiu.arcap.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CapsuleService {
    private final UserRepository userRepository;

    private final PartyRepository partyRepository;

    private final UserPartyRepository userPartyRepository;

    private final CapsuleRepository capsuleRepository;
    @Transactional
    public Boolean create(String email, CreateDTO request) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Party party = partyRepository.findById(request.getPid())
                .orElseThrow(()->new CustomException(ErrorCode.PARTY_NOT_FOUND));
        if (userPartyRepository.existsByStatusAndUserAndParty(ParticipationStatus.ACCEPTED, user, party)){
            try {
                Capsule capsule = Capsule.builder()
                        .user(user)
                        .party(party)
                        .title(request.getTitle())
                        .contents(request.getContents())
                        .locationName(request.getLocationName())
                        .likesCount(0)
                        .build();
                capsuleRepository.save(capsule);
                return true;
            }catch (Exception e){
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        throw new CustomException(ErrorCode.NO_AUTH);
    }

}

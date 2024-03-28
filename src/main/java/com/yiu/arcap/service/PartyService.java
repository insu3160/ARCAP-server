package com.yiu.arcap.service;

import com.yiu.arcap.constant.ParticipationStatus;
import com.yiu.arcap.dto.PartyRequest.CreateDTO;
import com.yiu.arcap.dto.PartyRequest.JoinDTO;
import com.yiu.arcap.dto.PartyRequest.PidDto;
import com.yiu.arcap.dto.UserPartyDto;
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
                .map(UserParty::getParty)
                .collect(Collectors.toList());
    }

    @Transactional
    public List getApplications(String email, PidDto request) {
        User user = userRepository.findById(email).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Party party = partyRepository.findById(request.getPid()).orElseThrow(()->new CustomException(ErrorCode.PARTY_NOT_FOUND));
        if (party.isLeader(user.getNickname())){
            return userPartyRepository.findByStatusAndParty(ParticipationStatus.PENDING, party).stream()
                    .map(userParty -> UserPartyDto.builder()
                            .nickname(userParty.getUser().getNickname())
                            .upid(userParty.getUpid())
                            .build())
                    .collect(Collectors.toList());
        }
        throw new CustomException(ErrorCode.ACCESS_NO_AUTH);
    }

}

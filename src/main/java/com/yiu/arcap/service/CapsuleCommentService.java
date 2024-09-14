package com.yiu.arcap.service;

import com.yiu.arcap.constant.ParticipationStatus;
import com.yiu.arcap.dto.capsulecomment.CapsuleCommentRequestDto.CreateDTO;
import com.yiu.arcap.entity.Capsule;
import com.yiu.arcap.entity.CapsuleComment;
import com.yiu.arcap.entity.Party;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.exception.ErrorCode;
import com.yiu.arcap.repository.CapsuleCommentRepository;
import com.yiu.arcap.repository.CapsuleRepository;
import com.yiu.arcap.repository.UserPartyRepository;
import com.yiu.arcap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CapsuleCommentService {

    private final UserRepository userRepository;

    private final CapsuleRepository capsuleRepository;

    private final UserPartyRepository userPartyRepository;

    private final CapsuleCommentRepository capsuleCommentRepository;

    public Boolean create(String email, CreateDTO request) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Capsule capsule = capsuleRepository.findById(request.getCid())
                .orElseThrow(()->new CustomException(ErrorCode.CAPSULE_NOT_FOUND));
        Party party = capsule.getParty();

        if (userPartyRepository.existsByStatusAndUserAndParty(ParticipationStatus.ACCEPTED,user,party)){
           try {
               CapsuleComment capsuleComment = CapsuleComment.builder()
                       .user(user)
                       .capsule(capsule)
                       .contents(request.getContents())
                       .build();
               capsuleCommentRepository.save(capsuleComment);
               return true;
           }catch (Exception e){
               throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
           }
        }

        throw new CustomException(ErrorCode.NO_AUTH);
    }

    public Boolean delete(String email, Long commentid) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        CapsuleComment capsuleComment = capsuleCommentRepository.findById(commentid)
                .orElseThrow(()->new CustomException(ErrorCode.CAPSULECOMMENT_NOT_FOUND));

        if (capsuleComment.getUser().getEmail().equals(user.getEmail())){
            capsuleCommentRepository.delete(capsuleComment);
            return true;
        }

        throw new CustomException(ErrorCode.NO_AUTH);

    }

}

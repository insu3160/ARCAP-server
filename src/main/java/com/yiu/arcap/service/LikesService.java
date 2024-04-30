package com.yiu.arcap.service;

import com.yiu.arcap.entity.Capsule;
import com.yiu.arcap.entity.Likes;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.exception.ErrorCode;
import com.yiu.arcap.repository.CapsuleRepository;
import com.yiu.arcap.repository.LikesRepository;
import com.yiu.arcap.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final UserRepository userRepository;

    private final LikesRepository likesRepository;

    private final CapsuleRepository capsuleRepository;

    @Transactional
    public boolean toggleLike(String email, Long cid) {
        User user = userRepository.findById(email)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        Capsule capsule = capsuleRepository.findById(cid)
                .orElseThrow(()->new CustomException(ErrorCode.CAPSULE_NOT_FOUND));
        Optional<Likes> like = likesRepository.findByUserAndCapsule(user, capsule);

        if (like.isPresent()) {
            // 좋아요 삭제
            likesRepository.delete(like.get());
            capsule.setLikesCount(capsule.getLikesCount() - 1);
        } else {
            // 좋아요 추가
            Likes newLike = Likes.builder()
                    .user(user)
                    .capsule(capsule)
                    .build();
            likesRepository.save(newLike);
            capsule.setLikesCount(capsule.getLikesCount() + 1);
        }
        capsuleRepository.save(capsule);
        return true;
    }
}

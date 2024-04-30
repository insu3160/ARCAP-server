package com.yiu.arcap.repository;

import com.yiu.arcap.entity.Capsule;
import com.yiu.arcap.entity.Likes;
import com.yiu.arcap.entity.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByUserAndCapsule(User user, Capsule capsule);
}

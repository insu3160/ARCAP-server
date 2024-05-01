package com.yiu.arcap.repository;

import com.yiu.arcap.entity.Capsule;
import com.yiu.arcap.entity.CapsuleComment;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CapsuleCommentRepository extends JpaRepository<CapsuleComment, Long> {
    List<CapsuleComment> findByCapsule(Capsule capsule);
}

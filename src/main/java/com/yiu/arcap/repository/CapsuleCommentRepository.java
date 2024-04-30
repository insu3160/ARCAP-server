package com.yiu.arcap.repository;

import com.yiu.arcap.entity.CapsuleComment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CapsuleCommentRepository extends JpaRepository<CapsuleComment, Long> {
}

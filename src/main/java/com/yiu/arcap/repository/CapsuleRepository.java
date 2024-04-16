package com.yiu.arcap.repository;

import com.yiu.arcap.entity.Capsule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
}

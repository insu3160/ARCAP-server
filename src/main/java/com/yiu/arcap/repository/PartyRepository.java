package com.yiu.arcap.repository;

import com.yiu.arcap.entity.Party;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface PartyRepository extends JpaRepository<Party, Long> {
    boolean existsByPartyCode(String partyCode);
}

package com.yiu.arcap.repository;

import com.yiu.arcap.entity.Party;
import com.yiu.arcap.entity.RefreshToken;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface PartyRepository extends JpaRepository<Party, Long> {
    boolean existsByPartyCode(String partyCode);
    Optional<Party> findByPartyCode(String partyCode);
}

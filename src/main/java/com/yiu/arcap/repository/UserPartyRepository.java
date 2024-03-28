package com.yiu.arcap.repository;

import com.yiu.arcap.constant.ParticipationStatus;
import com.yiu.arcap.entity.Party;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.entity.UserParty;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface UserPartyRepository extends JpaRepository<UserParty, Long> {
    List<UserParty> findByUser(User user);
    List<UserParty> findByParty(Party party);
    List<UserParty> findByStatusAndUser(ParticipationStatus status, User user);
    List<UserParty> findByStatusAndParty(ParticipationStatus status, Party party);
}

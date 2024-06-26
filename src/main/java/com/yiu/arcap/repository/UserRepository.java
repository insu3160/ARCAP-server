package com.yiu.arcap.repository;



import com.yiu.arcap.entity.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByNickname(String nickname);

}

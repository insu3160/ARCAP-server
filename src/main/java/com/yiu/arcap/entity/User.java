package com.yiu.arcap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true, length = 10)
    private String nickname;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String fcm;

    //    @CreatedDate
    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    //    @LastModifiedDate
    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private Set<UserParty> userParties = new HashSet<>();

    public User(String email, String nickname, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void encodePwd(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}

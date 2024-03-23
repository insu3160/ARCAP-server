package com.yiu.arcap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false)
    private String partyleader;

    @Column(nullable = false, unique = true)
    private String partyCode;

    @Column(nullable = false, length = 10)
    private String partyName;

    @Formula("(SELECT COUNT(*) FROM user_party up WHERE up.party_pid = pid AND up.status = 1)")
    private int participantCount;

    //    @CreatedDate
    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    //    @LastModifiedDate
    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "party")
    private Set<UserParty> userParties = new HashSet<>();

}

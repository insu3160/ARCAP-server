package com.yiu.arcap.entity;

import com.yiu.arcap.constant.ParticipationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_party", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"uid", "pid"})
})
public class UserParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long upid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Party party;

    @Column
    private LocalDateTime joinedAt;

    @Column(nullable = false)
    private ParticipationStatus status;

    // status를 업데이트하는 메서드
    public void updateStatus(ParticipationStatus newStatus) {
        // 상태를 업데이트합니다.
        this.status = newStatus;

        // 새 상태가 1(예: 활성 상태)일 경우, joinedAt을 현재 시간으로 설정
        if (newStatus == ParticipationStatus.ACCEPTED) { // ACTIVE는 예시입니다. 실제 사용하는 열거형 값을 사용해야 합니다.
            this.joinedAt = LocalDateTime.now();
        }
    }

}

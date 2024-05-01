package com.yiu.arcap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapsuleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ccid;

    @ManyToOne
    @JoinColumn(name = "cid", nullable = false) // 'cid'는 Capsule의 ID를 참조합니다.
    private Capsule capsule;

    @ManyToOne
    @JoinColumn(name = "uid", nullable = false) // 'uid'는 User의 ID를 참조합니다.
    private User user;

    @Column(nullable = false, length = 500) // 댓글 내용은 500자로 제한합니다.
    private String contents;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

}
package com.yiu.arcap.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapsuleCommentResponseDto {
    private Long ccid;

    private String contents;

    private String nickName;

    private LocalDateTime createdAt;
}

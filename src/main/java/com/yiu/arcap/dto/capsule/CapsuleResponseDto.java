package com.yiu.arcap.dto.capsule;

import com.yiu.arcap.dto.capsulecomment.CapsuleCommentResponseDto;
import java.time.LocalDateTime;
import java.util.List;
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
public class CapsuleResponseDto {
    private Long cid;

    private String partyName;

    private String nickName;

    private String title;

    private String contents;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private LocalDateTime createdAt;

    private int likesCount;

    private List<CapsuleCommentResponseDto> comments;
}

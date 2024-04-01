package com.yiu.arcap.dto;

import com.yiu.arcap.constant.ParticipationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPartyDto {
    private String nickname;
    private ParticipationStatus participationStatus;
    private Long upid;
}

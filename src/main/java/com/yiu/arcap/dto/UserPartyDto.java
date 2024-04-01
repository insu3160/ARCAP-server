package com.yiu.arcap.dto;

import com.yiu.arcap.constant.ParticipationStatus;
import com.yiu.arcap.entity.Party;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPartyDto {
    private String nickname;
    private ParticipationStatus participationStatus;
    private String partyName;
    private Long upid;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserListDto {
        private String nickname;
        private LocalDateTime joinedAt;
    }
}

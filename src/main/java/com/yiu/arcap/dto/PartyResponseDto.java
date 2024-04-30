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
public class PartyResponseDto {

    private Long pid;

    private String partyName;

    private String personNum;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int participantCount;

}

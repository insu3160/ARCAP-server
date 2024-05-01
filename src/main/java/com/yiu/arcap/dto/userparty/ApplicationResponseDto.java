package com.yiu.arcap.dto.userparty;

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
public class ApplicationResponseDto {
    private String partyCode;
    private List<UserPartyResponseDto> applications;
}

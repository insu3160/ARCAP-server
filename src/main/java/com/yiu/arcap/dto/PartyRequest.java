package com.yiu.arcap.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

public class PartyRequest {
    @Getter
    @Setter
    public static class CreateDTO {
        private String partyName;
    }
}

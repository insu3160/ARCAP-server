package com.yiu.arcap.dto.party;

import lombok.Getter;
import lombok.Setter;

public class PartyRequestDto {
    @Getter
    @Setter
    public static class CreateDTO {
        private String partyName;
    }
    @Getter
    @Setter
    public static class JoinDTO {
        private String partyCode;
    }
    @Getter
    @Setter
    public static class PidDto {
        private Long pid;
    }
    @Getter
    @Setter
    public static class InviteDto{
        private String nickname;
        private Long pid;
    }

}

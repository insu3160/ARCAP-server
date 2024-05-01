package com.yiu.arcap.dto.capsulecomment;

import lombok.Getter;
import lombok.Setter;

public class CapsuleCommentRequestDto {
    @Getter
    @Setter
    public static class CreateDTO {
        private Long cid;
        private String contents;
    }
}

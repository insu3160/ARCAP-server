package com.yiu.arcap.dto;

import lombok.Getter;
import lombok.Setter;

public class CapsuleCommentRequest {
    @Getter
    @Setter
    public static class CreateDTO {
        private Long cid;
        private String contents;
    }
}

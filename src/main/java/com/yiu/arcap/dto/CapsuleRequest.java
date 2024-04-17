package com.yiu.arcap.dto;

import lombok.Getter;
import lombok.Setter;

public class CapsuleRequest {
    @Getter
    @Setter
    public static class CreateDTO {
        private Long pid;
        private String title;
        private String contents;
        private String locationName;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Setter
    public static class LocationDto {
        private Double latitude;
        private Double longitude;
    }

}

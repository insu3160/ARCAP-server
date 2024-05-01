package com.yiu.arcap.dto.capsule;

import lombok.Getter;
import lombok.Setter;

public class CapsuleRequestDto {
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

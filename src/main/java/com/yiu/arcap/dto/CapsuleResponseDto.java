package com.yiu.arcap.dto;

import com.yiu.arcap.entity.Party;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CapsuleResponseDto {
    private Long cid;

    private String partyName;

    private String nickName;

    private String title;

    private String contents;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private LocalDateTime createdAt;

    private int likesCount;

}

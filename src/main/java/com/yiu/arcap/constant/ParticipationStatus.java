package com.yiu.arcap.constant;

import lombok.Getter;

@Getter
public enum ParticipationStatus {
    PENDING(0), // 대기 중
    ACCEPTED(1), // 수락됨
    INVITED(2); // 초대됨

    private final int state;

    ParticipationStatus(int state) {
        this.state = state;
    }
    public static ParticipationStatus fromInt(int value) {
        for (ParticipationStatus state : ParticipationStatus.values()) {
            if (state.getState() == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid ParticipationStatus value: " + value);
    }
}

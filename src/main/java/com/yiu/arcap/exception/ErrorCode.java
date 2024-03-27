package com.yiu.arcap.exception;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    INSUFFICIENT_DATA(400, ErrorMessage.INSUFFICIENT_DATA),

    UNAUTHORIZED(401, ErrorMessage.UNAUTHORIZED),

    NO_AUTH(401, ErrorMessage.NO_AUTH),

    // 권한 없음
    ACCESS_NO_AUTH(403, ErrorMessage.ACCESS_NO_AUTH),

    ACCESS_TOKEN_EXPIRED(403,ErrorMessage.ACCESS_TOKEN_EXPIRED),
    REFRESH_TOKEN_EXPIRED(403, ErrorMessage.REFRESH_TOKEN_EXPIRED),

    VALID_NOT_STUDENT_ID(401, ErrorMessage.VALID_NOT_STUDENT_ID),
    VALID_NOT_PWD(401, ErrorMessage.VALID_NOT_PWD),
    MEMBER_NOT_EXIST(401, ErrorMessage.MEMBER_NOT_EXIST),
    LOGIN_REQUIRED(401, ErrorMessage.LOGIN_REQUIRED),
    VALID_NOT_AUTHCODE(401,ErrorMessage.VALID_NOT_AUTHCODE),

    // 데이터를 찾을 수 없음
    NOT_EXIST(404, ErrorMessage.NOT_EXIST),

    // 데이터 충돌
    CONFLICT(409, ErrorMessage.CONFLICT),
    // 데이터 중복
    DUPLICATE(409, ErrorMessage.DUPLICATE),
    // 인원 초과
    EXCESS(409, ErrorMessage.EXCESS),

    // 서버 오류
    INTERNAL_SERVER_ERROR(500, ErrorMessage.INTERNAL_SERVER_ERROR),
    // 레디스 서버 오류
    REDIS_SERVER_ERROR(500, ErrorMessage.REDIS_SERVER_ERROR),
    ACCESS_TOKEN_PRESENT(500,ErrorMessage.ACCESS_TOKEN_PRESENT ),
    STOLEN_REFRESH_TOKEN(500, ErrorMessage.STOLEN_REFRESH_TOKEN),
    USER_NOT_FOUND(400, ErrorMessage.USER_NOT_FOUND ),
    PARTY_NOT_FOUND(400, ErrorMessage.PARTY_NOT_FOUND);

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

package com.yiu.arcap.exception;

public class ErrorMessage {
    public static final String UNAUTHORIZED = "인증 실패";
    public static final String NO_AUTH = "접근 권한 없음";
    public static final String ACCESS_NO_AUTH = "접근 권한 없음";
    public static final String ACCESS_TOKEN_EXPIRED = "AccessToken 만료";
    public static final  String REFRESH_TOKEN_EXPIRED = "RefreshToken 만료";
    public static final String VALID_NOT_STUDENT_ID = "가입하지 않은 학번";
    public static final String VALID_NOT_PWD = "잘못된 비밀번호";
    public static final String MEMBER_NOT_EXIST = "존재하지 않는 사용자";
    public static final String LOGIN_REQUIRED = "로그인 필요";
    public static final String INSUFFICIENT_DATA = "데이터 부족";
    public static final String NOT_EXIST = "존재하지 않음";
    public static final String CONFLICT = "데이터 충돌";
    public static final String DUPLICATE = "데이터 중복";
    public static final String EXCESS = "인원 초과";
    public static final String INTERNAL_SERVER_ERROR = "내부 서버 오류";
    public static final String REDIS_SERVER_ERROR = "Redis 서버 오류";
    public static final String VALID_NOT_AUTHCODE = "잘못된 인증 코드";
    public static final String ACCESS_TOKEN_PRESENT = "만료되지 않은 엑세스토큰";
    public static final String STOLEN_REFRESH_TOKEN = "탈취당한 리프레시 토큰";
    public static final String USER_NOT_FOUND = "유저 찾을 수 없음";
    public static final String PARTY_NOT_FOUND = "파티 찾을 수 없음";
}

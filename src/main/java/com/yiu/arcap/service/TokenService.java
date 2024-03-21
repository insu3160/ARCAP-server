package com.yiu.arcap.service;

import com.yiu.arcap.entity.Token;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.repository.TokenRepository;
import com.yiu.arcap.repository.UserRepository;
import com.yiu.arcap.security.TokenProvider;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final UserService userService;

//    public TokenResponse createNewAccessToken(TokenRequest request) {
//        // 토큰 유효성 검사에 실패하면 예외 발생
//
//        if (!tokenProvider.validToken(request.getRefreshToken())) {
//            throw new IllegalArgumentException("Unexpected token");
//        }
//
//
//        String email = findByRefreshToken(request.getRefreshToken()).getEmail();
//        User user = userService.findByEmail(email);
//        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
//        System.out.println("새로 생성된 액세스 토큰: " + accessToken);
//
//        return TokenResponse.builder()
//                .accessToken(accessToken)
//                .build();
//
//    }
//    public Token validRefreshToken(User user, String refreshToken) throws Exception {
//        Token token = tokenRepository.findById(user.getUid()).orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
//        // 해당 유저 Refresh 토큰 만료 => Redis에 해당 유저의 토큰이 존재하지 않음
//        if(token.getRefreshToken() == null) {
//            return null;
//        }
//        else {
//            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
//            if(token.getExpiration() < 10) {
//                token.setExpiration(1000);
//                tokenRepository.save(token);
//            }
//
//            // 토큰이 같은지 비교
//            if(!token.getRefreshToken().equals(refreshToken)) {
//                return null;
//            }
//            else {
//                return token;
//            }
//        }
//    }
    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}

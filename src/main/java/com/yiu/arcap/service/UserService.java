package com.yiu.arcap.service;

import com.yiu.arcap.dto.AuthVerifyRequestDto;
import com.yiu.arcap.dto.NickCheckRequestDto;
import com.yiu.arcap.dto.TokenRequestDto;
import com.yiu.arcap.dto.TokenResponseDto;
import com.yiu.arcap.dto.UserLoginRequestDto;
import com.yiu.arcap.dto.UserLoginResponseDto;
import com.yiu.arcap.dto.UserResisterRequestDto;
import com.yiu.arcap.entity.RefreshToken;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.exception.ErrorCode;
import com.yiu.arcap.repository.RefreshTokenRepository;
import com.yiu.arcap.repository.UserRepository;
import com.yiu.arcap.security.TokenProvider;
import com.yiu.arcap.util.RandomCodeGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final static int EXPIRATIONTIME = 3;// 유효기간 3분

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final JavaMailSender emailSender;

    private final RedisTemplate<String, String> redisTemplate;

    private final RefreshTokenRepository refreshTokenRepository;

    // <API> 회원가입
    @Transactional
    public boolean register(UserResisterRequestDto request) throws Exception {

        // 데이터 저장
        try {
            User user = User.builder()
                    .email(request.getEmail())
                    .nickname(request.getNickname())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            userRepository.save(user);
        }
        catch (Exception e) {
            throw new Exception("서버 오류");
        }
        return true;
    }


    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        if(request.getEmail() == null || request.getPassword() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 401 - 유저 존재 확인
        User user = userRepository.findById(request.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 401 - 비밀번호 일치 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.VALID_NOT_PWD);
        }


        try {
            String refreshToken = tokenProvider.generateRefreshToken(user);
            String accessToken = tokenProvider.generateToken(user);
            return UserLoginResponseDto.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .token(TokenResponseDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build())
                    .build();

        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public String authGenerate(String email) throws Exception {

        String authCode = RandomCodeGenerator.createCode();
        MimeMessage emailForm = createEmailForm(email, authCode);
        emailSender.send(emailForm);
        redisTemplate.opsForValue().set(email, authCode, EXPIRATIONTIME, TimeUnit.MINUTES);
        return authCode;
    }

    public MimeMessage createEmailForm(String email, String authCode) throws MessagingException {

        String setFrom = "yiuarcap@gmail.com";
        String title = "ARCAP 이메일 인증";

        jakarta.mail.internet.MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);

        // 메일 내용 설정
        String msgOfEmail="";
        msgOfEmail += "<div style='margin:20px;'>";
        msgOfEmail += "<h1> 안녕하세요 ARCAP 입니다. </h1>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<p>감사합니다.<p>";
        msgOfEmail += "<br>";
        msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgOfEmail += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgOfEmail += "<div style='font-size:130%'>";
        msgOfEmail += "CODE : <strong>";
        msgOfEmail += authCode + "</strong><div><br/> ";
        msgOfEmail += "</div>";

        message.setFrom(setFrom);
        message.setText(msgOfEmail, "utf-8", "html");
        return message;
    }

    public Boolean authVerify(AuthVerifyRequestDto request) {
        String storedAuthCode = redisTemplate.opsForValue().get(request.getEmail());
        if (storedAuthCode == null){
            throw new CustomException(ErrorCode.NO_AUTH);
        }
        else if (!storedAuthCode.equals(request.getAuthCode())){
            throw new CustomException(ErrorCode.VALID_NOT_AUTHCODE);
        }
        // Redis에 저장된 인증번호와 사용자가 제공한 이메일 주소 비교

            // 인증 성공 시 Redis에서 인증번호 삭제
        redisTemplate.delete(request.getEmail());
        return true;
    }

    public Boolean checkNicknameDuplication(NickCheckRequestDto request) {
        if (userRepository.findByNickname(request.getNickname()).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE);
        }
        return true;
    }

    public TokenResponseDto generateNewAccessToken(TokenRequestDto tokenRequestDto) {
        if (tokenProvider.validToken(tokenRequestDto.getAccessToken())) {
            // 액세스 토큰이 아직 유효하면 새로 발급할 필요가 없습니다.
            throw new CustomException(ErrorCode.ACCESS_TOKEN_PRESENT);
        }

        // 리프레시 토큰으로 사용자 정보 조회
        String refreshToken = tokenRequestDto.getRefreshToken();

        RefreshToken storedToken = refreshTokenRepository.findById(tokenProvider.getUserIdFromExpiredToken(tokenRequestDto.getAccessToken()))
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED));

        if (!storedToken.getRefreshToken().equals(refreshToken)){
            throw new CustomException(ErrorCode.STOLEN_REFRESH_TOKEN);
        }

        User user = userRepository.findById(storedToken.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

        String newAccessToken = tokenProvider.generateToken(user);

        // 새로운 TokenDto 객체를 생성하여 반환
        return new TokenResponseDto(refreshToken, newAccessToken);
    }
}

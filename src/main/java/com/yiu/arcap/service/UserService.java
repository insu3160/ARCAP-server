package com.yiu.arcap.service;

import com.yiu.arcap.dto.AuthVerifyRequestDto;
import com.yiu.arcap.dto.NickCheckRequestDto;
import com.yiu.arcap.dto.TokenDto;
import com.yiu.arcap.dto.UserLoginRequestDto;
import com.yiu.arcap.dto.UserLoginResponseDto;
import com.yiu.arcap.dto.UserResisterRequestDto;
import com.yiu.arcap.entity.Token;
import com.yiu.arcap.entity.User;
import com.yiu.arcap.exception.CustomException;
import com.yiu.arcap.exception.ErrorCode;
import com.yiu.arcap.repository.TokenRepository;
import com.yiu.arcap.repository.UserRepository;
import com.yiu.arcap.security.TokenProvider;
import com.yiu.arcap.util.RandomNumberGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> {
            return new IllegalArgumentException("User ID를 찾을 수 없습니다.");
        });
    }

    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        if(request.getEmail() == null || request.getPassword() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 401 - 유저 존재 확인
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));

        // 401 - 비밀번호 일치 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.VALID_NOT_PWD);
        }


        try {
            // 리프레시 토큰 생성
            user.setRefreshToken(tokenProvider.generateRefreshToken(user));
//            user.setFcm(request.getFcm());
            // String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
            return UserLoginResponseDto.builder()
                    .uid(user.getUid())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .token(TokenDto.builder()
                            .accessToken(tokenProvider.generateToken(user))
                            .refreshToken(user.getRefreshToken())
                            .build())
                    .build();
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public String authGenerate(String email) throws Exception {

        String authCode = RandomNumberGenerator.createCode();
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

//    public TokenDto generateNewAccessToken(TokenDto token) {
//
//    }
}

package com.ssafy.sulnaeeum.model.user.service;

import com.ssafy.sulnaeeum.exception.CustomException;
import com.ssafy.sulnaeeum.exception.CustomExceptionList;
import com.ssafy.sulnaeeum.jwt.TokenProvider;
import com.ssafy.sulnaeeum.model.user.dto.UserPreferenceDto;
import com.ssafy.sulnaeeum.model.user.entity.UserPreference;
import com.ssafy.sulnaeeum.model.user.repo.UserPreferenceRepo;
import com.ssafy.sulnaeeum.model.user.repo.UserRepo;
import com.ssafy.sulnaeeum.model.user.dto.TokenDto;
import com.ssafy.sulnaeeum.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepo userRepository;
    private final UserPreferenceRepo userPreferenceRepo;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto refreshToken(TokenDto tokenRequestDto) {

        // refresh token 검증
        if(!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new CustomException(CustomExceptionList.TOKEN_VALID_FAILED);
        }

        // access token에서 사용자 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        User user = userRepository.findByKakaoId(authentication.getName()).orElseThrow(() -> new CustomException(CustomExceptionList.MEMBER_NOT_FOUND));

        // 저장된 refresh token과 받은 token 비교
        if(!user.getToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(CustomExceptionList.TOKEN_VALID_FAILED);
        }

        // AccessToken, RefreshToken 재발급 및 RefreshToken 저장
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken();

        user.updateToken(refreshToken);

        return TokenDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Transactional
    public void logout(String kakaoId) {

        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new CustomException(CustomExceptionList.MEMBER_NOT_FOUND));

        user.updateToken(null);
    }

    @Transactional
    public void preference(String kakaoId, UserPreferenceDto userPreferenceDto) {

        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new CustomException(CustomExceptionList.MEMBER_NOT_FOUND));

        UserPreference userPreference = userPreferenceDto.toEntity();
        userPreference.setUser(user);

        userPreferenceRepo.save(userPreference);
        user.updateFinish(true);
    }

    // kakaoId로 userId 찾기
    public Long findUserId(String kakaoId) {
        Optional<Long> userId = userRepository.findUserId(kakaoId);
        if(!userId.isPresent()) {
            throw new CustomException(CustomExceptionList.MEMBER_NOT_FOUND);
        }
        return userId.get();
    }
}
package com.finalproject.backend.baseballmate.OAuth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KakaoUserInfo {
    Long id;
    String email;
    String nickname;
    String picture;
}

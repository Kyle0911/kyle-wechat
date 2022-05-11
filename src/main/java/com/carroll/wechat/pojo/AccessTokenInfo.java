package com.carroll.wechat.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: carroll.he
 * @date 2022/5/7 
 */
@Getter
@Setter
public class AccessTokenInfo {

    private String accessToken;

    private Long expiresIn;

    private String refreshToken;

    private String openid;

    private String scope;
}
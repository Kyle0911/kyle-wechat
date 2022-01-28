package com.carroll.wechat.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 登录凭证校验接口返回内容
 *
 * @author: carroll.he
 * @date 2021/12/10 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginCodeParseResult {

    private String openId;

    private String sessionKey;
}
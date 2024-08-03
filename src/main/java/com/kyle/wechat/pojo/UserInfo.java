package com.kyle.wechat.pojo;

/*
 *@title 微信公众用户信息
 *@description
 *@author kyle.he
 *@version 1.0
 *@create 2024/8/1 22:23
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    private String openId;

    private String unionId;

    private String remark;
}

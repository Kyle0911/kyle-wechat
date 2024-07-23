package com.kyle.wechat.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信用户手机号信息
 *
 * @author: kyle.he
 * @date 2021/12/10 
 */
@Getter
@Setter
public class UserMobileInfo {
    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;
    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;
    /**
     * 区号
     */
    private String countryCode;

    private Watermark watermark;
}
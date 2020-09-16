package com.carroll.wechat.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信通用接口凭证
 *
 * @author correy
 * @date 2013-08-08
 */
@Data
public class AccessTokenAndTicket implements Serializable{
    // 获取到的凭证
    private String token;
    // 凭证有效时间，单位：秒
    private int expiresIn;

    private Date createTime;

    private String jsTicket;
}

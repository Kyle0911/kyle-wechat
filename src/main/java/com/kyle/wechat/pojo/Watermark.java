package com.kyle.wechat.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: kyle.he
 * @date 2021/12/10 
 */
@Getter
@Setter
public class Watermark {
    /**
     * 敏感数据归属 appId，开发者可校验此参数与自身 appId 是否一致
     */
    private String appid;
    /**
     * 敏感数据获取的时间戳, 开发者可以用于数据时效性校验
     */
    private Long timestamp;
}
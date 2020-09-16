package com.carroll.wechat;

import com.carroll.wechat.pojo.AccessTokenAndTicket;

import java.util.Map;

/**
 * @author: carroll.he
 * @date 2020/9/15
 */
public interface IWechatHandler {

    /**
     * 文本消息处理
     * @param requestMap
     * @return
     */
    String handleTextMsg(Map<String, String> requestMap);
    String handleImageMsg(Map<String, String> requestMap);
    String handleLocationMsg(Map<String, String> requestMap);
    String handleLinkMsg(Map<String, String> requestMap);
    String handleVoiceMsg(Map<String, String> requestMap);

    String handleSubscribe(Map<String, String> requestMap);
    String handleUnSubscribe(Map<String, String> requestMap);
    String handleClick(Map<String, String> requestMap);
    /**
     * 从缓存中获取access token
     * @return
     */
    AccessTokenAndTicket getSavedAccessToken();

    /**
     * 获取到的Access token 保存到缓存中
     * @param token
     */
    void saveAccessTokenAndTicket(AccessTokenAndTicket token);
}

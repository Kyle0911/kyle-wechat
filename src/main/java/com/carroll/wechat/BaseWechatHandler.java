package com.carroll.wechat;

import com.carroll.wechat.pojo.AccessTokenAndTicket;
import com.carroll.wechat.response.TextMessage;
import com.carroll.wechat.utils.MessageUtil;

import java.util.Map;

/**
 * @author: carroll.he
 * @date 2020/9/16 
 */
public class BaseWechatHandler implements IWechatHandler {
    @Override
    public String handleTextMsg(Map<String, String> requestMap) {
        return generateTextMessagXml("您发送的是文本消息！:"+requestMap.get("Content"),requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public String handleImageMsg(Map<String, String> requestMap) {
        return generateTextMessagXml("您发送的是图片消息！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public String handleLocationMsg(Map<String, String> requestMap) {
        return generateTextMessagXml("您发送的是地理位置消息！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public String handleLinkMsg(Map<String, String> requestMap) {
        return generateTextMessagXml("您发送的是链接消息！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public String handleVoiceMsg(Map<String, String> requestMap) {
        return generateTextMessagXml("您发送的是音频消息！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public String handleSubscribe(Map<String, String> requestMap) {
        return generateTextMessagXml("谢谢您的关注！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public String handleUnSubscribe(Map<String, String> requestMap) {
        return generateTextMessagXml("谢谢您的关注！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public String handleClick(Map<String, String> requestMap) {
        return generateTextMessagXml("谢谢您的关注，暂不支持！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }
    @Override
    public String handleScan(Map<String, String> requestMap) {
        return generateTextMessagXml("谢谢您的关注，暂不支持！",requestMap.get("FromUserName"),requestMap.get("ToUserName"));
    }

    @Override
    public AccessTokenAndTicket getSavedAccessToken() {
        return null;
    }

    @Override
    public void saveAccessTokenAndTicket(AccessTokenAndTicket token) {

    }

    public String generateTextMessagXml(String msg,String fromUserName,String toUserName){
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setContent(msg);
        return MessageUtil.textMessageToXml(textMessage);
    }
}

package com.kyle.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kyle.wechat.pojo.AccessTokenAndTicket;
import com.kyle.wechat.pojo.send.MessageTemplate;
import com.kyle.wechat.pojo.send.SendResult;
import com.kyle.wechat.response.TextMessage;
import com.kyle.wechat.utils.MessageUtil;
import com.kyle.wechat.utils.StringUtils;
import com.kyle.wechat.utils.WechatUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核心服务类
 *
 * @author correy
 * @date 2013-05-20
 */
@Service
@Slf4j
@Data
public class WeChatService {

    private static final String SEND_URL_BY_OPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
    private static final String SEND_URL_BY_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    private static final String TOKEN_KEY = "WECHAT_ACCESS_TOKEN";

    @Autowired
    private IWechatHandler handler;

    @Value("${wechat.appid}")
    private String appid;
    @Value("${wechat.appsecret}")
    private String appsecret;

    @PostConstruct
    private void init(){
        if(handler==null){
            log.warn("请自定义消息处理接口:com.kyle.wechat.IWechatHandler");
        }
    }

    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            // 默认返回的文本消息内容  
            String respContent = "请求处理异常，请稍候尝试！";

            // xml请求解析  
            Map<String, String> requestMap = MessageUtil.parseXml(request);

            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");
            // 消息类型  
            String msgType = requestMap.get("MsgType");

            log.debug("reques map:" + StringUtils.toJson(requestMap));
            log.debug("reques map:" + StringUtils.toJson(request.getParameterMap()));

            // 回复文本消息  
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//            textMessage.setFuncFlag(0);

            // 文本消息  
            if (msgType.equalsIgnoreCase(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                if(handler!=null){
                    return handler.handleTextMsg(requestMap);
                }
                respContent = "您发送的是文本消息！:"+requestMap.get("Content");
//                respMessage = MessageUtil.newsMessageToXml(sendRecommendAndHistory(fromUserName,toUserName));
                log.debug("消息:{}", respMessage);

//                return respMessage;

            }
            // 图片消息  
            else if (msgType.equalsIgnoreCase(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                if(handler!=null){
                    return handler.handleImageMsg(requestMap);
                }
                respContent = "您发送的是图片消息！";
            }
            // 地理位置消息  
            else if (msgType.equalsIgnoreCase(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                if(handler!=null){
                    return handler.handleLocationMsg(requestMap);
                }
                respContent = "您发送的是地理位置消息！";
            }
            // 链接消息  
            else if (msgType.equalsIgnoreCase(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                if(handler!=null){
                    return handler.handleLinkMsg(requestMap);
                }
                respContent = "您发送的是链接消息！";
            }
            // 音频消息  
            else if (msgType.equalsIgnoreCase(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                if(handler!=null){
                    return handler.handleVoiceMsg(requestMap);
                }
                respContent = "您发送的是音频消息！";
            }
            // 事件推送  
            else if (msgType.equalsIgnoreCase(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型  
                String eventType = requestMap.get("Event");
                // 订阅  
                if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    if(handler!=null){
                        return handler.handleSubscribe(requestMap);
                    }
                    respContent = "谢谢您的关注！";

                }
                // 取消订阅  
                else if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    if(handler!=null){
                        return handler.handleUnSubscribe(requestMap);
                    }
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 自定义菜单点击事件  
                else if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_CLICK)) {
                    if(handler!=null){
                        return handler.handleClick(requestMap);
                    }
                    respContent = "谢谢您的关注，暂不支持！";
                } else if(eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_SCAN)){
                    if(handler!=null){
                        return handler.handleScan(requestMap);
                    }
                    respContent = "谢谢您的关注，暂不支持！";
                } else if(eventType.equalsIgnoreCase(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){
                    if(handler!=null){
                        return handler.handleLocationMsg(requestMap);
                    }
                    respContent = "谢谢您的关注，暂不支持！";
                }
            }

            textMessage.setContent(respContent);
            respMessage = MessageUtil.textMessageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("消息:{}", respMessage);

        return respMessage;
    }

    /**
     * 群发消息
     *
     * @param content
     * @param toUsers
     * @return
     * @throws Exception
     */
    public SendResult sendTextMessage(String content, List<String> toUsers) throws Exception {
        com.kyle.wechat.pojo.send.TextMessage message = new com.kyle.wechat.pojo.send.TextMessage();
        message.setTouser(toUsers);
        message.setContent(content);
        message.setMsgtype(MessageUtil.SEND_MESSAGE_TYPE_TEXT);
        AccessTokenAndTicket accessToken = getAccessToken();
        String url = SEND_URL_BY_OPENID.replace("ACCESS_TOKEN", accessToken.getToken());
        JSONObject obj = WechatUtil.httpRequest(url, "POST", StringUtils.toJson(message));
        SendResult result = null;
        try {
            result = obj == null ? null : JSON.parseObject(obj.toJSONString(), SendResult.class);
        } catch (Exception e) {
            log.warn("send weichat fail:", e);
        }
        return result;
    }

    /**
     * 发送模版消息
     *
     * @param template
     * @return
     * @throws Exception
     */
    public SendResult sendTemplateMessage(MessageTemplate template) throws Exception {
        AccessTokenAndTicket accessToken = getAccessToken();
        String url = SEND_URL_BY_TEMPLATE.replace("ACCESS_TOKEN", accessToken.getToken());
        JSONObject obj = WechatUtil.httpRequest(url, "POST", StringUtils.toJson(template));
        SendResult result = null;
        try {
            result = obj == null ? null : JSON.parseObject(obj.toJSONString(), SendResult.class);
        } catch (Exception e) {
            log.warn("send weichat fail:", e);
        }
        return result;
    }

    /**
     * 获取 AccessToken
     * @return
     */
    public AccessTokenAndTicket getAccessToken() {
        AccessTokenAndTicket token = null;
        if(handler!=null){
            token = handler.getSavedAccessToken();
        }
        if (token == null || StringUtils.isNullOrEmpty(token.getToken()) || StringUtils.isNullOrEmpty(token.getJsTicket())) {
            token = WechatUtil.getAccessToken(appid, appsecret);
            if(handler!=null){
                handler.saveAccessTokenAndTicket(token);
            }
        } else if (System.currentTimeMillis() - token.getCreateTime().getTime() > 7000 * 1000) {
            token = WechatUtil.getAccessToken(appid, appsecret);
            if(handler!=null){
                handler.saveAccessTokenAndTicket(token);
            }
        }
        return token;
    }

    /**
     * @param @return hashmap {appid,timestamp,nonceStr,signature}
     * @Description: 前端 jssdk 页面配置需要用到的配置参数
     */
    public HashMap<String, String> jsSDK_Sign(String url) throws Exception {
        String nonce_str = StringUtils.getUUID();
        String timestamp = String.valueOf((new Date()).getTime() / 1000);
        String jsapi_ticket = getAccessToken().getJsTicket();
        // 注意这里参数名必须全部小写，且必须有序
        String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
                + "&timestamp=" + timestamp + "&url=" + url;
        String signature = StringUtils.sha1(string1);
        log.debug("sha1 string1 :" + string1);
        HashMap<String, String> jssdk = new HashMap<String, String>();
        jssdk.put("appId", appid);
        jssdk.put("timestamp", timestamp);
        jssdk.put("nonceStr", nonce_str);
        jssdk.put("signature", signature);
        return jssdk;

    }

//    public static void main(String[] args) throws Exception {
//        WeiChatService s=new WeiChatService();
//        s.sendTextMessage("测试一下", Arrays.asList(new String[]{"o_M3Xv4cXXFDL5GDaYB55r3GoI5s"}));
//    }
}  
package com.kyle.wechat.response;

/**
 * 文本消息
 *
 * @author correy
 * @date 2013-05-19
 */
public class TextMessage extends BaseMessage {
    // 回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
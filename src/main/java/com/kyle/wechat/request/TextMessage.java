package com.kyle.wechat.request;

/**
 * 文本消息
 * Created by correy on 16/9/23.
 */
public class TextMessage extends BaseMessage {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}


package com.kyle.wechat.pojo.send;

import lombok.Data;

import java.util.List;

/**
 * Created by correy on 16/9/27.
 */
@Data
public class TextMessage {

    private List<String> touser;
    private String msgtype;
    private TextMessageContent text;

    public void setContent(String content){
        this.text=new TextMessageContent(content);
    }
}

@Data
class TextMessageContent {

    public TextMessageContent(){}
    public TextMessageContent(String content){
        this.content=content;
    }

    private String content;
}

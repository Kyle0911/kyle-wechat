package com.kyle.wechat.request;

/**
 * 图片消息
 *
 * @author correy
 * @date 2016-09-23
 */
public class ImageMessage extends BaseMessage {
    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}

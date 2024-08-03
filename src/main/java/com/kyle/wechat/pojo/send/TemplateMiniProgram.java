package com.kyle.wechat.pojo.send;

/*
 *@title TemplateMiniProgram
 *@description
 *@author kyle.he
 *@version 1.0
 *@create 2024/8/3 17:11
 */
public class TemplateMiniProgram {

    private String appid;

    private String pagepath;

    public TemplateMiniProgram() {
    }

    public TemplateMiniProgram(final String appid, final String pagepath) {
        this.appid = appid;
        this.pagepath = pagepath;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }
}

package com.kyle.wechat.pojo.send;

import lombok.Data;

import java.util.Map;

/**
 * Created by correy on 2016/10/21.
 */
@Data
public class MessageTemplate {
    private String touser;
    private String template_id;
    private String url;
    private Map<String,TemplateDataItem> data;
}

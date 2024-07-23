package com.kyle.wechat.pojo.send;

import lombok.Data;

/**
 * Created by correy on 2016/10/21.
 */
@Data
public class TemplateDataItem {
    private String value;
    private String color;
    public TemplateDataItem(String value,String color){
        this.value=value;
        this.color=color;
    }
}

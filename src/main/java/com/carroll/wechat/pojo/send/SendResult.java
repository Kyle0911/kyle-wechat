package com.carroll.wechat.pojo.send;

import lombok.Data;

/**
 * Created by correy on 16/9/28.
 */
@Data
public class SendResult {

    private String type;
    private String errcode;
    private String errmsg;
    private Long msg_id;
    private Long msg_data_id;
}

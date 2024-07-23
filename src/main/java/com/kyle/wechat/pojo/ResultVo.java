package com.kyle.wechat.pojo;

/**
 * Created by hhb on 2015/10/8.
 */
public class ResultVo {
    private String code="0";
    private String message;
    private Object obj;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultVo(String code, String message){
        this.code=code;
        this.message=message;
    }

    public ResultVo(){}

    public static ResultVo fail(String message){
        return new ResultVo("-1",message);
    }

    public static ResultVo success(String message){
        return new ResultVo("0",message);
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}

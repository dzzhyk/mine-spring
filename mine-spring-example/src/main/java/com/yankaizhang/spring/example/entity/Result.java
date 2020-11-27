package com.yankaizhang.spring.example.entity;

/**
 * @author dzzhyk
 */
public class Result {

    private int code;
    private Object data;
    private String msg;

    public Result(int code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static Result success(){
        return new Result(1, null, "成功");
    }

    public static Result failed(){
        return new Result(0, null, "失败");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

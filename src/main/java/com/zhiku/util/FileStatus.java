package com.zhiku.util;

public enum FileStatus {
    NORMAL("n","正常"),DUPLICATE("d","文件已存在"),TOO_LARGE("l","文件过大"),TYPE_ERROR("te","文件类型错误"),
    FILE_ERROR("fe","上传文件受损"),FORBIDDEN("f","文件被封禁"),UNCHECK("u","待审核");
    private final String code;
    private final String name;

    private FileStatus(String code,String name){
        this.code = code;
        this.name = name;
    }

    public String getCode(){ return this.code; }
    public String getName(){
        return this.name;
    }
}

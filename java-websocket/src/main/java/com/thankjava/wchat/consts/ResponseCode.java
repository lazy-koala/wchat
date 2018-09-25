package com.thankjava.wchat.consts;

/**
 * @Author: acexy@thankjava.com
 * 2018/8/10
 * @Description:
 **/
public enum ResponseCode {

    SUCCESS("0000", "处理完成"),
    FAILED("9998", "处理失败"),
    BAD_REQUEST_PARAMETER("9997", "请求参数错误"),

    TARGET_OFFLINE("8999", "目标已离线"),

    // chat 0001 - 0100
    NOT_YOUR_FRIEND("0001", "对方不是你的好友"),

    // friend 0101 - 0200
    FRIEND_APPLY_PROCESSED("0101", "该好友申请已处理"),
    USE_NOT_EXIST("0102", "不存在的用户"),
    ALREADY_FRIEND_ELATION("0103", "该用户已经是你的好友"),
    CAN_NOT_ADD_YOURSELF("0104", "不能添加自己"),
    EXIST_FRIEND_APPLY("0105", "已经发送过好友申请"),

    // group 0201 - 0300
    GROUP_NOT_EXIST("0201", "不存在的群组信息"),
    ALREADY_GROUP_ELATION("0202", "已经加入该群"),
    EXIST_GROUP_APPLY("0203", "已经发起过群组申请"),
    GROUP_APPLY_NOT_EXIST("0204", "不存在的群组申请id"),
    GROUP_APPLY_PROCESSED("0205", "该群组申请已处理"),



    ;

    ResponseCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String code;
    public String desc;
}

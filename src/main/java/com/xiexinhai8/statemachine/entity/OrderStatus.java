package com.xiexinhai8.statemachine.entity;
public enum OrderStatus {

    WAIT_SUBMIT(0, "待提交企业实名"),
    WAIT_REAL_NAME(1, "待用户实名"),
    WAIT_APPLICATION_FORM(2, "待提交申请表"),
    WAIT_CHECK(3, "待企业实名审核"),
    WAIT_REMIT_CHECK(4,"待打款验证"),
    WAIT_SIGN_CHECK(5, "待入驻签约"),
    FINISH(6,"企业入驻完成");

    OrderStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private int value;
    private String desc;

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatus of(int value) {
        for (OrderStatus type : OrderStatus.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return OrderStatus.WAIT_SUBMIT;
    }

    public static boolean isValid(int value) {
        for (OrderStatus mediumType : OrderStatus.values()) {
            if (mediumType.value == value) {
                return true;
            }
        }
        return false;
    }

}
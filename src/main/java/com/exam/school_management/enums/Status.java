package com.exam.school_management.enums;

public enum Status {

    SIX(1), SEVEN(2), EIGHT(3), NINE(4), ADMISSION(5), NOT_AVAILABLE(6), APPROVE(7), SENT_BACK(8), OPENING_STOCK(9),FORWARDED(10),RECOMMEND(11),TEN(14)
    ;



    private final Integer value;

    Status(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}

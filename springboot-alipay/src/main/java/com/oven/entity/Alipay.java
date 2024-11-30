package com.oven.entity;

import lombok.Data;

@Data
public class Alipay {

    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;

}
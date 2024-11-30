package com.oven.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultInfo<T> {

    private int code;
    private String message;
    private T data;

}

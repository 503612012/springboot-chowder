package com.oven.config;

public enum CacheType {

    TEN(10),

    FIVE(5);

    private final int expires;

    CacheType(int expires) {
        this.expires = expires;
    }

    public int getExpires() {
        return expires;
    }

}

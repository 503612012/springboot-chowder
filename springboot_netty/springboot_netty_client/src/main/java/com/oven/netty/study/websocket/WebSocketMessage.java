package com.oven.netty.study.websocket;

import lombok.Data;

import java.io.Serializable;

/**
 * WebSocketMessage
 */
@Data
public class WebSocketMessage implements Serializable {

    private static final long serialVersionUID = -4666429837358506065L;

    private String accept;
    private String content;
    private Type header;

    enum Type {
        send_user, send_users, request_success;
    }

}

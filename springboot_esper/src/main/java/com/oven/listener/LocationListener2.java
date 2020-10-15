package com.oven.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class LocationListener2 implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        if (newEvents != null) {
            System.out.println(String.format(" 222 匹配成功，批到的位置为：%s，要发送的手机号为：%s，原始信息内容：%s", newEvents[0].get("location"), newEvents[0].get("phone"), newEvents[0].getUnderlying()));
        }
    }
}
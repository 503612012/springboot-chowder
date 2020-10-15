package com.oven.util;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import java.util.HashMap;
import java.util.Map;

public class EsperUtil {

    private static final EPAdministrator administrator;
    private static final EPServiceProvider provider;

    static {
        provider = EPServiceProviderManager.getDefaultProvider();
        administrator = provider.getEPAdministrator();

        Map<String, Object> accesslog = new HashMap<>();
        accesslog.put("ip", String.class);
        accesslog.put("url", Integer.class);

        // 注册accesslog到esper
        administrator.getConfiguration().addEventType("accesslog", accesslog);
    }

    public static EPAdministrator getAdministrator() {
        return administrator;
    }

    public static EPServiceProvider getProvider() {
        return provider;
    }

}
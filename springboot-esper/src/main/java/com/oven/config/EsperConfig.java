package com.oven.config;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EsperConfig {

    public final static Map<String, EPStatement> STATEMENT_WRAP = new HashMap<>();

    @Bean
    public EPServiceProvider epServiceProvider() {
        com.espertech.esper.client.Configuration config = new com.espertech.esper.client.Configuration();

        Map<String, Object> mobileLocation = new HashMap<>();
        mobileLocation.put("location", String.class);
        mobileLocation.put("phone", Integer.class);

        config.addEventType("mobileLocation", mobileLocation);

        return EPServiceProviderManager.getDefaultProvider(config);
    }

    @Bean
    public EPAdministrator epAdministrator() {
        return epServiceProvider().getEPAdministrator();
    }

}
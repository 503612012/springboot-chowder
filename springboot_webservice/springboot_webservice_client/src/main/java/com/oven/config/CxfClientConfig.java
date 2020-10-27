package com.oven.config;

import com.oven.wsdl.UserPortType;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfClientConfig {

    private final static String SERVICE_ADDRESS = "http://localhost:8080/ws/user";

    @Bean("cxfProxy")
    public UserPortType createUserPortTypeProxy() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(UserPortType.class);
        jaxWsProxyFactoryBean.setAddress(SERVICE_ADDRESS);

        return (UserPortType) jaxWsProxyFactoryBean.create();
    }

}

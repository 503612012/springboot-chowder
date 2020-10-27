package com.oven.config;

import com.oven.service.UserService;
import com.oven.service.impl.UserServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfWebServiceConfig {

    @Bean("cxfServletRegistration")
    public ServletRegistrationBean<CXFServlet> dispatcherServlet() {
        return new ServletRegistrationBean<>(new CXFServlet(), "/ws/*");
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint endpoint(UserService userService) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), userService);
        endpoint.publish("/user");
        return endpoint;
    }

}

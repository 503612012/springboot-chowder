package com.oven.context;

import com.oven.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Hashtable;
import java.util.Map;

@Component
public class ServiceContext {

    private Map<String, IService> serviceHolder;

    @Autowired
    private void setServiceHolder(IService[] services) {
        serviceHolder = new Hashtable<>();
        for (IService service : services) {
            serviceHolder.put(service.serviceId(), service);
        }
    }

    public IService getService(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            serviceId = "00";
        }
        return serviceHolder.get(serviceId);
    }

}
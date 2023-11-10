package com.oven.service.impl;

import com.oven.service.ISaveIndividualizationService;
import org.springframework.stereotype.Service;

@Service
public class ServiceIndividualizationImpl implements ISaveIndividualizationService {
    @Override
    public void preSave() {
        System.out.println("preSaving...");
    }

    @Override
    public void afterSave() {
        System.out.println("afterSaveing...");
    }
}

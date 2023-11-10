package com.oven.service.impl;

import com.oven.service.ISaveIndividualizationService;
import com.oven.service.ISaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveServiceImpl implements ISaveService {

    @Autowired(required = false)
    private ISaveIndividualizationService saveIndividualizationService;

    @Override
    public void save() {
        if (saveIndividualizationService != null) {
            saveIndividualizationService.preSave();
        }
        System.out.println("saving...");
        if (saveIndividualizationService != null) {
            saveIndividualizationService.afterSave();
        }
    }

}

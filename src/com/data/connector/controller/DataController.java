package com.data.connector.controller;

import java.util.Collections;
import java.util.Map;
import com.data.connector.service.BaseService;
import com.data.connector.service.MappingService;
import com.data.connector.service.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/v1/data")
public class DataController {

    @Autowired
    private BaseService baseService;


    @Autowired
    private MigrationService migrationService;

    @Autowired
    private MappingService mappingService;

    @RequestMapping (value = "/test", method = RequestMethod.GET)
    public Map<String, Object> sayHello() {
        return Collections.EMPTY_MAP;
    }


    @RequestMapping(method = RequestMethod.GET)
    Map<String, Object> readData() {
        return baseService.readData();
    }

    @RequestMapping(value= "/mappings", method = RequestMethod.GET)
    Map<String, Object> getMappings() {return mappingService.getMappings("ZSALESFORCE_UPDATE","SALESFORCE_DEMO");}

    @RequestMapping(value= "/migrate", method = RequestMethod.GET)
    void migrate() {
        migrationService.startMigration();
    }
}
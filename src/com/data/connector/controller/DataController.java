package com.data.connector.controller;

import java.util.Collections;
import java.util.Map;
import com.data.connector.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/v1/data")
public class DataController {

    @Autowired
    private BaseService baseService;

    @RequestMapping (value = "/test", method = RequestMethod.GET)
    public Map<String, Object> sayHello() {
        return Collections.EMPTY_MAP;
    }


    @RequestMapping(method = RequestMethod.GET)
    Map<String, Object> readData() {
        return baseService.readData();
    }

}
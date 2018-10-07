package main.java.com.data.connector.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping ("/v1/data")
public class DataController {
    @RequestMapping (value = "/test", method = RequestMethod.GET)
    public Map<String, Object> sayHello() {
        final Map<String, Object> value = new HashMap<>();
        final Map<String, Object> customer = new HashMap<>();
        customer.putIfAbsent("Status", "SUCCESS");
        customer.putIfAbsent("FirstName", "Bruce");
        customer.putIfAbsent("LastName", "Wayne");
        customer.putIfAbsent("Email", "batman@gotham.com");
        customer.putIfAbsent("Phone", "703.555.1212");
        final List<Map<String, Object>> customerList = new ArrayList<>();
        customerList.add(customer);
        customerList.add(customer);
        value.putIfAbsent("customers", customerList);
        return value;
    }
}
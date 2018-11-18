package com.data.connector.service.gateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.data.connector.config.SFConfiguration;
import org.springframework.stereotype.Service;

@Service
public class SFGatewayService extends BaseGatewayService<SFConfiguration> {

    @Override
    public Map<String, Object> readData() {
        return null;
    }

    @Override
    public Map<String, Object> sendData(final Map<String, Object> data) {
      //TODO : Code to send data to Salesforce to be written here.
        System.out.print("Sending data to salesforce.");
        return new HashMap<>();
    }
}
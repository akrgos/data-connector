package com.data.connector.service;

import java.util.Map;
import com.data.connector.service.gateway.BaseGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MigrationService {

    @Autowired
    private BaseService source;

    @Autowired
    private BaseGatewayService destination;

    public void startMigration() {
        try {
            while(true) {
                final Map<String, Object> data = source.readData();
                final Map<String, Object> confirmedData = destination.sendData(data);
                source.sendData(confirmedData);
                Thread.sleep(10800000L);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
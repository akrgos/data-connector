package com.data.connector.service.gateway;

import java.util.Map;
import com.data.connector.config.BaseConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public abstract class BaseGatewayService<T extends BaseConfiguration> {

    @Autowired
    private T configuration;

    abstract public Map<String, Object> sendData(final Map<String, Object> data);

    abstract public Map<String, Object> readData();

}
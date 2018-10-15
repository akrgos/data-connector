package main.java.com.data.connector.service;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import main.java.com.data.connector.config.BaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public abstract class BaseService<T extends BaseConfiguration> {
    @Autowired
    private T configuration;

    public abstract void sendData(final Map<String, Object> data);

    public abstract Map<String, Object> readData();
}
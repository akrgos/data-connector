package main.java.com.data.connector.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataProvider;
import main.java.com.data.connector.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class SAPService extends BaseService<SapConfiguration> {

    private Properties properties;

    @Override
    public Map<String, Object> readData() {
        setProperties();
        final Map<String, Object> response = new HashMap<>();
        try {
            final JCoDestination destination = JCoDestinationManager.getDestination(getConfiguration().getDestinationName());
            final JCoFunction function = destination.getRepository().getFunction(getConfiguration().getFunction());
            function.getImportParameterList().setValue("I_NUM1", 10);
            function.getImportParameterList().setValue("I_NUM2", 5);
            function.execute(destination);
            response.put("status",200);
            response.put("message",function.getExportParameterList().getValue("E_RESULT"));
        } catch (final AbapException ae) {
            System.out.println("Abap Exception occured.");
            ae.printStackTrace();
            response.put("status",400);
            response.put("message",ae.getMessage());
        } catch (final Exception e) {
            System.out.println("Other exception occured.");
            e.printStackTrace();
            response.put("status",500);
            response.put("message",e.getMessage());
        }
        return response;
    }

    @Override
    public void sendData(Map<String, Object> data) {

    }

    private void setProperties() {
        properties = new Properties();
        properties.setProperty(DestinationDataProvider.JCO_ASHOST, getConfiguration().getHost());
        properties.setProperty(DestinationDataProvider.JCO_SYSNR, getConfiguration().getSystemNumber());
        properties.setProperty(DestinationDataProvider.JCO_USER, getConfiguration().getUser());
        properties.setProperty(DestinationDataProvider.JCO_PASSWD, getConfiguration().getPassword());
        properties.setProperty(DestinationDataProvider.JCO_LANG, getConfiguration().getLanguage());
    }
}
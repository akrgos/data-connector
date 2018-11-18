package com.data.connector.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.data.connector.config.SAPConfiguration;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;
import org.springframework.stereotype.Service;

@Service
public class SAPService extends BaseService<SAPConfiguration> {


    private Properties properties;

    @Override
    public Map<String, Object> readData() {
        /*setProperties();
        final Map<String, Object> response = new HashMap<>();
        try {
            final JCoDestination destination = JCoDestinationManager.getDestination(getConfiguration().getDestinationName());
            final JCoFunction function = destination.getRepository().getFunction(getConfiguration().getFunction());
            function.getImportParameterList().setValue("I_NUM1", 10);
            function.getImportParameterList().setValue("I_NUM2", 5);
            function.execute(destination);
            try {
                function.execute(destination);
            }
            catch(AbapException e) {
                response.put("status","500");
                response.put("message","Internal server error : "+e.getMessage());
            }

            System.out.println("T_RESULT");
            JCoTable table = function.getTableParameterList().getTable("T_RESULT");

            System.out.println("Rows = "+table.getNumRows());
            final List<Map<String, Object>> responseDataList = new ArrayList<>();
            for(int i=0;i<table.getNumRows();i++) {
                table.setRow(i);
                JCoFieldIterator iterator = table.getFieldIterator();
                final Map<String, Object> responseData = new HashMap<>();
                while(iterator.hasNextField()) {
                    JCoField data = iterator.nextField();
                    responseData.put(data.getName(), data.getValue());
                }
                responseDataList.add(responseData);
            }
            response.put("status",200);
            response.put("message","SUCCESS");
            response.put("data",responseDataList);
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
        return response;*/
        System.out.print("Getting data from SAP");
        return Collections.EMPTY_MAP;
    }

    @Override
    public void sendData(Map<String, Object> data) {
        //TODO: RFC call to insert data to SAP
        System.out.print("sending refurnished data to SAP");
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
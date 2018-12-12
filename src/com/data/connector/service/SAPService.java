package com.data.connector.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.data.connector.config.SAPConfiguration;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class SAPService extends BaseService<SAPConfiguration> {


    private Properties properties;

    private static String HOST="";
    private static String SYSTEM_NUMBER="";
    private static String CLIENT = "";
    private static String USER = "";
    private static String PASSWORD = "";
    private static String LANGUAGE = "";

    @Override
    public Map<String, Object> readData() {
        System.out.println("Reading data from SAP");
        setProperties();
        final Map<String, Object> response = new HashMap<>();
        try {
            final JCoDestination destination = JCoDestinationManager.getDestination(getConfiguration().getDestinationName());
            final JCoFunction function = destination.getRepository().getFunction("ZSALESFORCE_UPDATE");
            function.execute(destination);
            try {
                function.execute(destination);
            }
            catch(AbapException e) {
                response.put("status","500");
                response.put("message","Internal server error : "+e.getMessage());
            }
            JCoTable table = function.getTableParameterList().getTable("IT_SALESFORCE");
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
            response.put("status",400);
            response.put("message",ae.getMessage());
        } catch (final Exception e) {
            e.printStackTrace();
            System.out.println("Other exception occured.");
            response.put("status",500);
            response.put("message",e.getMessage());
        }
        return response;
    }

    @Override
    public void sendData(Map<String, Object> data) {
        //TODO: RFC call to insert data to SAP
        System.out.println("Sending data back to SAP");
        setProperties();
        System.out.println("From Salesforce : "+data);
        if(data != null && data.get("root") != null && new JSONObject(data.get("root").toString()).getJSONArray("data").length()>0) {
            JSONObject object = new JSONObject(data.get("root").toString());
            JSONArray array = object.getJSONArray("data");
            try {
                final JCoDestination destination = JCoDestinationManager.getDestination(getConfiguration().getDestinationName());
                final JCoFunction function = destination.getRepository().getFunction("ZSALESFORCE_UPDATE");
                final JCoTable table = function.getTableParameterList().getTable("IT_SALESFORCE");
                table.appendRows(array.length());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    table.setRow(i);
                    table.setValue("LIFSK", obj.getString("LIFSK"));
                    table.setValue("VBELN", obj.getString("VBELN"));
                    table.setValue("MANDT", obj.getString("MANDT"));
                    table.setValue("COUNTRY", obj.getString("COUNTRY"));
                    table.setValue("CITY1", obj.getString("CITY1"));
                    table.setValue("AUART", obj.getString("AUART"));
                    table.setValue("POST_CODE1", obj.getString("POST_CODE1"));
                    table.setValue("TELF1", obj.getString("TELF1"));
                    table.setValue("ZZ_RNIN", obj.getString("ZZ_RNIN"));
                    table.setValue("STREET", obj.getString("STREET"));
                    table.setValue("ZINDICATOR", obj.getString("ZINDICATOR"));
                }
                function.execute(destination);
                System.out.println("Migration Complete");
            } catch (final Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No records found in destination");
        }
    }

    private void setProperties() {
        getConnectionDetails();
        properties = new Properties();
        properties.setProperty(DestinationDataProvider.JCO_ASHOST, HOST);
        properties.setProperty(DestinationDataProvider.JCO_SYSNR, SYSTEM_NUMBER);
        properties.setProperty(DestinationDataProvider.JCO_USER, USER);
        properties.setProperty(DestinationDataProvider.JCO_PASSWD, PASSWORD);
        properties.setProperty(DestinationDataProvider.JCO_LANG, LANGUAGE);
        properties.setProperty(DestinationDataProvider.JCO_CLIENT, CLIENT);
        createDestinationDataFile(getConfiguration().getDestinationName(), properties);
    }

    private static void createDestinationDataFile(String destinationName, Properties connectProperties) {

        File destCfg = new File(destinationName+".jcoDestination");
        try {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "for tests only !");
            fos.close();
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }

    private static void getConnectionDetails() {
        final String SQL = "select a.login_id, a.password,a.token, b.client_or_client_secret, b.host_or_machine_address, b.machine_port_or_system_number, b.language from CONNECTION_AUTH a JOIN CONNECTION_MANAGER b on a.connection_id = b.connection_id and b.connection_name ='SAP'";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

        try (final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_connector_db","root","PeIjs5qbw!");
             final PreparedStatement preparedStatement = connection.prepareStatement(SQL)){

            final ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                USER = resultSet.getString("login_id");
                PASSWORD = resultSet.getString("password");
                CLIENT = resultSet.getString("client_or_client_secret");
                HOST = resultSet.getString("host_or_machine_address");
                LANGUAGE = resultSet.getString("language");
                SYSTEM_NUMBER = resultSet.getString("machine_port_or_system_number");
            }
            resultSet.close();
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
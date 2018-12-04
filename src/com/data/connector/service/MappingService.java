package com.data.connector.service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class MappingService {

    private static String MAPPING_SQL = "select md.source_field, md.destination_field from MAPPING_DETAILS md join MAPPING_MASTER mm on mm.mapping_id = md.mapping_id WHERE mm.source_api_id = (SELECT object_id FROM OBJECTS where object_name = ?) and mm.destination_api_id = (SELECT object_id FROM OBJECTS where object_name = ?);";

    public Map<String, Object> getMappings(final String source, final String destination) {
        Map<String, Object> response = new HashMap<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (final Exception e) {
            response.put("message",e.getMessage());
            response.put("status",500);
        }

        try (final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_connector_db","root","PeIjs5qbw!");
        final PreparedStatement preparedStatement = connection.prepareStatement(MAPPING_SQL)){

            preparedStatement.setString(1, source);
            preparedStatement.setString(2, destination);
            final ResultSet resultSet = preparedStatement.executeQuery();
            Map<String, Object> data = new HashMap<>();
            while(resultSet.next()) {
                data.put(resultSet.getString("source_field"), resultSet.getString("destination_field"));
            }
            response.put("data", data);
            response.put("status", 200);
            response.put("message","Success");
            resultSet.close();
        } catch (final SQLException e) {
            response.put("message", e.getErrorCode()+" "+e.getMessage());
            response.put("status",500);
        } catch (final Exception e) {
            response.put("message", e.getMessage());
            response.put("status",500);
        }
        return response;
    }

}

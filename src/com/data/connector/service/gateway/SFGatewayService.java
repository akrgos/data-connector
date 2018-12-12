package com.data.connector.service.gateway;

import java.sql.*;
import java.util.*;

import com.data.connector.config.SFConfiguration;
import com.data.connector.domain.Authorization;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SFGatewayService extends BaseGatewayService<SFConfiguration> {

    private HttpClient httpClient;

    private static String SQL = "select a.login_id, a.password,a.token, b.client_or_client_secret, b.host_or_machine_address, b.machine_port_or_system_number, b.grant_type, b.client_id from CONNECTION_AUTH a JOIN CONNECTION_MANAGER b on a.connection_id = b.connection_id and b.connection_name ='Salesforce'";

    @Autowired
    private SFConfiguration configuration;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> readData() {
        return null;
    }

    @Override
    public Map<String, Object> sendData(final Map<String, Object> data) {
        System.out.println("Sending data to Salesforce.");
        httpClient = HttpClientBuilder.create().build();
        final Optional<Authorization> optionalAuthorization = getOptionalAuthorization();
        if(optionalAuthorization.isPresent() && optionalAuthorization.get().getAuthToken()!=null && optionalAuthorization.get().getBaseUrl() !=null) {
          final String accessToken = optionalAuthorization.get().getAuthToken();
          final String baseUrl = optionalAuthorization.get().getBaseUrl();

          final HttpPost dataCheckRequest = new HttpPost(baseUrl+"/services/apexrest/SaveOrderReturn");
          dataCheckRequest.addHeader("Content-Type", "application/json");
          dataCheckRequest.addHeader("Authorization", "OAuth "+accessToken);
          try {
              dataCheckRequest.setEntity(new StringEntity(new Gson().toJson(data)));
              final HttpResponse response = httpClient.execute(dataCheckRequest);
              final String responseFromSF = convertStreamToString(response.getEntity().getContent());
              final Map<String, Object> objectFromSF = JsonFlattener.flattenAsMap(responseFromSF);
              return objectFromSF;
          } catch (final Exception e) {
              e.printStackTrace();
          }
        }
        return new HashMap<>();
    }

    private Optional<Authorization> getOptionalAuthorization() {
        try {
            String authToken = "";
            final HttpPost authTokenRequest = new HttpPost(getAccessTokenUri());
            final HttpResponse response = httpClient.execute(authTokenRequest);
            final Map<String, String> authMap = objectMapper.readValue(response.getEntity().getContent(), Map.class);
            return Optional.of(new Authorization(authMap.get("access_token"), authMap.get("instance_url")));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    private String getAccessTokenUri() {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
           Class.forName("com.mysql.jdbc.Driver");
        } catch (final Exception e) {
            System.out.println("Internal server error occured : "+e.getMessage());
        }

        try (final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_connector_db","root","PeIjs5qbw!");
             final PreparedStatement preparedStatement = connection.prepareStatement(SQL)){
            final ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                stringBuilder.append(resultSet.getString("host_or_machine_address"))
                .append("?grant_type=").append(resultSet.getString("grant_type"))
                .append("&client_id=").append(resultSet.getString("client_id"))
                .append("&client_secret=").append(resultSet.getString("client_or_client_secret"))
                .append("&username=").append(resultSet.getString("login_id"))
                .append("&password=").append(resultSet.getString("password")).append(resultSet.getString("token"));
            }
            resultSet.close();
            return stringBuilder.toString();
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
        return configuration.getAccessTokenUri();
    }
}
package com.data.connector.service.gateway;

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
            final HttpPost authTokenRequest = new HttpPost(configuration.getAccessTokenUri());
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
}


package com.data.connector.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Authorization {
    private String authToken;
    private String baseUrl;
}

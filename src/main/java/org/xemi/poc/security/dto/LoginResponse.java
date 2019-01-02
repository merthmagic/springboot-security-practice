package org.xemi.poc.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private Long expireAt;

    private String type;
}

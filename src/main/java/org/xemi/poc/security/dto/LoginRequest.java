package org.xemi.poc.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class LoginRequest {

    private String loginName;

    private String password;
}

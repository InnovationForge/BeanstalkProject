package com.github.innovationforge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("authorities")
    private List<String> authorities;
}

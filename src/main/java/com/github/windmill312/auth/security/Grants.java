package com.github.windmill312.auth.security;

public enum Grants {

    AUTHORIZATION_GRANT("AUTHORIZATION"),
    OAUTH_GRANT("OAUTH");

    private String value;

    Grants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

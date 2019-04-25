package com.github.windmill312.auth.model.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "oauth_code", schema = "auth")
public class OAuthCode {

    private Integer id;
    private String code = UUID.randomUUID().toString();
    private String clientId;
    private String userAccessToken;
    private Instant expiresIn;

    public OAuthCode() {
    }

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(
            schema = "auth", name = "auth.oauth_code_id_seq",
            sequenceName = "auth.oauth_code_id_seq", allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "auth.oauth_code_id_seq")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "code", nullable = false)
    public String getCode() {
        return code;
    }

    public OAuthCode setCode(String code) {
        this.code = code;
        return this;
    }

    @Column(name = "client_id", nullable = false)
    public String getClientId() {
        return clientId;
    }

    public OAuthCode setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    @Column(name = "access_token", nullable = false)
    public String getUserAccessToken() {
        return userAccessToken;
    }

    public OAuthCode setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
        return this;
    }

    @Column(name = "expires_in", nullable = false)
    public Instant getExpiresIn() {
        return expiresIn;
    }

    public OAuthCode setExpiresIn(Instant expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
